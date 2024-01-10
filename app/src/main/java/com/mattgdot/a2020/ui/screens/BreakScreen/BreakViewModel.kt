package com.mattgdot.a2020.ui.screens.BreakScreen


import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.mattgdot.a2020.BreakService
import com.mattgdot.a2020.R
import com.mattgdot.a2020.data.repositories.SharedPreferencesRepository
import com.mattgdot.a2020.utils.SHARED_PREFERENCES
import java.util.Random


class BreakViewModel(application: Application):AndroidViewModel(application) {
    lateinit var timer: CountDownTimer

    private val repository = SharedPreferencesRepository(
        application.getSharedPreferences(
            SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    )

    var breakDuration by mutableIntStateOf(0)
    var progress by mutableFloatStateOf(0f)
    var timerFinished by mutableStateOf(false)
    var message by mutableStateOf("")

    private val totalBreaks: Int
    private val completedBreaks: Int
    private val skippedBreaks: Int

    fun startTimer() {
        repository.setTotalBreaks(totalBreaks + 1)

        timer = object : CountDownTimer(breakDuration.toLong(), 10) {
            override fun onTick(millisUntilFinished: Long) {
                val millisElapsed = breakDuration - millisUntilFinished

                progress = millisElapsed.toFloat() / breakDuration.toFloat()
            }

            override fun onFinish() {
                repository.setCompletedBreaks(completedBreaks + 1)
                timerFinished = true
            }
        }

        timer.start()
    }

    init {
        totalBreaks = repository.getTotalBreaks()
        completedBreaks = repository.getCompletedBreaks()
        skippedBreaks = totalBreaks - completedBreaks
        breakDuration = repository.getBreakDuration() * 5 * 1000

        val array: Array<String> = application.resources.getStringArray(R.array.rest_messages)
        message = array[Random().nextInt(array.size)]

    }
}