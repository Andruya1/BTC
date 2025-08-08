package com.example.btcwidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews
import com.example.btcwidget.R
import com.example.btcwidget.data.PriceCache
import com.example.btcwidget.receivers.RefreshReceiver
import com.example.btcwidget.util.Prefs

class BtcWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        appWidgetIds.forEach { id ->
            updateViews(context, id)
        }
    }

    companion object {
        fun updateAll(context: Context) {
            val mgr = AppWidgetManager.getInstance(context)
            val ids = mgr.getAppWidgetIds(ComponentName(context, BtcWidgetProvider::class.java))
            ids.forEach { updateViews(context, it) }
        }

        private fun updateViews(context: Context, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_btc)

            // Colors from prefs
            val color = Prefs.getColor(context)
            views.setTextColor(R.id.tvSymbol, color)
            views.setTextColor(R.id.tvPrice, color)

            val price = PriceCache.getLastPrice(context)
            val priceText = if (price != null) "$" + price.toString() else "—"
            views.setTextViewText(R.id.tvPrice, priceText)

            // Tap to refresh
            val intent = Intent(context, RefreshReceiver::class.java).apply {
                action = "com.example.btcwidget.ACTION_REFRESH"
            }
            val pi = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.root, pi)

            val mgr = AppWidgetManager.getInstance(context)
            mgr.updateAppWidget(appWidgetId, views)
        }
    }
}
