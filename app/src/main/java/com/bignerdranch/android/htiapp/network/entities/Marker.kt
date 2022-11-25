package com.bignerdranch.android.htiapp.network.entities

import com.google.gson.annotations.SerializedName

data class Marker(
    @SerializedName("x_Coordinate") val xCoordinate: String? = "",
    @SerializedName("y_Coordinate") val yCoordinate: String? = "",
    @SerializedName("comments") val comments: String? = "",
    @SerializedName("plusCount") val plusCount: Int? = 0,
    @SerializedName("minusCount") val minusCount: Int? = 0,
    @SerializedName("approved") val approved: Boolean? = false
)