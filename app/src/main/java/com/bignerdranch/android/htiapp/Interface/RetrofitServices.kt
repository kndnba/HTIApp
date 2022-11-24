package com.bignerdranch.android.htiapp.Interface

import com.bignerdranch.android.htiapp.Model.RequestModel
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST



interface RetrofitServices {

    @GET("/ping")
    fun ping(): Call<String>

    @POST("/register")
    fun register(@Body requestModel: RequestModel): Call<String>

    @POST("/login")
    fun login(): Call<String>

    @POST("/authcode")
    fun checkCode(): Call<String>
}


