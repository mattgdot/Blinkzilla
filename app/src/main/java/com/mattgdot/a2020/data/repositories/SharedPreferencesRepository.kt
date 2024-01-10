package com.mattgdot.a2020.data.repositories

import android.content.SharedPreferences
import com.mattgdot.a2020.utils.BREAK_DURATION
import com.mattgdot.a2020.utils.BREAK_ENABLED
import com.mattgdot.a2020.utils.COMPLETED_BREAKS
import com.mattgdot.a2020.utils.TOTAL_BREAKS
import com.mattgdot.a2020.utils.WORK_DURATION

class SharedPreferencesRepository(private val sharedPreferences: SharedPreferences) {

    fun setTotalBreaks(totalBreaks:Int){
        sharedPreferences.edit().apply {
            putInt(TOTAL_BREAKS, totalBreaks)
            apply()
        }
    }

    fun getTotalBreaks(): Int {
        return sharedPreferences.getInt(TOTAL_BREAKS, 0)
    }

    fun setCompletedBreaks(completedBreaks:Int){
        sharedPreferences.edit().apply {
            putInt(COMPLETED_BREAKS, completedBreaks)
            apply()
        }
    }

    fun getCompletedBreaks(): Int {
        return sharedPreferences.getInt(COMPLETED_BREAKS, 0)
    }

    fun setWorkDuration(workDuration:Int){
        sharedPreferences.edit().apply {
            putInt(WORK_DURATION, workDuration)
            apply()
        }
    }

    fun getWorkDuration(): Int {
        return sharedPreferences.getInt(WORK_DURATION, 4)
    }

    fun setBreakDuration(breakDuration:Int){
        sharedPreferences.edit().apply {
            putInt(BREAK_DURATION, breakDuration)
            apply()
        }
    }

    fun getBreakDuration(): Int {
        return sharedPreferences.getInt(BREAK_DURATION, 4)
    }

    fun setBreakEnabled(breakEnabled:Boolean){
        sharedPreferences.edit().apply {
            putBoolean(BREAK_ENABLED, breakEnabled)
            apply()
        }
    }

    fun getBreakEnabled(): Boolean {
        return sharedPreferences.getBoolean(BREAK_ENABLED, false)
    }
}