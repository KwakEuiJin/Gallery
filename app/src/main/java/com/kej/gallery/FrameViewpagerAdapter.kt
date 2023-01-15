package com.kej.gallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kej.gallery.databinding.ItemFrameBinding

class FrameViewpagerAdapter(private val list: List<FrameItem>) : RecyclerView.Adapter<FrameViewpagerAdapter.FrameViewHolder>() {

    inner class FrameViewHolder(private val binding: ItemFrameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FrameItem) {
            binding.imageView.setImageURI(item.uri)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameViewHolder {
        return FrameViewHolder(ItemFrameBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FrameViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

data class FrameItem(val uri: Uri)
