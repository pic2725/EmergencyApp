package edu.utah.cs4530.emergency.ui.home

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import edu.utah.cs4530.emergency.R
import edu.utah.cs4530.emergency.abstract.LiveModelFragment
import edu.utah.cs4530.emergency.activity.TutorialActivity
import edu.utah.cs4530.emergency.dao.UserDAO
import edu.utah.cs4530.emergency.extension.getLogger
import edu.utah.cs4530.emergency.extension.showMessageBox
import edu.utah.cs4530.emergency.global.Location
import edu.utah.cs4530.emergency.repository.DeviceRepository
import edu.utah.cs4530.emergency.repository.UserRepository
import edu.utah.cs4530.emergency.service.CloudFunctions
import edu.utah.cs4530.emergency.util.DeviceInfo
import edu.utah.cs4530.emergency.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : LiveModelFragment<HomeViewModel>(HomeViewModel::class, R.layout.fragment_home), OnMapReadyCallback, PermissionListener {

    private val logger by getLogger()

    private var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationRequest = LocationRequest()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(1000)
        .setFastestInterval(500)

    override fun onCreateViewModel(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
    {
        root.mapView.apply {
            onCreate(savedInstanceState)
            onResume()
            MapsInitializer.initialize(activity!!.applicationContext)
            getMapAsync(this@HomeFragment)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

        root.btn_sos.setOnSwipeListener {
            if(!NetworkUtil.isNetworkAvailable())
            {
                context?.showMessageBox("Network Unavailable", "You are currently in offline. Please retry after get in online.")
                return@setOnSwipeListener
            }

            Handler().post {
                CloudFunctions.sendEmergencyMessage(
                    Location.lastLocation.latitude,
                    Location.lastLocation.longitude
                )
                    .continueWith {
                        if(it.isSuccessful) {
                            btn_sos.showResultIcon(true, true)
                        } else {
                            btn_sos.showResultIcon(false, true)
                            context?.showMessageBox("Error", "Fail to send emergency message due to below reason.\n\n${it.exception}")
                            logger.error("Fail to send emergency message", it.exception)
                        }
                    }
            }
        }

        //Check network status, and tell to user
        if(!NetworkUtil.isNetworkAvailable())
        {
            context?.showMessageBox("You are in offline now", "You are currently in offline now. You might not be able to use all features.")
        }


    }

    override fun onResume() {
        super.onResume()

        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()

        stopLocationUpdates()
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map.apply {
            uiSettings.apply {
                isMyLocationButtonEnabled = false
                isScrollGesturesEnabled = false
                isZoomGesturesEnabled = false
                isCompassEnabled = false
            }

            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(Location.lastLocation, 15F)
            moveCamera(cameraUpdate)
        }

        TedPermission.with(applicationContext)
            .setPermissionListener(this)
            .setDeniedMessage("If you reject permission, we cannot track your current location.")
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .check()
    }

    private fun startLocationUpdates() {
       mMap?.let {
           if (TedPermission.isGranted(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
           {
               fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
               it.isMyLocationEnabled = true
           }
           else
           {
               it.isMyLocationEnabled = false
           }
       }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onPermissionGranted() {
        logger.debug("Permission Granted")
        startLocationUpdates()
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        logger.debug("Permission Denied")
    }

    private val locationCallback: LocationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return

            Location.lastLocation = LatLng(locationResult.locations.last().latitude, locationResult.locations.last().longitude)
            val currentLatLng = Location.lastLocation
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15F)
            mMap?.moveCamera(cameraUpdate)
        }
    }

}