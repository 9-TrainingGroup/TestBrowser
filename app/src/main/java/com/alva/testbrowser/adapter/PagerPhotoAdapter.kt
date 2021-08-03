package com.alva.testbrowser.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alva.testbrowser.R
import com.alva.testbrowser.databinding.PagerPhotoViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class PagerPhotoAdapter : ListAdapter<String, PagerPhotoViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerPhotoViewHolder(
        PagerPhotoViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: PagerPhotoViewHolder, position: Int) {
        holder.viewBinding.shimmerLayout.apply {
            setShimmerColor(Color.argb(178, 255, 255, 255))
            setShimmerAngle(30)
            startShimmerAnimation()
        }
        Glide.with(holder.itemView)
            .load(getItem(position))
            .placeholder(R.drawable.ic_baseline_photo)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false.also { holder.viewBinding.shimmerLayout.stopShimmerAnimation() }
                }
            })
            .into(holder.viewBinding.pagerPhoto)
    }

    object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }
}

class PagerPhotoViewHolder(val viewBinding: PagerPhotoViewBinding) :
    RecyclerView.ViewHolder(viewBinding.root)