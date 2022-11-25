package com.bignerdranch.android.htiapp.network

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
}


