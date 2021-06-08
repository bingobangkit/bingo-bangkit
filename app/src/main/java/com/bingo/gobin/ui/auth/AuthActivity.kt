package com.bingo.gobin.ui.auth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.getSystemService
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bingo.gobin.R
import com.bingo.gobin.databinding.ActivityAuthBinding
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
@ExperimentalCoroutinesApi
class AuthActivity : AppCompatActivity(), PermissionRequest.Listener {
    private val request by lazy {
        permissionsBuilder(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).build()}
    private val viewModel: AuthViewModel by viewModels()
    private val binding: ActivityAuthBinding by viewBinding()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        request.send()
        with(binding) {
            btnRegister.setOnClickListener {
                if(locationEnabled()){
                    val intent = Intent(this@AuthActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent)
                }

            }
            btnLogin.setOnClickListener {
                login()
            }

        }
    }

    private fun locationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return gpsStatus
    }

    private fun login() {
        with(binding) {
            val email = fieldEmailLogin.text.toString()
            val password = fieldPassword.text.toString()
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this@AuthActivity, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password).observe(this@AuthActivity, {
                    if (it) {
                        Toast.makeText(this@AuthActivity, "Berhasil Login", Toast.LENGTH_SHORT).show()

                        finish()
                    } else {
                        Toast.makeText(this@AuthActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    override fun onPermissionsResult(result: List<PermissionStatus>) {
        val context = this.applicationContext
        when {
            result.anyPermanentlyDenied() -> Toast.makeText(context, "Harus menambah permission", Toast.LENGTH_SHORT).show()
            result.anyShouldShowRationale() -> Toast.makeText(context, "Harus menambah permission", Toast.LENGTH_SHORT).show()
            result.allGranted() -> {
                return
            }
        }
    }
}