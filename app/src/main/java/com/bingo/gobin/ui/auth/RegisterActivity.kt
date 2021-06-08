package com.bingo.gobin.ui.auth

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bingo.gobin.R
import com.bingo.gobin.data.model.User
import com.bingo.gobin.databinding.ActivityRegisterBinding
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.MapboxMapOptions
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.maps.SupportMapFragment
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
@SuppressLint("MissingPermission")
class RegisterActivity : AppCompatActivity(), PermissionsListener,  PermissionRequest.Listener {
    private val request by lazy {
        permissionsBuilder(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).build()
    }
    private val binding: ActivityRegisterBinding by viewBinding()
    private val viewModel: AuthViewModel by viewModels()
    private var mapFragment: SupportMapFragment? = null
    private lateinit var mapBoxMap: MapboxMap
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var callback: LocationChangeListeningCallback
    private lateinit var locationEngine: LocationEngine
    private var lastMarker : MarkerOptions? = null


    companion object {
        const val REQUEST_CODE = 5678
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
        request.send()
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
            val latLng = viewModel.last_latlng.value
            val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(16.0)
                .build()
            mapBoxMap.cameraPosition =cameraPosition
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
            val latLng = LatLng(latitude!!, longitude!!)
            viewModel._latlng.postValue(LatLng(latitude!!, longitude!!))
            lastMarker = MarkerOptions().position(latLng).title("Your Picked Location")
            mapBoxMap.addMarker(MarkerOptions().position(latLng).title("Your Picked Location"))
            val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(17.0)
                .build()
            mapBoxMap.cameraPosition =cameraPosition
            viewModel.last_latlng.postValue(latLng)
            mapBoxMap.locationComponent.isLocationComponentEnabled = false
            binding.btnPickLocationRegister.isEnabled = false
        }
    }

    private fun register() {
        viewModel._latlng.observe(this,{latlng->
            val latitude = latlng.latitude.toString()
            val longitude = latlng.longitude.toString()
            val name = binding.fieldName.editText?.text.toString()
            val email = binding.fieldEmail.editText?.text.toString()
            val phone = binding.fieldPhone.editText?.text.toString()
            val password = binding.fieldPassword.editText?.text.toString()
            val address = binding.txtSetAddress.text.toString()
            val data = User(name = name, phone = phone,address = address, latitude = latitude, longitude = longitude)
            if (name.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Cek kembali form Anda!", Toast.LENGTH_SHORT).show()
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
                            finish()
                        } else {
                            Toast.makeText(this, "Gagal Register", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        })




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
                    viewModel.last_latlng.postValue(latLng)
                }
            }

        }

        override fun onFailure(exception: Exception) {

        }
    }

    override fun onResume() {
        super.onResume()
        mapFragment?.onResume()
    }

    override fun onPermissionsResult(result: List<PermissionStatus>) {
        val context = this.applicationContext
        when {
            result.anyPermanentlyDenied() -> Toast.makeText(context, "Harus menambah permission", Toast.LENGTH_SHORT).show()
            result.anyShouldShowRationale() -> Toast.makeText(context, "Harus menambah permission", Toast.LENGTH_SHORT).show()
            result.allGranted() -> return
        }
    }
}