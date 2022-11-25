package com.bignerdranch.android.htiapp.network

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("status") val status: String
)