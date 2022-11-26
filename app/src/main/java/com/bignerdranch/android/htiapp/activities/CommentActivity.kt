package com.bignerdranch.android.htiapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bignerdranch.android.htiapp.common.ARG_BUNDLE
import com.bignerdranch.android.htiapp.common.ARG_LATITUDE
import com.bignerdranch.android.htiapp.databinding.ActivityCommentBinding
import com.bignerdranch.android.htiapp.network.NetworkRepository
import com.bignerdranch.android.htiapp.network.entities.Marker
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class CommentActivity : AppCompatActivity() {

    @Inject
    lateinit var networkRepository: NetworkRepository

    lateinit var binding: ActivityCommentBinding

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val latitude: String = (intent.getBundleExtra(ARG_BUNDLE)?.get(ARG_LATITUDE)) as String

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.cashCategory.setOnClickListener {
            sendComment("Берет только наличные", latitude)
        }

        binding.perevodCategory.setOnClickListener {
            sendComment("Просит перевести", latitude)
        }

        binding.noReceiptCategory.setOnClickListener {
            sendComment("Не дает чеков", latitude)
        }

        binding.noTerminalCategory.setOnClickListener {
            sendComment("Нет терминала", latitude)
        }
    }

    private fun sendComment(text: String, latitude: String) {
        compositeDisposable.add(
            networkRepository.addComment(Marker(xCoordinate = latitude, comments = text))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoader(true) }
                .doFinally { showLoader(false) }
                .subscribe({
                    if (it.status == "ok") {
                        onSuccess()
                    }
                }, {
                        onError()
                })
        )
    }

    private fun showLoader(show: Boolean) {
        binding.loaderView.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun onSuccess() {
        Toast.makeText(this, "Комментарий добавлен", Toast.LENGTH_SHORT).show()
    }

    private fun onError() {
        Toast.makeText(this, "Internet issues", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}