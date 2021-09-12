package com.alva.testbrowser.activity

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.alva.testbrowser.R
import com.alva.testbrowser.adapter.PagerPhotoAdapter
import com.alva.testbrowser.adapter.PagerPhotoViewHolder
import com.alva.testbrowser.databinding.ActivityPhotoBinding
import com.alva.testbrowser.util.JavascriptInterface
import com.alva.testbrowser.util.PhotoViewModel
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PhotoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPhotoBinding.inflate(layoutInflater) }

    @Inject
    lateinit var pagerPhotoAdapter: PagerPhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(binding.constraint)
            duration = 300
        }
        window.sharedElementExitTransition = MaterialContainerTransform().apply {
            addTarget(binding.constraint)
            duration = 300
        }

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

        val viewModel by viewModels<PhotoViewModel>()
        binding.viewPager2.apply {
            adapter = pagerPhotoAdapter
            viewModel.photoList.observe(this@PhotoActivity, {
                pagerPhotoAdapter.submitList(it)
                setCurrentItem(JavascriptInterface.index, false)
            })
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.photoTag.text = getString(
                        R.string.photo_tag,
                        position + 1,
                        pagerPhotoAdapter.itemCount
                    )
                }
            })
        }
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->    //动态申请权限
                permissions.entries.forEach {
                    Log.d("Hello", "onCreate: ${it.key} = ${it.value}")
                }
                if (permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
                    lifecycleScope.launch(Dispatchers.IO) { savePhoto() }
                } else {
                    Toast.makeText(this, getString(R.string.save_fail), Toast.LENGTH_SHORT).show()
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
        val saveUri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        ) ?: kotlin.run {
            MainScope().launch {
                Toast.makeText(
                    this@PhotoActivity,
                    getString(R.string.save_fail),
                    Toast.LENGTH_SHORT
                ).show()
            }
            return
        }
        contentResolver.openOutputStream(saveUri).use {
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)) {  //保存图片
                MainScope().launch {
                    Toast.makeText(
                        this@PhotoActivity,
                        getString(R.string.save_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                MainScope().launch {
                    Toast.makeText(
                        this@PhotoActivity,
                        getString(R.string.save_fail),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}