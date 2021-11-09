package com.alva.testbrowser.activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.alva.testbrowser.R
import com.alva.testbrowser.databinding.ActivityNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {
    val binding by lazy { ActivityNewsBinding.inflate(layoutInflater) }
    private val navController by lazy { binding.fragmentContainerView.getFragment<NavHostFragment>().navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Handler(mainLooper).postDelayed({
            binding.appbar.setExpanded(false)
        }, 800)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.webFragment) {
                binding.refreshNews.setImageResource(R.drawable.button_menu)
                binding.appbar.setExpanded(false)
            } else {
                binding.refreshNews.setImageResource(R.drawable.button_refresh)
            }
        }
    }

}