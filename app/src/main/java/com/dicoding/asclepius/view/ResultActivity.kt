package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.db.SavedAnalyze
import com.dicoding.asclepius.model.SavedViewModel
import com.dicoding.asclepius.repository.SavedRepository
import com.dicoding.asclepius.utils.AppExecutors

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var isSaved= false
    private lateinit var savedViewModel: SavedViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowTitleEnabled(false);



        val savedRepository = SavedRepository.getInstance(application, AppExecutors())
        savedViewModel = SavedViewModel(savedRepository)

        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)

        imageUri?.let { uri ->
            binding.resultImage.setImageURI(Uri.parse(uri))
        }

        val displayResult = intent.getStringExtra(EXTRA_RESULT)
        binding.resultText.text = displayResult


        binding.saveBtn.setOnClickListener {
            if (isSaved) {
                showToast("Data has been saved, please go back to home page to analyze new image")
                isSaved = false
            } else {
                val savedData = SavedAnalyze(image = imageUri!!, confidence_score = displayResult!!)
                savedViewModel.saveData(savedData)
                showToast("Data is saved!")
                isSaved = true
            }
        }


        onBackPressedCallback()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Log.d("test", "Clicked")
                val intent = Intent(this, MainActivity::class.java)

                startActivity(intent)
                return true
            }
            R.id.history -> {
                Log.d("test", "Clicked History")
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



    private fun onBackPressedCallback() {
        val dispatcher = onBackPressedDispatcher

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

        dispatcher.addCallback(this, onBackPressedCallback)

    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }

}