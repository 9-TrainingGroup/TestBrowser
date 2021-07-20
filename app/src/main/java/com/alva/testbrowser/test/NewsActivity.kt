package com.alva.testbrowser.test

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.alva.testbrowser.R
import com.alva.testbrowser.databinding.ActivityNewsBinding
import com.google.android.material.appbar.AppBarLayout

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.appbar.layoutParams.height =
//            windowManager.currentWindowMetrics.bounds.height() / 2 - 56
        navController =
            (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            controller.navigatorProvider
            if (destination.id == R.id.webFragment) {
                binding.appbar.setExpanded(false)
                binding.appbar.isLiftOnScroll
            }
        }
//        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//
//        })
    }

}