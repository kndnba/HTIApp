package com.bignerdranch.android.htiapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.htiapp.R
import com.bignerdranch.android.htiapp.databinding.ActivityRegisterBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegisterBinding
    val APP_PREFERENCES: String = "mysettings"
    val APP_PREFERENCES_PHONE_NUMBER: String = "phone_number"
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        var phoneNumber = mBinding.enterPhoneNumber.text.toString()
        var editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(APP_PREFERENCES_PHONE_NUMBER, phoneNumber)
        editor.apply()
        if(phoneNumber.length>0){
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        } else{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
