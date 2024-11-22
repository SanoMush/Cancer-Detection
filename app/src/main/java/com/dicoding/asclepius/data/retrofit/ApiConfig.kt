package com.dicoding.asclepius.data.retrofit

import com.dicoding.asclepius.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = BuildConfig.BASE_URL
class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val authInterceptor = Interceptor {chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .build()
                chain.proceed(requestHeaders)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}