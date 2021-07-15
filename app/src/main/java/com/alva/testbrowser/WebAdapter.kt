package com.alva.testbrowser

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alva.testbrowser.databinding.CellBookmarkBinding

class BookmarkAdapter(private val allWebs: List<Bookmark>) :
    RecyclerView.Adapter<BookmarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        CellBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            return BookmarkViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val webs = allWebs[position]
        holder.viewBinding.textView.text = webs.name
        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(webs.url)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setClass(it.context, MainActivity::class.java)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return allWebs.size
    }
}

class BookmarkViewHolder(val viewBinding: CellBookmarkBinding) :
    RecyclerView.ViewHolder(viewBinding.root)

class HistoryAdapter(private val allWebs: List<History>) :
    RecyclerView.Adapter<HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        CellBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            return HistoryViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val webs = allWebs[position]
        holder.viewBinding.textView.text = webs.name
        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(webs.url)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setClass(it.context, MainActivity::class.java)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return allWebs.size
    }
}

class HistoryViewHolder(val viewBinding: CellBookmarkBinding) :
    RecyclerView.ViewHolder(viewBinding.root)