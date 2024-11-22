package com.dicoding.asclepius.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.databinding.SavedItemRowBinding
import com.dicoding.asclepius.db.SavedAnalyze

class SavedAdapter : ListAdapter<SavedAnalyze, SavedAdapter.SavedViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedAdapter.SavedViewHolder {
        val binding = SavedItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedAdapter.SavedViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    inner class SavedViewHolder(private val binding: SavedItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SavedAnalyze) {
            binding.tvConfidenceScore.text = data.confidence_score
            Glide.with(binding.root.context)
                .load(data.image)
                .into(binding.imgSavedPhoto)
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<SavedAnalyze> =
            object: DiffUtil.ItemCallback<SavedAnalyze>() {
                override fun areItemsTheSame(
                    oldItem: SavedAnalyze,
                    newItem: SavedAnalyze
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: SavedAnalyze,
                    newItem: SavedAnalyze
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }
}