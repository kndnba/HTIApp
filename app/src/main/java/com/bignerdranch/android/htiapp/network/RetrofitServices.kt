package com.bignerdranch.android.htiapp.network

import com.bignerdranch.android.htiapp.network.entities.Marker
import com.bignerdranch.android.htiapp.network.entities.MarkersResponse
import com.bignerdranch.android.htiapp.network.entities.Response
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitServices {

    @GET("/ping")
    fun ping(): Call<String>

    @POST("/register")
    fun register(@Body params: HashMap<String, String>): Single<Response>

    @POST("/login")
    fun login(@Body params: HashMap<String, String>): Single<String>

    @POST("/authcode")
    fun checkCode(@Body params: HashMap<String, String>): Single<Response>

    @GET("/getmarkers")
    fun getMarkers(): Single<List<Marker>>

    @POST("/addmarker")
    fun addMarker(@Body marker: Marker): Single<Response>

    @POST("/addcomment")
    fun addComment(@Body marker: Marker): Single<Response>
}


