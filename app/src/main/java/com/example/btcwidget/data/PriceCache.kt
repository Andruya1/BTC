package com.example.btcwidget.data

import android.content.Context

object PriceCache {
    private const val FILE = "btc_cache"
    private const val KEY_PRICE = "last_price"

    fun setLastPrice(ctx: Context, price: Int) {
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            .edit().putInt(KEY_PRICE, price).apply()
    }
    fun getLastPrice(ctx: Context): Int? {
        val v = ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE).getInt(KEY_PRICE, Int.MIN_VALUE)
        return if (v == Int.MIN_VALUE) null else v
    }
}
