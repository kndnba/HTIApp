package com.bignerdranch.android.htiapp.network

import com.bignerdranch.android.htiapp.network.entities.Marker
import com.bignerdranch.android.htiapp.network.entities.MarkersResponse
import com.bignerdranch.android.htiapp.network.entities.Response
import io.reactivex.Single
import javax.inject.Inject

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
}