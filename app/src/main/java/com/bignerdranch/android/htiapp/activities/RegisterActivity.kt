package com.bignerdranch.android.htiapp.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.htiapp.Interface.RetrofitServices
import com.bignerdranch.android.htiapp.Model.RequestModel
import com.bignerdranch.android.htiapp.Retrofit.RetrofitClient
import com.bignerdranch.android.htiapp.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var retrofitClient: RetrofitServices


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        retrofitClient = RetrofitClient.getClient()
        mBinding.register.setOnClickListener {
            register()
        }
        mBinding.submitCode.setOnClickListener {
//            if (чето чето) openApp()
        }
        mBinding.alreadyRegistered.setOnClickListener {

        }
    }

    fun ping() {
        retrofitClient.ping().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Toast.makeText(this@RegisterActivity, response.message(), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println("FAIL")
            }

        })
    }
    fun register() {
        val requestModel = RequestModel(mBinding.email.text.toString())
        retrofitClient.register(requestModel = requestModel).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    mBinding.email.visibility = View.GONE
                    mBinding.enterCode.visibility = View.VISIBLE
                    mBinding.register.visibility = View.GONE
                    mBinding.registerTextEnterCode.visibility = View.VISIBLE
                    mBinding.submitCode.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("FAIL")
            }
        })
    }
    fun openApp(){
//        туох эрэ хаппыт
    }
}