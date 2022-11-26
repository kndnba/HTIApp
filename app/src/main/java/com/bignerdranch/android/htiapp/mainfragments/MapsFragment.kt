package com.bignerdranch.android.htiapp.mainfragments

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bignerdranch.android.htiapp.R
import com.bignerdranch.android.htiapp.network.NetworkRepository
import com.bignerdranch.android.htiapp.network.entities.Marker
import com.bignerdranch.android.htiapp.utils.BitmapUtil

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import com.google.android.gms.maps.model.Marker as GoogleMarker

@AndroidEntryPoint
class MapsFragment : Fragment(), GoogleMap.OnPoiClickListener, GoogleMap.OnMarkerClickListener {

    @Inject
    lateinit var networkRepository: NetworkRepository

    private val compositeDisposable = CompositeDisposable()

    private var map: GoogleMap? = null
    private var lastPoint: GoogleMarker? = null

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val yakutsk = LatLng(62.03, 129.67)

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(yakutsk))
        googleMap.setMinZoomPreference(12.0F)
        googleMap.setOnPoiClickListener(this@MapsFragment)
        googleMap.setOnMarkerClickListener(this)
        googleMap.setOnMapClickListener {
            val markerOptions = MarkerOptions()
            markerOptions.position(it)
            addMarker(markerOptions)
            showDialog()
        }

        getMarkers()
    }

    override fun onMarkerClick(marker: GoogleMarker): Boolean {
        map?.animateCamera(CameraUpdateFactory.newLatLng(LatLng(marker.position.latitude, marker.position.longitude)))
        return true
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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun showDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialog = dialogBuilder.setMessage("Добавить точку?")
            .setTitle("Добавление точки")
            .setCancelable(false)
            .setPositiveButton("ДА!") { _, _ -> saveMarker() }
            .setNegativeButton("Нет") { dialog, _ ->
                lastPoint?.remove()
                dialog.cancel()
            }
            .create()

        dialog.show()
    }

    private fun saveMarker() {
        lastPoint?.let {
            compositeDisposable.add(
                networkRepository.addMarker(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { showToast("Failed to save marker") }
                    .subscribe()
            )
        }
    }

    private fun getMarkers() {
        compositeDisposable.add(
            networkRepository.getMarkers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showMarkers(it)
            }, {
                showToast("Failed to load markers")
            })
        )
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun showMarkers(markers: List<Marker>) {
        if (markers.isNotEmpty()) {
            map?.let {
                for (marker in markers) {
                    val latitude = marker.xCoordinate?.toDouble() ?: 0.0
                    val longitude = marker.yCoordinate?.toDouble() ?: 0.0
                    val position = LatLng(latitude, longitude)
                    val options = MarkerOptions().apply {
                        this.position(position)
                        this.title(marker.comments)
                    }
                    addMarker(options)
                }
            }
        }
    }

    private fun addMarker(markerOptions: MarkerOptions) {
        markerOptions.icon(BitmapUtil.bitmapDestructorFromDrawable(requireContext(), R.drawable.ic_marker))
        lastPoint = map?.addMarker(markerOptions)
    }
}