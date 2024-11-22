package com.dicoding.asclepius.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.SavedAdapter
import com.dicoding.asclepius.databinding.ActivitySavedBinding
import com.dicoding.asclepius.db.SavedAnalyze
import com.dicoding.asclepius.model.SavedViewModel
import com.dicoding.asclepius.repository.SavedRepository
import com.dicoding.asclepius.utils.AppExecutors

class SavedActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedBinding
    private lateinit var savedViewModel: SavedViewModel
    private lateinit var savedRepository: SavedRepository
    private lateinit var savedAdapter: SavedAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowTitleEnabled(false);

        savedRepository = SavedRepository.getInstance(application, AppExecutors())
        savedAdapter = SavedAdapter()

        val layoutManager = LinearLayoutManager(this)
        binding.rvSaved.layoutManager = layoutManager

        savedViewModel = SavedViewModel(savedRepository)
        savedViewModel.getSavedData().observe(this) {saved ->
            setSavedData(saved)
            binding.progressBar.visibility = View.GONE
        }


        onBackPressedCallback()

    }

    private fun setSavedData(data: List<SavedAnalyze>) {
        savedAdapter.submitList(data)
        binding.rvSaved.adapter = savedAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Log.d("test", "Clicked")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
}