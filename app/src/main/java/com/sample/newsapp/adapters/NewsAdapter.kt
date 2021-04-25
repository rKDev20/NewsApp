package com.sample.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sample.newsapp.R
import com.sample.newsapp.data.model.NewsModel
import com.sample.newsapp.databinding.ItemNewsBinding
import com.sample.newsapp.glide.transformations.GradientTransformation

class NewsAdapter(private val listener: NewsClickListener) :
    RecyclerView.Adapter<NewsViewHolder>() {

    private val activeDescription: HashSet<String> = HashSet()

    var dataList: List<NewsModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = dataList[position]
        val binding = holder.binding
        binding.title.text = news.title
        Glide.with(binding.image)
            .load(news.urlToImage)
            .placeholder(R.drawable.placeholder_news)
            .transform(GradientTransformation())
            .into(binding.image)
        binding.description.text = news.description
        binding.source.text = binding.source.context.getString(R.string.source, news.source.name)
        binding.root.setOnClickListener {
            if (activeDescription.contains(news.title)) {
                listener.onClick(news)
            } else {
                binding.extraContent.visibility = View.VISIBLE
                activeDescription.add(news.title)
            }
        }
        binding.back.setOnClickListener {
            binding.extraContent.visibility = View.GONE
            activeDescription.remove(news.title)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class NewsViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

interface NewsClickListener {
    fun onClick(news: NewsModel)
}