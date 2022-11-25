package com.bignerdranch.android.htiapp.activities

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.bignerdranch.android.htiapp.R
import com.bignerdranch.android.htiapp.databinding.ActivityRegisterBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        mBinding.alreadyRegisteredLetsLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}