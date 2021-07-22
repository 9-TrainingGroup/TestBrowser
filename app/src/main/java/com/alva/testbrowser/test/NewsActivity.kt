package com.alva.testbrowser.test

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.alva.testbrowser.R
import com.alva.testbrowser.databinding.ActivityNewsBinding

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
        val viewModel by viewModels<NewsViewModel>()
        navController =
            (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            controller.navigatorProvider
            if (destination.id == R.id.webFragment) {
                binding.appbar.setExpanded(false)
                binding.refreshNews.setImageResource(R.drawable.button_menu)
            } else {
                binding.refreshNews.setImageResource(R.drawable.button_refresh)
            }
        }
        binding.urlEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.filter(s.toString())
            }
        })
//        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//
//        })
    }

}