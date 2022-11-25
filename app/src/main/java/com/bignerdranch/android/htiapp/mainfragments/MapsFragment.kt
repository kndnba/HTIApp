package com.bignerdranch.android.htiapp.mainfragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bignerdranch.android.htiapp.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest

class MapsFragment : Fragment(), GoogleMap.OnPoiClickListener {

    private val callback = OnMapReadyCallback { googleMap ->
        val yakutsk = LatLng(62.03, 129.67)

        googleMap.addMarker(MarkerOptions()
            .position(yakutsk).title("Marker in Yakutsk")
            .snippet("It's cold!"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(yakutsk))
        googleMap.setMinZoomPreference(12.0F)
        googleMap.setOnPoiClickListener(this@MapsFragment)
        googleMap.setOnMapClickListener {
            val markerOptions = MarkerOptions()
            markerOptions.position(it)
            googleMap.clear()
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(it))
            googleMap.addMarker(markerOptions)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onPoiClick(p0: PointOfInterest) {

    }
}