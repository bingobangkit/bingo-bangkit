@file:Suppress("DEPRECATION")

package com.bingo.gobin.ui.pickup

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bingo.gobin.R
import com.bingo.gobin.databinding.FragmentMapsBinding
import com.google.android.gms.location.places.ui.PlacePicker

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Suppress("DEPRECATION")
class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    private fun getLocation(latitude:Float,longitude:Float) {
        val supportMapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync { googleMap ->

            val markerOptions = MarkerOptions()

            val addressUser = LatLng(
                latitude.toDouble(), longitude.toDouble()
            )

            markerOptions.position(addressUser)
            markerOptions.title("user")
            googleMap.clear()

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addressUser, 15f))
            googleMap.addMarker(markerOptions)


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                val place = PlacePicker.getPlace(data,context)
                Log.d("TAG", "onActivityResult: ${place.latLng.latitude},${place.latLng.longitude}")
                val latitude = place.latLng.latitude.toFloat()
                val longitude = place.latLng.longitude.toFloat()
                getLocation(latitude, longitude)
            }
        }
    }
}
