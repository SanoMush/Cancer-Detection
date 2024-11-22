package com.dicoding.asclepius.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.databinding.NewsItemRowBinding
import android.util.Log

class NewsAdapter : ListAdapter<ArticlesItem, NewsAdapter.NewsViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(news)
        }
    }


    interface OnItemClickCallback {
        fun onItemClicked(news: ArticlesItem)
    }


    inner class NewsViewHolder(private val binding: NewsItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: ArticlesItem) {

            Log.d("NewsAdapter", "Binding News: ${news.title}, ${news.publishedAt}, ${news.urlToImage}")


            binding.tvTitle.text = news.title ?: "No title"
            binding.tvDate.text = news.publishedAt ?: "No date"
            binding.tvAuthor.text = news.author ?: "No author"
            binding.tvDesc.text = news.description ?: "No description"

            // Menggunakan Glide untuk memuat gambar (jika ada)
            Glide.with(binding.root.context)
                .load(news.urlToImage ?: "No Image") // Default jika gambar kosong
                .into(binding.imgNewsPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
