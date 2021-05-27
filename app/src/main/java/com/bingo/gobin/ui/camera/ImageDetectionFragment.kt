@file:Suppress("DEPRECATION")

package com.bingo.gobin.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bingo.gobin.R
import com.bingo.gobin.data.content.Content
import com.bingo.gobin.data.content.DataContent
import com.bingo.gobin.databinding.FragmentImageDetectionBinding
import com.bingo.gobin.ml.ConvertedModel
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


class ImageDetectionFragment : Fragment() {

    private val binding: FragmentImageDetectionBinding by viewBinding()
    private var bitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_detection, container, false)
    }

    private val fileName = "label.txt"
    private var inputString : String? = null
    private var plasticLabel = inputString?.split("\n")


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputString = requireActivity().application.assets.open(fileName).bufferedReader()
            .use { it.readText() }
        plasticLabel = inputString!!.split("\n")

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CODE_CAMERA
            )
        }

        binding.btnCamera.setOnClickListener {
            startCropActivity()
        }
        binding.btnPredict.setOnClickListener {
            predict(plasticLabel!!)

        }
    }

    private fun predict(plasticLabel: List<String>) {
        if (this.bitmap != null) {
            val resized: Bitmap = Bitmap.createScaledBitmap(bitmap!!, 224, 224, true)
            val model = ConvertedModel.newInstance(requireContext())

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
            binding.tvPredictResult.text = "hasil " + plasticLabel[max]

            val listType : List<Content> = DataContent.getContent()
            val content = listType.find { it.name == plasticLabel[max] }
        }
    }

    private fun getMax(arr: FloatArray): Int {
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


    private fun startCropActivity() {
        CropImage.activity().setAspectRatio(8, 8).setGuidelines(CropImageView.Guidelines.ON)
            .start(requireContext(), this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result: CropImage.ActivityResult? = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val cropUri = result?.uriContent
                bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, cropUri)
                binding.imageCamera.setImageURI(cropUri)
                predict(plasticLabel!!)
            } else {
                Log.d("image", "error")
            }
        }


    }


    companion object {
        const val CODE_CAMERA = 42
    }

}