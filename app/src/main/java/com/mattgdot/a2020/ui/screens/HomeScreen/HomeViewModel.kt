package com.mattgdot.a2020.ui.screens.HomeScreen

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.mattgdot.a2020.BreakService
import com.mattgdot.a2020.R
import com.mattgdot.a2020.data.repositories.SharedPreferencesRepository
import com.mattgdot.a2020.utils.SHARED_PREFERENCES

class HomeViewModel(private val application: Application) : AndroidViewModel(application) {
    var totalBreaks by mutableIntStateOf(0)
    var totalCompleted by mutableIntStateOf(0)
    var userRating by mutableIntStateOf(0)
    var breakDuration by mutableFloatStateOf(0f)
    var workDuration by mutableFloatStateOf(0f)
    var breakEnabled by mutableStateOf(false)

    private val repository = SharedPreferencesRepository(
        application.getSharedPreferences(
            SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    )

    private fun enableBreaks() {
        if (Build.VERSION.SDK_INT >= 26) {
            getApplication<Application>().startForegroundService(
                Intent(
                    getApplication(),
                    BreakService::class.java
                )
            )
        } else {
            getApplication<Application>().startService(
                Intent(
                    getApplication(),
                    BreakService::class.java
                )
            )
        }
        Toast.makeText(application,application.getString(R.string.break_enabled),Toast.LENGTH_SHORT).show()
    }

    private fun disableBreaks() {
        getApplication<Application>().stopService(
            Intent(
                getApplication(),
                BreakService::class.java
            )
        )
        Toast.makeText(application,application.getString(R.string.break_stopped),Toast.LENGTH_SHORT).show()
    }

    fun setBreakEnabled() {
        breakEnabled = !breakEnabled
//        repository.setBreakEnabled(breakEnabled)

        if(breakEnabled) enableBreaks()
        else disableBreaks()
    }

    fun updateBreak(value:Float) {
        breakDuration = value
        repository.setBreakDuration(breakDuration.toInt())

        if(breakEnabled){
            setBreakEnabled()
        }
    }

    fun updateWork(value:Float) {
        workDuration = value
        repository.setWorkDuration(workDuration.toInt())

        if(breakEnabled){
            setBreakEnabled()
        }
    }

    fun setUserRating() {
        userRating = if (totalBreaks != 0) {
            (totalCompleted.toDouble() / totalBreaks.toDouble() * 100).toInt()
        } else 100
    }

    fun getData(){
        totalBreaks = repository.getTotalBreaks()
        totalCompleted = repository.getCompletedBreaks()
        breakDuration = repository.getBreakDuration().toFloat()
        workDuration = repository.getWorkDuration().toFloat()
        breakEnabled = repository.getBreakEnabled()
        setUserRating()
    }

    fun <T> Context.isServiceRunning(service: Class<T>): Boolean {
        return (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Integer.MAX_VALUE)
            .any { it -> it.service.className == service.name }
    }

    fun checkIfRunning(){
        repository.setBreakEnabled(application.isServiceRunning(BreakService::class.java))
    }

    init {
        getData()
        checkIfRunning()
    }
}