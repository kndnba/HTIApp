package com.bignerdranch.android.htiapp.network

import com.bignerdranch.android.htiapp.network.entities.Marker
import com.bignerdranch.android.htiapp.network.entities.Response
import io.reactivex.Single
import javax.inject.Inject
import com.google.android.gms.maps.model.Marker as GoogleMarker


class NetworkRepository @Inject constructor(
    private val api: RetrofitServices
) {

    fun register(phone: String): Single<Response> = api.register(
        hashMapOf(
            "phone" to phone
        )
    )

    fun authCode(code: String): Single<Response> = api.checkCode(
        hashMapOf(
            "code" to code
        )
    )

    fun getMarkers(): Single<List<Marker>> = api.getMarkers()

    fun addMarker(googleMarker: GoogleMarker): Single<Response> = api.addMarker(
        Marker(
            xCoordinate = googleMarker.position.latitude.toString(),
            yCoordinate = googleMarker.position.longitude.toString()
        )
    )
}