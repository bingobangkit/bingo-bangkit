package com.bingo.gobin.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bingo.gobin.R
import com.bingo.gobin.data.model.User
import com.bingo.gobin.databinding.ActivityRegisterBinding
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.*
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.Exception


@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
@SuppressLint("MissingPermission")
class RegisterActivity : AppCompatActivity(), PermissionsListener {
    private val binding: ActivityRegisterBinding by viewBinding()
    private val viewModel: AuthViewModel by viewModels()
    private var mapFragment: SupportMapFragment? = null
    private lateinit var mapBoxMap: MapboxMap
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var symbolManager: SymbolManager
    private lateinit var callback: LocationChangeListeningCallback
    private lateinit var locationEngine: LocationEngine

    companion object {
        const val REQUEST_CODE = 5678
        private const val ICON_ID = "ICON_ID"
        const val DEFAULT_INTERVAL_IN_MILLISECONDS = 2000L
        const val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5
    }

    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        val request = LocationEngineRequest
            .Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
            .build()
        locationEngine.requestLocationUpdates(request, callback, mainLooper)
        locationEngine.getLastLocation(callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_register)
        binding.btnRegister.setOnClickListener {
            register()
        }
        viewModel.last_latlng.observe(this, { latlng ->
            val cameraPosition = CameraPosition.Builder()
                .target(latlng)
                .zoom(16.0)
                .build()
            val placePickerOptions =
                PlacePickerOptions.builder().statingCameraPosition(cameraPosition).build()

            val intentBuilder = PlacePicker.IntentBuilder()
                .accessToken(getString(R.string.mapbox_access_token))
                .placeOptions(placePickerOptions).build(this)

            binding.btnPickLocationRegister.setOnClickListener {
                startActivityForResult(
                    intentBuilder, REQUEST_CODE
                )
            }
        })


        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val options = MapboxMapOptions.createFromAttributes(this, null)
            mapFragment = SupportMapFragment.newInstance(options)
            transaction.add(R.id.map_register, mapFragment!!, "com.mapbox.map")
            transaction.commit()
        } else {
            mapFragment =
                supportFragmentManager.findFragmentByTag("com.mapbox.map") as SupportMapFragment
        }

        mapFragment?.getMapAsync { mapBoxMap ->
            this.mapBoxMap = mapBoxMap
            mapBoxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                callback = LocationChangeListeningCallback()
                enableLocationComponent(style)
            }
        }





    }

    @SuppressLint("LogNotTimber")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            // Retrieve the information from the selected location's CarmenFeature
            val carmenFeature = PlacePicker.getPlace(data)
            val latitude = carmenFeature?.center()?.latitude()
            val longitude = carmenFeature?.center()?.longitude()
            viewModel._latlng.postValue(LatLng(latitude!!, longitude!!))
        }
    }

    private fun register() {
        val name = binding.fieldName.editText?.text.toString()
        val email = binding.fieldEmail.editText?.text.toString()
        val phone = binding.fieldPhone.editText?.text.toString()
        val password = binding.fieldPassword.editText?.text.toString()
        val data = User(name = name, phone = phone)

        if (name.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Cek kembali form Anda!", Toast.LENGTH_SHORT).show()
            return
        } else {
            viewModel.register(user = data, email = email, password = password)
                .observe(this, {
                    if (it) {
                        Toast.makeText(
                            this,
                            "Berhasil Register, silakan login!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        Toast.makeText(this, "Gagal Register", Toast.LENGTH_SHORT).show()
                    }
                })
        }

    }

    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            initLocationEngine()
            val locationComponent = mapBoxMap.locationComponent
            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(this, loadedMapStyle).build()
            )
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.NORMAL



        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, "Perlu Izin Lokasi", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {

            mapBoxMap.getStyle { style ->
                callback = LocationChangeListeningCallback()
                enableLocationComponent(style)
            }


        } else {
            Toast.makeText(this, "Perlu Izin Lokasi", Toast.LENGTH_LONG).show()
        }
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
                    val position = CameraPosition.Builder()
                        .target(latLng)
                        .zoom(13.0) //disable this for not follow zoom
                        .tilt(10.0)
                        .build()
//                    mapBoxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position))
                    viewModel.last_latlng.postValue(latLng)
                }
            }

        }

        override fun onFailure(exception: Exception) {

        }
    }
}