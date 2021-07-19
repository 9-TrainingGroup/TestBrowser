package com.alva.testbrowser.test

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.alva.testbrowser.databinding.ActivityNewsBinding
import com.google.android.material.appbar.AppBarLayout

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.appbar.layoutParams.height =
//            windowManager.currentWindowMetrics.bounds.height() / 2 - 56
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

        })
    }

}