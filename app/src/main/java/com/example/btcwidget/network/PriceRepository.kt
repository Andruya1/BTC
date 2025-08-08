package com.example.btcwidget.network

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class PriceRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    /**
     * Fetches BTC price in USD from CoinGecko without API key.
     * Returns an integer (no decimals), or throws.
     */
    fun fetchBtcUsd(): Int {
        val req = Request.Builder()
            .url("https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd")
            .get()
            .build()
        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) error("HTTP ${'$'}{resp.code}")
            val body = resp.body?.string() ?: error("Empty body")
            val usd = JSONObject(body).getJSONObject("bitcoin").getDouble("usd")
            return usd.roundToInt()
        }
    }
}
