package com.example.btcwidget.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.btcwidget.workers.PriceUpdateWorker

class RefreshReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val req = OneTimeWorkRequestBuilder<PriceUpdateWorker>().build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "manual_refresh",
            ExistingWorkPolicy.REPLACE,
            req
        )
    }
}
