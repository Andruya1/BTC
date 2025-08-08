package com.example.btcwidget.util

import android.content.Context
import android.graphics.Color

object Prefs {
    private const val FILE = "btc_prefs"
    private const val KEY_INTERVAL_MIN = "interval_min"
    private const val KEY_COLOR = "color"
    private const val KEY_HIGH_FREQ = "high_freq"

    fun setIntervalMinutes(ctx: Context, minutes: Int) {
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit().putInt(KEY_INTERVAL_MIN, minutes).apply()
    }
    fun getIntervalMinutes(ctx: Context): Int =
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE).getInt(KEY_INTERVAL_MIN, 15)

    fun setColor(ctx: Context, color: Int) {
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit().putInt(KEY_COLOR, color).apply()
    }
    fun getColor(ctx: Context): Int =
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE).getInt(KEY_COLOR, Color.WHITE)

    fun setHighFreq(ctx: Context, enabled: Boolean) {
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_HIGH_FREQ, enabled).apply()
    }
    fun isHighFreq(ctx: Context): Boolean =
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE).getBoolean(KEY_HIGH_FREQ, false)
}
