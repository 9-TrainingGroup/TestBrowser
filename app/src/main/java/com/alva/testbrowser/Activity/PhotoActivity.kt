package com.alva.testbrowser.Activity

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.alva.testbrowser.Adapter.PagerPhotoAdapter
import com.alva.testbrowser.Adapter.PagerPhotoViewHolder
import com.alva.testbrowser.R
import com.alva.testbrowser.databinding.ActivityPhotoBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.ArrayList

class PhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoBinding
    private lateinit var images: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this) {
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
        }
        val bundle = intent.getBundleExtra("bundle")!!
        images = bundle.getStringArrayList("imageUrls")!!
        val index = bundle.getInt("index")
        binding.viewPager2.apply {
            adapter = PagerPhotoAdapter(images)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    "${position + 1}/${images.size}".also { binding.photoTag.text = it }
                }
            })
            setCurrentItem(index, false)
        }
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->    //动态申请权限
                permissions.entries.forEach {
                    Log.d("Hello", "onCreate: ${it.key} = ${it.value}")
                }
                if (permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
                    lifecycleScope.launch { savePhoto() }
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
                lifecycleScope.launch { savePhoto() }
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
            Toast.makeText(this, getString(R.string.save_failed), Toast.LENGTH_SHORT).show()
            return
        }
        this.contentResolver.openOutputStream(saveUri).use {
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)) {  //保存图片
                Toast.makeText(this, getString(R.string.saved_success), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.save_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }
}