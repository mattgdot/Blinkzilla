package com.mattgdot.a2020

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.mattgdot.a2020.data.repositories.SharedPreferencesRepository
import com.mattgdot.a2020.utils.CHANNEL_ID
import com.mattgdot.a2020.utils.SHARED_PREFERENCES


class BreakService : Service() {
    private lateinit var alarmReceiver: BroadcastReceiver
    private lateinit var repository: SharedPreferencesRepository

    private lateinit var timer: CountDownTimer
    private lateinit var notificationManager: NotificationManager

    private var breakDuration = 20000L
    private var workDuration = 1200000L
    private var remainingMillis = 0L

    companion object{
        val NOTIFICATION_ID = 1234
    }

    override fun onCreate() {
        super.onCreate()

        repository = SharedPreferencesRepository(
            application.getSharedPreferences(
                SHARED_PREFERENCES,
                Context.MODE_PRIVATE
            )
        )

        breakDuration = repository.getBreakDuration().toLong() * 5 * 1000
        workDuration = repository.getWorkDuration().toLong() * 5 * 60000

        repository.setBreakEnabled(true)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        alarmReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    Intent.ACTION_SCREEN_OFF -> stopTimer()
                    Intent.ACTION_USER_PRESENT -> startTimer(workDuration)
                    Intent.ACTION_SHUTDOWN -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            stopForeground(STOP_FOREGROUND_DETACH)
                        } else {
                            stopSelf()
                        }
                    }
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val activityIntent = Intent(this, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)


        val pendingIntent = PendingIntent.getActivity(
            this, 0, activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_eye)
            .setTicker(getString(R.string.notification_title))
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_content))
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_USER_PRESENT)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        intentFilter.addAction(Intent.ACTION_SHUTDOWN)
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED)

        startTimer(workDuration)

        registerReceiver(alarmReceiver, intentFilter)

        startForeground(NOTIFICATION_ID, notification)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? = null


    override fun onDestroy() {
        super.onDestroy()
        repository.setBreakEnabled(false)
        stopTimer()
        unregisterReceiver(alarmReceiver)
    }

    private fun startTimer(endOfTimerInMillis: Long) {
        timer = object : CountDownTimer(endOfTimerInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingMillis = millisUntilFinished
            }
            override fun onFinish() {
                val restIntent = Intent(this@BreakService, BreakActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)

                startActivity(restIntent)
                startTimer(workDuration + breakDuration)
            }
        }
        timer.start()
    }

    private fun stopTimer() {
        try{
            timer.cancel()
        } catch (e:Exception){

        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 27) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}