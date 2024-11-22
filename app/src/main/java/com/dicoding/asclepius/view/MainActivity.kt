package com.dicoding.asclepius.view

import ImageViewModel
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var cropImageView: CropImageView
    private val imageViewModel: ImageViewModel by viewModels() // Menggunakan ViewModel untuk mempertahankan data URI

    private var currentImageUri: Uri? = null

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        cropImageView = binding.cropImageView


        imageViewModel.imageUri?.let {
            currentImageUri = Uri.parse(it)
            binding.previewImageView.setImageURI(currentImageUri)
        }


        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.analyzeButton.setOnClickListener {
            if (currentImageUri != null) {
                analyzeImage()
            } else {
                showToast("No Image to Analyze")
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            croppedImageLauncher.launch(
                CropImageContractOptions(
                    uri,
                    CropImageOptions(
                        imageSourceIncludeGallery = true,
                        allowRotation = true,
                        allowFlipping = true,
                        allowCounterRotation = true,
                        guidelines = CropImageView.Guidelines.OFF
                    )
                )
            )
        } ?: showToast("No media selected")
    }

    private val croppedImageLauncher = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val croppedUri = result.uriContent
            currentImageUri = croppedUri
            imageViewModel.imageUri = croppedUri.toString()
            showImage()
        } else {
            showToast("Cropping failed: ${result.error}")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let {
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                val sortedCategories = it[0].categories.sortedByDescending { it?.score }
                                val topCategory = sortedCategories.firstOrNull()
                                topCategory?.let {
                                    val displayResult = "${it.label} " + NumberFormat.getPercentInstance()
                                        .format(it.score).trim()
                                    moveToResult(displayResult)
                                }
                            }
                        }
                    }
                }
            }
        )
        currentImageUri?.let { imageClassifierHelper.classifyStaticImage(it) }
    }

    private fun moveToResult(displayResult: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
        intent.putExtra(ResultActivity.EXTRA_RESULT, displayResult)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.history -> {
                val intent = Intent(this, SavedActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.news -> {
                val intent = Intent(this, NewsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
