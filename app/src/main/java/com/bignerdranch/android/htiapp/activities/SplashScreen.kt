package com.bignerdranch.android.htiapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.htiapp.common.APP_PREFERENCES_PHONE_NUMBER
import com.bignerdranch.android.htiapp.databinding.ActivityRegisterBinding
import com.bignerdranch.android.htiapp.databinding.ActivitySplashScreenBinding
import com.bignerdranch.android.htiapp.utils.getSharedPrefs

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    lateinit var mBinding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val intent = if (isAlreadyLoggedIn()) {
            Intent(this, MapsActivity::class.java)
        } else {
            Intent(this, RegisterActivity::class.java)
        }

        startActivity(intent)
    }

    private fun isAlreadyLoggedIn(): Boolean {
        val sharedPreferences = applicationContext.getSharedPrefs()
        return sharedPreferences.contains(APP_PREFERENCES_PHONE_NUMBER)
    }
}