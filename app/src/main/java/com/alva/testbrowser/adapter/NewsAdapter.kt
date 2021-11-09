package com.alva.testbrowser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.alva.testbrowser.R
import com.alva.testbrowser.database.NewsItem
import com.alva.testbrowser.databinding.CellNewsBinding
import com.alva.testbrowser.databinding.FooterNewsBinding
import com.alva.testbrowser.fragment.InfoFragmentDirections
import javax.inject.Inject

class NewsAdapter @Inject constructor() :
    PagingDataAdapter<NewsItem, NewsViewHolder>(object : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem) = oldItem == newItem
    }) {
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = getItem(position)
        if (newsItem != null) {
            holder.viewBinding.apply {
                shimmerLayout.apply {
                    setShimmerColor(0x55FFFFFF)
                    setShimmerAngle(30)
                    startShimmerAnimation()
                }
                textTitle.text = newsItem.title
                textAuthor.text = newsItem.author
                textTime.text = newsItem.time
                imageView.load(newsItem.img) {
                    placeholder(R.drawable.ic_baseline_photo)
                    listener { _, _ ->
                        shimmerLayout.stopShimmerAnimation()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val holder = NewsViewHolder(
            CellNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        holder.itemView.setOnClickListener {
            val url = if (getItem(holder.absoluteAdapterPosition)?.url.isNullOrEmpty()) {
                "https://3g.163.com/news/article/${getItem(holder.absoluteAdapterPosition)?.id}.html"
            } else {
                getItem(holder.absoluteAdapterPosition)?.url
            }
            val action = InfoFragmentDirections.actionInfoFragmentToWebFragment(url)
            it.findNavController().navigate(action)
        }
        return holder
    }
}

class NewsViewHolder(val viewBinding: CellNewsBinding) : RecyclerView.ViewHolder(viewBinding.root)

class FooterAdapter(private val retry: () -> Unit) : LoadStateAdapter<FooterViewHolder>() {
    override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) {
        holder.viewBinding.apply {
            when (loadState) {
                is LoadState.Loading -> {
                    textView.setText(R.string.footer_loading)
                    progressBar.visibility = View.VISIBLE
                    holder.itemView.isClickable = false
                }
                is LoadState.Error -> {
                    textView.setText(R.string.footer_error)
                    progressBar.visibility = View.GONE
                    holder.itemView.isClickable = true
                }
                else -> {
                    textView.setText(R.string.footer_success)
                    progressBar.visibility = View.GONE
                    holder.itemView.isClickable = false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FooterViewHolder {
        val holder = FooterViewHolder(
            FooterNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        holder.itemView.setOnClickListener {
            retry()
        }
        return holder
    }
}

class FooterViewHolder(val viewBinding: FooterNewsBinding) :
    RecyclerView.ViewHolder(viewBinding.root)