package com.example.btcwidget.schedule

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.*
import com.example.btcwidget.receivers.ExactAlarmReceiver
import com.example.btcwidget.util.Prefs
import com.example.btcwidget.workers.PriceUpdateWorker
import java.util.concurrent.TimeUnit

object Scheduler {

    fun schedule(context: Context) {
        if (Prefs.isHighFreq(context)) {
            scheduleExactNext(context)
        } else {
            scheduleWork(context)
        }
    }

    fun kickNow(context: Context) {
        val req = OneTimeWorkRequestBuilder<PriceUpdateWorker>().build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork("kick_now", ExistingWorkPolicy.REPLACE, req)
    }

    private fun scheduleWork(context: Context) {
        val minutes = Prefs.getIntervalMinutes(context).coerceAtLeast(15) // WorkManager min period
        val req = PeriodicWorkRequestBuilder<PriceUpdateWorker>(minutes.toLong(), TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork("periodic_price", ExistingPeriodicWorkPolicy.UPDATE, req)
    }

    fun scheduleExactNext(context: Context) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ExactAlarmReceiver::class.java).apply {
            action = "com.example.btcwidget.ACTION_ALARM_TICK"
        }
        val pi = PendingIntent.getBroadcast(
            context, 1001, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val minutes = Prefs.getIntervalMinutes(context).coerceAtMost(10)
        val triggerAt = System.currentTimeMillis() + minutes * 60_000L
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi)
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pi)
        }
    }
}
