package com.kej.gallery

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.kej.gallery.databinding.ActivityMainBinding

@RequiresApi(Build.VERSION_CODES.M)
//API 분기 처리가 필요
class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_EXTERNAL_STORAGE_CODE = 100
    }

    private lateinit var binding: ActivityMainBinding
    private val imageLoadRegisterLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            updateImages(uriList)
        }
    private val imageAdapter = ImageAdapter {
        checkPermissions()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initViews()
        initRecyclerView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE_CODE -> {
                if ((grantResults.firstOrNull() ?: PackageManager.PERMISSION_DENIED) == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                }
            }
        }
    }

    private fun initViews() {
        binding.imageLoadButton.setOnClickListener {
            checkPermissions()
        }
        binding.navigateFrameButton.setOnClickListener {
            navigateToFrameActivity()
        }
    }

    private fun navigateToFrameActivity() {
        val images = imageAdapter.currentList.filterIsInstance<ImageItems.Image>().map { it.uri.toString() }.toTypedArray()
        val intent = Intent(this, FrameActivity::class.java)
        intent.putExtra("images", images)
        startActivity(intent)
    }


    private fun initRecyclerView() {
        binding.imageRecyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
        }
    }

    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadImage()
            }

            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showPermissionInfoDialog()
            }
            else -> {
                requestReadExternalStorage()
            }
        }

    }

    private fun loadImage() {
        imageLoadRegisterLauncher.launch("image/*")
    }

    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("이미지를 가져오기 위해서 저장 공간 권한이 필요합니다.")
            setPositiveButton("허용") { _, _ ->
                requestReadExternalStorage()
            }
            setNegativeButton("거부", null)
        }.show()
    }

    private fun requestReadExternalStorage() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_EXTERNAL_STORAGE_CODE
        )
    }


    private fun updateImages(uriList: List<Uri>) {
        val imageUriList = uriList.map {
            ImageItems.Image(it)
        }
        imageAdapter.submitList(imageUriList)
    }
}