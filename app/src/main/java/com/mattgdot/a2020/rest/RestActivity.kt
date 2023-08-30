package com.mattgdot.a2020.rest

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mattgdot.a2020.R
import com.mattgdot.a2020.databinding.ActivityRestBinding

class RestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestBinding
    private lateinit var restViewModel: RestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}

        val analytics = Firebase.analytics

        binding = DataBindingUtil.setContentView(this, R.layout.activity_rest)

        restViewModel = ViewModelProvider(this)[RestViewModel::class.java]

        binding.restViewModel = restViewModel
        binding.lifecycleOwner = this

        // Load the ads
        binding.adBreak.loadAd(AdRequest.Builder().build())

        restViewModel.startTimer()

        // Toggle the visibility of views once timer ends
        restViewModel.timeLeft.observe(this) {
            if (it == 0) {
                binding.textTimeleftCaption.visibility = View.GONE
                binding.progressTimeElapsed.visibility = View.GONE
                binding.textTimeleft.visibility = View.GONE
                binding.buttonEnd.visibility = View.VISIBLE
            }
        }

        // Stops the activity
        restViewModel.endActivity.observe(this) {
            if (it) {
                finishAndRemoveTask()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        finishAndRemoveTask()
    }

    override fun onDestroy() {
        super.onDestroy()
        try{
            finishAndRemoveTask()
            restViewModel.timer.cancel()
        } catch (e:Exception){

        }
    }
}
