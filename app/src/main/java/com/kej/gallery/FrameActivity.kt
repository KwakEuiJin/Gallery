package com.kej.gallery

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.kej.gallery.databinding.ActivityFrameBinding

class FrameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFrameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_frame)
        getData()
        initTab()
    }

    private fun initTab() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab , position ->
            binding.viewPager.currentItem = tab.position
        }.attach()
    }

    private fun getData() {
        val images = (intent.getStringArrayExtra("images") ?: emptyArray()).map { uriString ->
            FrameItem(Uri.parse(uriString))
        }.toList()

        val frameAdapter = FrameViewpagerAdapter(images)
        binding.viewPager.adapter = frameAdapter
    }
}