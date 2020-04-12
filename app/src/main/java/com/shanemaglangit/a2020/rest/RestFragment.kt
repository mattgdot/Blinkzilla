package com.shanemaglangit.a2020.rest


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.shanemaglangit.a2020.R
import com.shanemaglangit.a2020.databinding.FragmentRestBinding

class RestFragment : Fragment() {
    private lateinit var binding: FragmentRestBinding
    private lateinit var restViewModel: RestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rest, container, false)
        restViewModel = ViewModelProvider(
            this,
            RestViewModelFactory(activity!!.application)
        ).get(RestViewModel::class.java)

        // Load the ads
        binding.adBreak.loadAd(AdRequest.Builder().build())

        binding.lifecycleOwner = this
        binding.restViewModel = restViewModel

        restViewModel.startTimer()

        // Toggle the visibility of views once timer ends
        restViewModel.timeLeft.observe(viewLifecycleOwner, Observer {
            if (it == 0) {
                binding.textTimeleftCaption.visibility = View.GONE
                binding.progressTimeElapsed.visibility = View.GONE
                binding.textTimeleft.visibility = View.GONE
                binding.buttonEnd.visibility = View.VISIBLE
            }
        })

        // Stops the activity
        restViewModel.endActivity.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (Build.VERSION.SDK_INT >= 21) activity!!.finishAndRemoveTask()
                else activity!!.finishAffinity()
            }
        })

        return binding.root
    }
}
