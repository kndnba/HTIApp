package com.bignerdranch.android.htiapp.di

import com.bignerdranch.android.htiapp.Interface.RetrofitServices
import com.bignerdranch.android.htiapp.network.NetworkRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ActivityComponent::class)
class AppModule {

    companion object {
        private const val NETWORK_TIMEOUT = 30L
        private const val BASE_URL = "https://eqmanager3limbs.herokuapp.com"
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        with(OkHttpClient.Builder()) {
            connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            addNetworkInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            build()
        }

    @Provides
    fun provideNetworkApi(gson: Gson, client: OkHttpClient): RetrofitServices =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()
            .create(RetrofitServices::class.java)

    @Provides
    fun provideNetworkRepository(api: RetrofitServices) = NetworkRepository(api)
}