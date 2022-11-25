package com.bignerdranch.android.htiapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory


class BitmapUtil {

    companion object {

        fun bitmapDestructorFromDrawable(context: Context, resId: Int): BitmapDescriptor? {
            val vectorDrawable = ContextCompat.getDrawable(context, resId)

            vectorDrawable?.let {
                vectorDrawable.setBounds(
                    0,
                    0,
                    vectorDrawable.intrinsicWidth,
                    vectorDrawable.intrinsicHeight
                )

                val bitmap = Bitmap.createBitmap(
                    vectorDrawable.intrinsicWidth,
                    vectorDrawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )

                val canvas = Canvas(bitmap)
                vectorDrawable.draw(canvas)

                // after generating our bitmap we are returning our bitmap.

                // after generating our bitmap we are returning our bitmap.
                return BitmapDescriptorFactory.fromBitmap(bitmap)
            }

            return null
        }
    }
}