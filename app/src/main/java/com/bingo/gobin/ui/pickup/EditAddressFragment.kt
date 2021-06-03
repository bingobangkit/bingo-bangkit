@file:Suppress("DEPRECATION")

package com.bingo.gobin.ui.pickup
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bingo.gobin.R
import com.bingo.gobin.databinding.FragmentEditAddressBinding
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.plugins.places.picker.*
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker.getPlace
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Suppress("DEPRECATION")
class EditAddressFragment : Fragment(), PermissionsListener {
    private val viewModel: ScheduleViewModel by activityViewModels()
    companion object{
        private const val DEFAULT_INTERVAL_IN_MILLISECONDS = 2000L
        private const val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5
        private const val REQUEST_CODE = 5678
    }
    private lateinit var callback: LocationChangeListeningCallback
    private lateinit var locationEngine: LocationEngine
    private var _binding: FragmentEditAddressBinding? = null
    private val binding get() = _binding!!



    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(requireContext())
        val request = LocationEngineRequest
            .Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
            .build()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationEngine.requestLocationUpdates(request, callback, Looper.getMainLooper())
        locationEngine.getLastLocation(callback)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(requireContext(), getString(R.string.access_token))
        _binding = FragmentEditAddressBinding.inflate(layoutInflater)
        callback = LocationChangeListeningCallback()
        initLocationEngine()
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.last_latlng.observe(viewLifecycleOwner, { latlng ->
            val cameraPosition = CameraPosition.Builder()
                .target(latlng)
                .zoom(16.0)
                .build()
            val placePickerOptions =
                PlacePickerOptions.builder().statingCameraPosition(cameraPosition).build()

            val intentBuilder = PlacePicker.IntentBuilder()
                .accessToken(getString(R.string.mapbox_access_token))
                .placeOptions(placePickerOptions).build(requireActivity())

            binding.btnPickLocation.setOnClickListener {
                startActivityForResult(
                    intentBuilder, REQUEST_CODE
                )
            }
        })


        binding.btnSetLocation.setOnClickListener {
            if (!binding.txtSetAddress.text.isNullOrBlank()) {
                viewModel.setAddress(binding.txtSetAddress.text.toString())
                if (!binding.txtCoordinate.text.isNullOrBlank() || binding.txtCoordinate.hint.isNullOrBlank()) {
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(context, "Please Pick Location :)", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the information from the selected location's CarmenFeature
            val carmenFeature = getPlace(data)
            val latitude = carmenFeature?.center()?.latitude()
            val longitude = carmenFeature?.center()?.longitude()
            viewModel._latlng.postValue(LatLng(latitude!!, longitude!!))
            viewModel.latitude.postValue(latitude.toString())
            viewModel.longitude.postValue(longitude.toString())
            binding.txtCoordinate.text = Editable.Factory.getInstance().newEditable( "$latitude,${longitude}")
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
    private inner class LocationChangeListeningCallback :
        LocationEngineCallback<LocationEngineResult> {

        override fun onSuccess(result: LocationEngineResult?) {
            result?.lastLocation
                ?: return //BECAREFULL HERE, IF NAME LOCATION UPDATE DONT USER -> val resLoc = result.lastLocation ?: return
            if (result.lastLocation != null) {
                val lat = result.lastLocation?.latitude!!
                val lng = result.lastLocation?.longitude!!
                val latLng = LatLng(lat, lng)

                if (result.lastLocation != null) {
                    viewModel.last_latlng.postValue(latLng)
                }
            }

        }

        override fun onFailure(e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(context, "Perlu Izin Lokasi", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted){
            callback = LocationChangeListeningCallback()
            initLocationEngine()
        }else{
            Toast.makeText(context, "Perlu Izin Lokasi", Toast.LENGTH_LONG).show()
        }
    }


}