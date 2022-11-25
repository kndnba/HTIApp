package com.bignerdranch.android.htiapp.registration

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.DialogCompat
import androidx.fragment.app.DialogFragment
import com.bignerdranch.android.htiapp.Model.RequestModel
import com.bignerdranch.android.htiapp.activities.MapsActivity
import com.bignerdranch.android.htiapp.databinding.ActivityRegisterBinding
import com.bignerdranch.android.htiapp.network.NetworkRepository
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegisterBinding

    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var networkRepository: NetworkRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

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

    private fun register() {
        val phone = mBinding.enterPhoneNumber.text.trim().toString()

        compositeDisposable.add(
            networkRepository.register(phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoader(true) }
            .doFinally { showLoader(false) }
            .subscribe({
                doOnSuccess()
            }, {
                doOnError()
            })
        )
    }

    private fun doOnSuccess() {
        mBinding.enterPhoneNumber.visibility = View.GONE
        mBinding.enterCode.visibility = View.VISIBLE
        mBinding.register.visibility = View.GONE
        mBinding.registerTextEnterCode.visibility = View.VISIBLE
        mBinding.submitCode.visibility = View.VISIBLE
    }

    private fun doOnError() {

    }

    private fun showLoader(show: Boolean) {
        if (show) {
            mBinding.loaderView.visibility = View.VISIBLE
        } else {
            mBinding.loaderView.visibility = View.GONE
        }
    }

    private fun authorisation() {
        val requestModel = RequestModel(mBinding.enterCode.text.toString())
        val params = hashMapOf(
            "authcode" to mBinding.enterCode.text.toString()
        )
//        retrofitClient.register(params).enqueue(object : Callback<String>{
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                if(response.isSuccessful) {
//                    sharedPreferences = getPreferences(MODE_PRIVATE)
//                    var mEditor : SharedPreferences.Editor = sharedPreferences.edit()
//                    mEditor.putBoolean("success", response.isSuccessful)
//                    val intent = Intent(this@RegisterActivity, MapsActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                println("FAIL")
//            }
//        })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}