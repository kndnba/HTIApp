package com.bignerdranch.android.htiapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.htiapp.R
import com.bignerdranch.android.htiapp.common.APP_PREFERENCES_PHONE_NUMBER
import com.bignerdranch.android.htiapp.databinding.ActivityRegisterBinding
import com.bignerdranch.android.htiapp.utils.getSharedPrefs

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)

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
