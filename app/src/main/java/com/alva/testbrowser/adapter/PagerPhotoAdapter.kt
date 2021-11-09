package com.alva.testbrowser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.alva.testbrowser.R
import com.alva.testbrowser.databinding.PagerPhotoViewBinding
import javax.inject.Inject

class PagerPhotoAdapter @Inject constructor() :
    ListAdapter<String, PagerPhotoViewHolder>(object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerPhotoViewHolder(
        PagerPhotoViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: PagerPhotoViewHolder, position: Int) {
        holder.viewBinding.apply {
            shimmerLayout.apply {
                setShimmerColor(0x55FFFFFF)
                setShimmerAngle(30)
                startShimmerAnimation()
            }
            pagerPhoto.load(getItem(position)) {
                placeholder(R.drawable.ic_baseline_photo)
                listener { _, _ ->
                    shimmerLayout.stopShimmerAnimation()
                }
            }
        }
    }
}

class PagerPhotoViewHolder(val viewBinding: PagerPhotoViewBinding) :
    RecyclerView.ViewHolder(viewBinding.root)