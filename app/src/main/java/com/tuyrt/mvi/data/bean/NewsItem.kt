package com.tuyrt.mvi.data.bean

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by tuyrt7 on 2022/1/17.
 * 说明：
 */
data class NewsItem(
    val title: String,
    val description: String,
    val imageUrl: String
) {
    companion object {
        val CALLBACK: DiffUtil.ItemCallback<NewsItem> = object : DiffUtil.ItemCallback<NewsItem>() {

            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem) = oldItem.title == newItem.title


            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem) = oldItem == newItem
        }
    }
}