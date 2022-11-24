package com.bignerdranch.android.htiapp.Retrofit

import com.bignerdranch.android.htiapp.Interface.RetrofitServices
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://eqmanager3limbs.herokuapp.com"
    private var retrofit : RetrofitServices? = null

    fun getClient(): RetrofitServices {
        if (retrofit == null) {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitServices::class.java)
        }
        return requireNotNull(retrofit)
    }
}