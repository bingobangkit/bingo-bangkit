@file:Suppress("DEPRECATION")

package com.bingo.gobin.ui.pickup

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.bingo.gobin.MainActivity
import com.bingo.gobin.R
import com.bingo.gobin.databinding.FragmentEditAddressBinding
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Suppress("DEPRECATION")
class EditAddressFragment : Fragment() {
    private val viewModel : PickupViewModel by viewModels()
    private val PLACE_PICKER_REQUEST = 1
    private var _binding: FragmentEditAddressBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditAddressBinding.inflate(layoutInflater)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPickLocation.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()
            try {
                startActivityForResult(builder.build(requireActivity()), PLACE_PICKER_REQUEST)
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            }
        }

        binding.btnSetLocation.setOnClickListener {
            if (!binding.txtSetAddress.text.isNullOrBlank() && !binding.txtCoordinate.text.isNullOrBlank()){
                viewModel.setAddress(binding.txtSetAddress.text.toString())

            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val place = PlacePicker.getPlace(data, context)
                Log.d("TAG", "onActivityResult: ${place.latLng.latitude},${place.latLng.longitude}")
                "${place.latLng.latitude},${place.latLng.longitude}".also {
                    binding.txtCoordinate.text =
                        Editable.Factory.getInstance().newEditable(it)
                }
                binding.txtNumberLongitude.hint = ""
                if (!place.name.isNullOrBlank()) {
                    binding.txtSetAddress.text = place.name as Editable?
                }
            }

        }
    }
}