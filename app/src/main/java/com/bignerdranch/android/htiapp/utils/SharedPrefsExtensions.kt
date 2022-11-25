package com.bignerdranch.android.htiapp.utils

import android.content.Context
import com.bignerdranch.android.htiapp.common.APP_PREFERENCES

fun Context.getSharedPrefs() = this.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
