package com.kotlin.currentlocation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kotlin.currentlocation.databinding.ActivityMainBinding
import com.kotlin.currentlocation.utils.GeneralUtils.showToast
import com.kotlin.currentlocation.utils.PermissionManager

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionManager = PermissionManager.from(this)


    private fun initializations() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initializations()

        binding.mbGetMain.setOnClickListener { onGetCurrentLocation() }
    }

    private fun onGetCurrentLocation() {
        /*permissionManager
            .request(PermissionManager.ACCESS_COARSE_LOCATION)
            .setRationale()
            .setRationaleDialog(null)
            .hasPermission(object : PermissionManager.PermissionCheck {
                override fun onPermissionGranted() {
                    getMyCurrentLocation()
                }

                override fun onPermissionDenied() {
                    showToast("Permission Denied")
                }
            })*/

        permissionManager
            .requestMultiple(arrayListOf(PermissionManager.READ_EXTERNAL_STORAGE, PermissionManager.WRITE_EXTERNAL_STORAGE, PermissionManager.MANAGE_EXTERNAL_STORAGE, PermissionManager.ACCESS_FINE_LOCATION, PermissionManager.ACCESS_COARSE_LOCATION))
            .setRationale()
            .hasPermission(object : PermissionManager.PermissionCheck {
                override fun onPermissionGranted() {
                    getMyCurrentLocation()
                }

                override fun onPermissionDenied() {
                    showToast("Permission Denied")
                }
            })
    }


    private fun getMyCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            if (it.isSuccessful) {
                val location = it.result
                val text = "Location {Lat:${location.latitude}, Lng:${location.longitude}}"
                binding.tvCurrentLocation.text = text
            } else {
                showToast("Failed to show Location")
            }
        }
    }
}