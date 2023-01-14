package com.kej.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kej.gallery.databinding.ItemImageBinding
import com.kej.gallery.databinding.ItemImageMoreBinding

class ImageAdapter : ListAdapter<ImageItems, RecyclerView.ViewHolder>(diffUtil) {
    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ImageItems>() {
            override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
                return oldItem == newItem
            }
        }

        private const val IMAGE_TYPE = 10010
        private const val IMAGE_MORE_TYPE = 10020

    }

    inner class ImageViewHolder(
        private val itemImageBinding: ItemImageBinding
    ) : RecyclerView.ViewHolder(itemImageBinding.root) {
        fun bind(item: ImageItems.Image) {
            itemImageBinding.previewImageView.setImageURI(item.uri)
        }
    }

    inner class ImageMoreViewHolder(
        private val itemImageMoreBinding: ItemImageMoreBinding
    ) : RecyclerView.ViewHolder(itemImageMoreBinding.root) {
        fun bind() {
        }
    }

    override fun getItemCount(): Int {
        val originSize = currentList.size
        return if (currentList.size == 0) {
            0
        } else {
            originSize.inc()
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (itemCount.dec() == position) {
            IMAGE_MORE_TYPE
        } else {
            IMAGE_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            IMAGE_MORE_TYPE -> {
                val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                ImageMoreViewHolder(ItemImageMoreBinding.inflate(inflater, parent, false))

            }
            else -> {
                val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                ImageViewHolder(ItemImageBinding.inflate(inflater, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                holder.bind(currentList[position] as ImageItems.Image)
            }
            is ImageMoreViewHolder -> {
                holder.bind()
            }
        }
    }


}