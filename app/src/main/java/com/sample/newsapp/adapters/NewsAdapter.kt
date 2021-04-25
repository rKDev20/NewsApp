package com.sample.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sample.newsapp.R
import com.sample.newsapp.data.model.NewsModel

class NewsAdapter : RecyclerView.Adapter<NewsViewHolder>() {

    var dataList: List<NewsModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = dataList[position]
        holder.title.text = news.title
        Glide.with(holder.image)
            .load(news.urlToImage)
            .placeholder(R.drawable.placeholder_news)
            .into(holder.image)
        holder.description.text = news.description
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.title)
    val image: ImageView = itemView.findViewById(R.id.image)
    val description: TextView = itemView.findViewById(R.id.description)
}