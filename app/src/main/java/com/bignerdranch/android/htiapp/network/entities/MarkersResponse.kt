package com.bignerdranch.android.htiapp.network.entities

import com.google.gson.annotations.SerializedName

data class MarkersResponse(
    @SerializedName("markers") val markers: List<Marker>?
)
