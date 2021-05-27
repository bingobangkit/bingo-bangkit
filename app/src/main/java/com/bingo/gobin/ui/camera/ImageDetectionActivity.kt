package com.bingo.gobin.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bingo.gobin.databinding.ActivityImageDetectionBinding
import com.bingo.gobin.ml.ConvertedModel
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class ImageDetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageDetectionBinding
    lateinit var bitmap: Bitmap

    companion object {
        const val CODE_CAMERA = 42
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fileName = "label.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        var plasticLabel = inputString.split("\n")

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CODE_CAMERA
            )
        }

        binding.btnCamera.setOnClickListener {
            startCropActivity()

        }
        binding.btnPredict.setOnClickListener {
            val resized: Bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val model = ConvertedModel.newInstance(this)

            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            val tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(resized)
            val byteBuffer = tensorImage.buffer
            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            val max = getMax(outputFeature0.floatArray)

            model.close()
            for (i in 0 until outputFeature0.floatArray.size - 1) {
                Log.d("main", "${plasticLabel[i]} = " + outputFeature0.floatArray[i].toString())
            }
            Log.d("main", "hasil yang tertinggi ke ${plasticLabel[max]}")
            binding.tvPredictResult.text = "hasil " + plasticLabel[max].toString()
        }


    }

    fun getMax(arr: FloatArray): Int {
        var index = 0
        var min = 0.0


        for (i in 0 until arr.count() - 1) {
            if (arr[i] > min) {
                index = i
                min = arr[i].toDouble()
            }
        }

        return index
    }


    fun startCropActivity() {
        CropImage.activity().setAspectRatio(8, 8).setGuidelines(CropImageView.Guidelines.ON)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result: CropImage.ActivityResult? = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val cropUri = result?.uriContent
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, cropUri)
                binding.imageCamera.setImageURI(cropUri)
            } else {
                Log.d("image", "error")
            }
        }

    }
}