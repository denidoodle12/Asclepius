package com.dicoding.asclepius.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemsArticleBinding

class TopHeadlinesAdapter(private val onClickItem: (ArticlesItem) -> Unit ) : ListAdapter<ArticlesItem, TopHeadlinesAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemsArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val headlines = getItem(position)
        holder.bind(headlines, onClickItem)
    }

    class MyViewHolder(private val binding: ItemsArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(headlinesItem: ArticlesItem, onClickItem: (ArticlesItem) -> Unit) {
            Glide.with(binding.ivMediaCover.context)
                .load(headlinesItem.urlToImage)
                .into(binding.ivMediaCover)
            binding.tvAuthor.text = headlinesItem.author
            binding.tvTitleName.text = headlinesItem.title
            binding.tvDescHeadlines.text = headlinesItem.description
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ArticlesItem> =
            object : DiffUtil.ItemCallback<ArticlesItem>() {
                override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                    return oldItem == newItem
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                    return oldItem == newItem
                }
            }
    }

}