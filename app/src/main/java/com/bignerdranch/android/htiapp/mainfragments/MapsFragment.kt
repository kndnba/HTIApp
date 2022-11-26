package com.bignerdranch.android.htiapp.mainfragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bignerdranch.android.htiapp.R
import com.bignerdranch.android.htiapp.activities.CommentActivity
import com.bignerdranch.android.htiapp.common.ARG_BUNDLE
import com.bignerdranch.android.htiapp.common.ARG_LATITUDE
import com.bignerdranch.android.htiapp.databinding.FragmentMapsBinding
import com.bignerdranch.android.htiapp.network.NetworkRepository
import com.bignerdranch.android.htiapp.network.entities.Marker
import com.bignerdranch.android.htiapp.utils.BitmapUtil

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.CancelableCallback
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
    lateinit var binding: FragmentMapsBinding

    private var map: GoogleMap? = null
    private var lastPoint: GoogleMarker? = null

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val yakutsk = LatLng(62.03, 129.67)

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(yakutsk))
        googleMap.setMinZoomPreference(12.0F)
        googleMap.setOnPoiClickListener(this@MapsFragment)
        googleMap.setOnMarkerClickListener(this)
        googleMap.setOnCameraMoveListener { showCommentButton(false) }
        googleMap.setOnMapClickListener {
            val markerOptions = MarkerOptions()
            markerOptions.position(it)
            addMarker(markerOptions)
            showCommentButton(false)
            showDialog()
        }

        getMarkers()
    }

    override fun onMarkerClick(marker: GoogleMarker): Boolean {
        map?.animateCamera(
            CameraUpdateFactory.newLatLng(LatLng(marker.position.latitude, marker.position.longitude)),
            500, object : CancelableCallback{
                override fun onCancel() { showCommentButton(false) }
                override fun onFinish() { showCommentButton(true) }
            }
        )
        binding.addCommentButton.setOnClickListener {
            val intent = Intent(requireContext(), CommentActivity::class.java)
            val bundle = Bundle()
            bundle.putString(ARG_LATITUDE, marker.position.latitude.toString())
            intent.putExtra(ARG_BUNDLE, bundle)
            startActivity(intent)
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater)
        return binding.root
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

    private fun showCommentButton(show: Boolean) {
        binding.addCommentButton.visibility = if (show) View.VISIBLE else View.GONE
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