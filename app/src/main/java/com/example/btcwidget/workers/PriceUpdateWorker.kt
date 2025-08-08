package com.example.btcwidget.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.btcwidget.data.PriceCache
import com.example.btcwidget.network.PriceRepository
import com.example.btcwidget.widget.BtcWidgetProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PriceUpdateWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val price = PriceRepository().fetchBtcUsd()
            PriceCache.setLastPrice(applicationContext, price)
            BtcWidgetProvider.updateAll(applicationContext)
            Result.success()
        } catch (t: Throwable) {
            // Keep old price; signal retry with backoff
            Result.retry()
        }
    }
}
