package com.alva.testbrowser.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alva.testbrowser.*
import com.alva.testbrowser.databinding.ActivityWebListBinding
import com.google.android.material.tabs.TabLayoutMediator

class WebListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2

            override fun createFragment(position: Int) = when (position) {
                0 -> BookmarkFragment()
                else -> HistoryFragment()
            }
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.bookmark)
                else -> getString(R.string.history)
            }
        }.attach()
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
}