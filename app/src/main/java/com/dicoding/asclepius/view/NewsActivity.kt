package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.retrofit.API_KEY
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.model.NewsViewModel

class NewsActivity : AppCompatActivity() {
    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setting up action bar with back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Initialize the adapter
        newsAdapter = NewsAdapter()

        // Set item click listener for the adapter
        newsAdapter.setOnItemClickCallback(object : NewsAdapter.OnItemClickCallback {
            override fun onItemClicked(news: ArticlesItem) {
                showUrlNews(news)
            }
        })

        // Fetch articles
        newsViewModel.setArticlesItems("cancer", "health", "en", API_KEY)
        newsViewModel.articlesItem.observe(this) { items ->
            setNewsData(items) // Passing data to adapter
        }

        // Set RecyclerView layout manager
        binding.rvNews.layoutManager = LinearLayoutManager(this)

        // Observe loading state
        newsViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        // Handle back press
        onBackPressedCallback()
    }

    private fun setNewsData(news: List<ArticlesItem>?) {
        // Jika news adalah null, maka gunakan list kosong sebagai gantinya
        val validNews = news ?: emptyList()

        Log.d("NewsActivity", "Data Berita: $validNews")
        // Pastikan adapter di-set sebelum submitList dipanggil
        if (binding.rvNews.adapter == null) {
            binding.rvNews.adapter = newsAdapter
        }
        newsAdapter.submitList(validNews) // Menggunakan validNews yang tidak null
    }

    private fun showUrlNews(news: ArticlesItem) {
        val urlString = news.url
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
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
