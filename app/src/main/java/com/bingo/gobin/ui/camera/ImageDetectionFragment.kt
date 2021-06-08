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
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bingo.gobin.R
import com.bingo.gobin.data.content.DataContent
import com.bingo.gobin.databinding.FragmentImageDetectionBinding
import com.bingo.gobin.ml.PlasticTypeDetection
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
//        binding.btnPredict.setOnClickListener {
//            predict(plasticLabel!!)
//
//        }
    }

    private fun predict(plasticLabel: List<String>) {
        if (this.bitmap != null) {
            val resized: Bitmap = Bitmap.createScaledBitmap(bitmap!!, 224, 224, true)
            val model = PlasticTypeDetection.newInstance(requireContext())

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
            Log.d("main",outputFeature0.floatArray.size.toString())
            val tempArray = outputFeature0.floatArray.sortedDescending()
            for (i in outputFeature0.floatArray.indices) {
                Log.d("main", "${plasticLabel[i]} = " + outputFeature0.floatArray[i].toString())
            }


            Log.d("main", "hasil yang tertinggi ke ${plasticLabel[max]}")
            val listPlasticType = DataContent.getContent()[max]

            binding.apply {
                tvNoPhoto.visibility = View.GONE
                tvPredictTitle.text = listPlasticType.name
                tvPredictRecyclable.text = listPlasticType.type
                tvPredictProperties.text = listPlasticType.description
                tvPredictTitle.visibility = View.VISIBLE
                tvPredictProperties.visibility = View.VISIBLE
                tvPredictRecyclable.visibility = View.VISIBLE
            }
//            binding.tvPredictTitle.text = "hasil " + plasticLabel[max]
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
        CropImage.activity().setAspectRatio(3, 3).setGuidelines(CropImageView.Guidelines.ON)
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
//                binding.imageCamera.setImageURI(cropUri)
                binding.imageCamera.load(cropUri){
                    transformations(RoundedCornersTransformation(30f))
                }
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