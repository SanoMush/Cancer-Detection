package com.dicoding.asclepius.data.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(

    @field:SerializedName("articles")
    val articles: List<ArticlesItem>,

    )

data class ArticlesItem(

    @field:SerializedName("publishedAt")
    val publishedAt: String? = "no data",

    @field:SerializedName("author")
    val author: String? ="no data",

    @field:SerializedName("urlToImage")
    val urlToImage: String? = "no data",

    @field:SerializedName("description")
    val description: String? = "no data",

    @field:SerializedName("title")
    val title: String? = "no data",

    @field:SerializedName("url")
    val url: String? = "no data",

    )