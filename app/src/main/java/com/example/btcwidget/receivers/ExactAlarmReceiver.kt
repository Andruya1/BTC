package com.example.btcwidget.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.btcwidget.schedule.Scheduler

class ExactAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Trigger immediate refresh and schedule next tick
        Scheduler.kickNow(context)
        Scheduler.scheduleExactNext(context)
    }
}
