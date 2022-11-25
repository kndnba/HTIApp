package com.bignerdranch.android.htiapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.bignerdranch.android.htiapp.R
import com.bignerdranch.android.htiapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPoiClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this@MapsActivity, R.id.activity_main_nav_host_fragment)
        setupWithNavController(binding.bottomNavigationView, navController)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Yakutsk and move the camera
        val yakutsk = LatLng(62.03, 129.67)

        mMap.addMarker(MarkerOptions()
            .position(yakutsk).title("Marker in Yakutsk")
            .snippet("It's cold!"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(yakutsk))
        mMap.setMinZoomPreference(12.0F)
        mMap.setOnPoiClickListener(this)
        mMap.setOnMapClickListener {
            var markerOptions = MarkerOptions()
            markerOptions.position(it)
            googleMap.clear()
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(it))
            mMap.addMarker(markerOptions)
        }
    }

    override fun onPoiClick(poi: PointOfInterest) {
        
    }

}