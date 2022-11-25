package com.bignerdranch.android.htiapp.registration

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
    }
}