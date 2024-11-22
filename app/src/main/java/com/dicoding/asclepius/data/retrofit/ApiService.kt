package com.dicoding.asclepius.data.retrofit

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.response.NewsResponse
import okhttp3.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
const val API_KEY = BuildConfig.API_KEY
interface ApiService {
    @GET("v2/top-headlines")
    fun getNews(
        @Query("q") query: String = "cancer",
        @Query("category") category: String = "health",
        @Query("language") language: String = "en",
        @Query("apiKey") api: String = API_KEY
    ): retrofit2.Call<NewsResponse>
}