package com.bignerdranch.android.htiapp.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.htiapp.Interface.RetrofitServices
import com.bignerdranch.android.htiapp.Model.RequestModel
import com.bignerdranch.android.htiapp.R
import com.bignerdranch.android.htiapp.Retrofit.RetrofitClient
import com.bignerdranch.android.htiapp.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var retrofitClient: RetrofitServices
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        retrofitClient = RetrofitClient.getClient()


        mBinding.register.setOnClickListener {
            register()
        }

        mBinding.submitCode.setOnClickListener {
            authorisation()
        }
        mBinding.alreadyRegisteredLetsLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
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
        val requestModel = RequestModel(mBinding.enterPhoneNumber.text.toString())
        val params = hashMapOf(
            "phone" to mBinding.enterPhoneNumber.text.toString()
        )
        retrofitClient.register(params).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    mBinding.enterPhoneNumber.visibility = View.GONE
                    mBinding.enterCode.visibility = View.VISIBLE
                    mBinding.register.visibility = View.GONE
                    mBinding.registerTextEnterCode.visibility = View.VISIBLE
                    mBinding.submitCode.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@RegisterActivity, "Чето не то", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("FAIL")
            }
        })
    }
    fun authorisation() {
        val requestModel = RequestModel(mBinding.enterCode.text.toString())
        val params = hashMapOf(
            "authcode" to mBinding.enterCode.text.toString()
        )
        retrofitClient.register(params).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    sharedPreferences = getPreferences(MODE_PRIVATE)
                    var mEditor : SharedPreferences.Editor = sharedPreferences.edit()
                    mEditor.putBoolean("success", response.isSuccessful)
                    val intent = Intent(this@RegisterActivity, MapsActivity::class.java)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("FAIL")
            }
        })
    }
}