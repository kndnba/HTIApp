package com.bignerdranch.android.htiapp.registration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.htiapp.activities.MapsActivity
import com.bignerdranch.android.htiapp.common.APP_PREFERENCES_PHONE_NUMBER
import com.bignerdranch.android.htiapp.databinding.ActivityRegisterBinding
import com.bignerdranch.android.htiapp.network.NetworkRepository
import com.bignerdranch.android.htiapp.utils.getSharedPrefs
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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
            authorization()
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
        mBinding.registerLayout.visibility = View.GONE
        mBinding.codeLayout.visibility = View.VISIBLE
    }

    private fun doOnError() {
        Toast.makeText(this, "Internet issues", Toast.LENGTH_LONG).show()
    }

    private fun showLoader(show: Boolean) {
        if (show) {
            mBinding.loaderView.visibility = View.VISIBLE
        } else {
            mBinding.loaderView.visibility = View.GONE
        }
    }

    private fun authorization() {
        val code = mBinding.enterCode.text.trim().toString()

        compositeDisposable.add(networkRepository.authCode(code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoader(true) }
            .doFinally { showLoader(false) }
            .subscribe({
                doOnSuccessAuthCode()
            }, {
                doOnError()
            })
        )
    }

    private fun doOnSuccessAuthCode() {
        saveCredentials()

        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    private fun saveCredentials() {
        val prefs = applicationContext.getSharedPrefs()
        prefs.edit()
            .putString(APP_PREFERENCES_PHONE_NUMBER, "NO_PHONE_JUST_MOCK")
            .apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}