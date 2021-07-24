package com.alva.testbrowser.activity

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.alva.testbrowser.adapter.PagerPhotoAdapter
import com.alva.testbrowser.adapter.PagerPhotoViewHolder
import com.alva.testbrowser.R
import com.alva.testbrowser.databinding.ActivityPhotoBinding
import com.alva.testbrowser.util.PhotoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class PhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoBinding
    private lateinit var viewModel: PhotoViewModel

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
        window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
        )

/*        onBackPressedDispatcher.addCallback(this) {
            Toast.makeText(
                this@PhotoActivity,
                getString(R.string.button_back_message),
                Toast.LENGTH_SHORT
            ).show()
            isEnabled = false
            lifecycleScope.launch {
                delay(1500)
                isEnabled = true
            }
        }*/

        val bundle = intent.getBundleExtra("bundle")!!

        viewModel = viewModels<PhotoViewModel>().value
        viewModel.photoList.value = bundle.getStringArrayList("imageUrls")
        val adapter = PagerPhotoAdapter()
        binding.viewPager2.adapter = adapter
        viewModel.photoList.observe(this, {
            adapter.submitList(it)
            binding.viewPager2.setCurrentItem(bundle.getInt("index"), false)
        })
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                "${position + 1}/${viewModel.photoList.value?.size}".also {
                    binding.photoTag.text = it
                }
            }
        })
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->    //动态申请权限
                permissions.entries.forEach {
                    Log.d("Hello", "onCreate: ${it.key} = ${it.value}")
                }
                if (permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
                    lifecycleScope.launch(Dispatchers.IO) { savePhoto() }
                } else {
                    Toast.makeText(this, getString(R.string.save_failed), Toast.LENGTH_SHORT).show()
                }
            }
        binding.saveButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT > 28    //已获取权限
            ) {
                lifecycleScope.launch(Dispatchers.IO) { savePhoto() }
            } else {    //未获取权限
                requestPermissionLauncher.launch(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                )
            }
        }
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun savePhoto() {
        val holder =
            (binding.viewPager2[0] as RecyclerView).findViewHolderForAdapterPosition(binding.viewPager2.currentItem) as PagerPhotoViewHolder
        val bitmap = holder.viewBinding.pagerPhoto.drawable.toBitmap()
        val saveUri = this.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        ) ?: kotlin.run {
            MainScope().launch {
                Toast.makeText(
                    this@PhotoActivity,
                    getString(R.string.save_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
            return
        }
        this.contentResolver.openOutputStream(saveUri).use {
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)) {  //保存图片
                MainScope().launch {
                    Toast.makeText(
                        this@PhotoActivity,
                        getString(R.string.saved_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                MainScope().launch {
                    Toast.makeText(
                        this@PhotoActivity,
                        getString(R.string.save_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}