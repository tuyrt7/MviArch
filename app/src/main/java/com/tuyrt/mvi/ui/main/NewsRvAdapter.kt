package com.tuyrt.mvi.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hi.dhl.binding.viewbind
import com.tuyrt.mvi.R
import com.tuyrt.mvi.R.layout
import com.tuyrt.mvi.data.bean.NewsItem
import com.tuyrt.mvi.databinding.ItemViewBinding
import com.tuyrt.mvi.ext.inflate

/**
 * Created by tuyrt7 on 2022/1/17.
 * 说明：
 */
class NewsRvAdapter(
    private val listener: (View) -> Unit
) : ListAdapter<NewsItem, NewsRvAdapter.MyViewHolder>(NewsItem.CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(inflate(parent.context, layout.item_view, parent), listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MyViewHolder(view: View, listener: (View) -> Unit) : RecyclerView.ViewHolder(view) {

        private val binding: ItemViewBinding by viewbind()

        init {
            itemView.setOnClickListener(listener)
        }

        fun bind(newsItem: NewsItem) {
            itemView.tag = newsItem

            binding.run {
                tvTitle.text = newsItem.title
                tvDescription.text = newsItem.description
                ivThumbnail.load(newsItem.imageUrl) {
                    crossfade(true)
                    placeholder(R.mipmap.ic_launcher)
                }
            }
        }
    }
}

