package com.shanemaglangit.a2020.setting

import android.Manifest
import android.animation.ValueAnimator
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.*

import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.shanemaglangit.a2020.R
import com.shanemaglangit.a2020.databinding.ActivityMainBinding
import com.shanemaglangit.a2020.setRatingText

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var requestOverlayPermissionLauncher: ActivityResultLauncher<Intent>


    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {

            AlertDialog
                .Builder(ContextThemeWrapper(this@SettingActivity, R.style.AppTheme_Dialog))
                .setTitle("Permission Required")
                .setMessage("App needs to appear on top in order to work.")
                .setCancelable(false)
                .setPositiveButton("Grant"
                ) { p0, p1 ->
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                    requestOverlayPermissionLauncher.launch(intent)
                }
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        settingViewModel = ViewModelProvider(
            this,
        )[SettingViewModel::class.java]

        binding.settingViewModel = settingViewModel
        binding.lifecycleOwner = this

        requestOverlayPermissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                checkOverlayPermission()
            }
        }

        settingViewModel.userRating.observe(this){
            binding.textRating.startTextAnimation(
                it, 2500, 1000, true
            )
        }
        settingViewModel.totalBreak.observe(this){
            binding.textTotal.startTextAnimation(
                it, 2500, 1000
            )
        }

        settingViewModel.completedBreak.observe(this){
            binding.textCompleted.startTextAnimation(
                it,
                1500,
                1000
            )
        }

        settingViewModel.skippedBreak.observe(this){
            binding.textSkipped.startTextAnimation(
                it,
                1500,
                1000
            )
        }

        // Set up liveData observers
        settingViewModel.duration.setFieldChangeObserver()
        settingViewModel.work.setFieldChangeObserver()
        settingViewModel.invalidFields.setInvalidFieldObserver()

        checkOverlayPermission()

        // Request for the necessary permissions
        requestPermissions()
    }

    override fun onResume() {
        super.onResume()
        settingViewModel.updateStats()
    }

    /**
     * Used to set a valueAnimator for the TextView
     * @param endValue value where the animation will stop
     * @param duration duration of the animation
     * @param delay time before the animation starts
     * @param isRating is the view a rating
     */
    private fun TextView.startTextAnimation(
        endValue: Int,
        duration: Long,
        delay: Long = 0,
        isRating: Boolean = false
    ) {
        val valueAnimator = ValueAnimator.ofInt(0, endValue)
        valueAnimator.addUpdateListener {
            if (isRating) setRatingText(this, valueAnimator.animatedValue.toString())
            else this.text = valueAnimator.animatedValue.toString()
        }
        valueAnimator.duration = duration
        valueAnimator.startDelay = delay
        valueAnimator.start()
    }

    /**
     * Used to set the observer for invalidField
     */
    private fun LiveData<Boolean>.setInvalidFieldObserver() {
        this.observe(this@SettingActivity, Observer {
            if (it) {
                AlertDialog
                    .Builder(ContextThemeWrapper(this@SettingActivity, R.style.AppTheme_Dialog))
                    .setTitle("Invalid fields")
                    .setMessage("Fields cannot be set to 0")
                    .setCancelable(true)
                    .setPositiveButton("Ok", null)
                    .setOnDismissListener { settingViewModel.invalidFieldsDialogComplete() }
                    .show()
            }
        })
    }

    /**
     * Used to set the observer for observing if the mutableLiveData changed
     */
    private fun MutableLiveData<Int>.setFieldChangeObserver() {
        this.observe(this@SettingActivity) {
            settingViewModel.fieldsChanged()
        }
    }

    /**
     * Used to request for the permissions needed for the app to run properly
     */
    private fun requestPermissions() {
        val missingPermissions = getMissingPermissions()

        if(missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missingPermissions, 1)
        }
    }

    /**
     * Used to get the list of permissions that needs to be requested
     */
    private fun getMissingPermissions() : Array<String> {
        val missingPermissions = arrayListOf<String>()

        if (Build.VERSION.SDK_INT >= 28 && !checkPermission(Manifest.permission.FOREGROUND_SERVICE))
            missingPermissions.add(Manifest.permission.FOREGROUND_SERVICE)
        if (!checkPermission(Manifest.permission.VIBRATE))
            missingPermissions.add(Manifest.permission.VIBRATE)
        if (!checkPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED))
            missingPermissions.add(Manifest.permission.RECEIVE_BOOT_COMPLETED)
        if (Build.VERSION.SDK_INT >= 33 && !checkPermission(Manifest.permission.POST_NOTIFICATIONS))
            missingPermissions.add(Manifest.permission.POST_NOTIFICATIONS)

        return missingPermissions.toTypedArray()
    }

    /**
     * Used to check if the permission is already granted
     */
    private fun Context.checkPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    /**
     * Close the app if the permissions are not granted
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 0) {
            finishAndRemoveTask()
        }
    }
}
