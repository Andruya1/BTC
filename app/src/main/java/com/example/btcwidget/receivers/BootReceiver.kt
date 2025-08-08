package com.example.btcwidget.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.btcwidget.schedule.Scheduler

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Scheduler.schedule(context) // Reschedule periodic work / alarms
    }
}
