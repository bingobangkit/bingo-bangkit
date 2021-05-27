package com.bingo.gobin.ui.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import com.bingo.gobin.databinding.ActivityCameraBinding
import java.io.File
import java.util.concurrent.ExecutorService

class CameraActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    companion object{
        const val CODE_CAMERA = 42
    }
    private lateinit var binding: ActivityCameraBinding


}