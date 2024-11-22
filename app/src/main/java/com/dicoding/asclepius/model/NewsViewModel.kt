package com.dicoding.asclepius.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.response.NewsResponse
import com.dicoding.asclepius.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class NewsViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _articlesItems = MutableLiveData<List<ArticlesItem>?>()
    val articlesItem: LiveData<List<ArticlesItem>?> = _articlesItems

    fun setArticlesItems(query: String, category: String, language: String, apiKey: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getNews(query, category, language, apiKey)
        client.enqueue(object : retrofit2.Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val news = response.body()?.articles
                    _articlesItems.value = news
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("responseError", "onFailure: ${t.message}")
            }

        })
    }
}