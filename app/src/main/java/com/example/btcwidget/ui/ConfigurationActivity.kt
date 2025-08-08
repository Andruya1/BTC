package com.example.btcwidget.ui

import android.app.Activity
import android.app.AlarmManager
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.btcwidget.R
import com.example.btcwidget.databinding.ActivityConfigBinding
import com.example.btcwidget.schedule.Scheduler
import com.example.btcwidget.util.Prefs
import com.example.btcwidget.widget.BtcWidgetProvider

class ConfigurationActivity : Activity() {

    private lateinit var binding: ActivityConfigBinding
    private var appWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setResult(RESULT_CANCELED)

        // Parse widget ID
        appWidgetId = intent?.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish(); return
        }

        // Populate spinners
        val intervals = listOf(10, 15, 30, 60)
        binding.spInterval.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, intervals.map { "$it min" })
        val colors = listOf("Blanco", "Negro")
        binding.spColor.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colors)

        // Defaults
        val curInt = Prefs.getIntervalMinutes(this)
        binding.spInterval.setSelection(intervals.indexOf(curInt).coerceAtLeast(1)) // default 15
        val curColor = Prefs.getColor(this)
        binding.spColor.setSelection(if (curColor == Color.BLACK) 1 else 0)
        binding.cbHighFreq.isChecked = Prefs.isHighFreq(this)

        binding.btnSave.setOnClickListener {
            val minutes = intervals[binding.spInterval.selectedItemPosition]
            Prefs.setIntervalMinutes(this, minutes)

            val color = if (binding.spColor.selectedItemPosition == 1) Color.BLACK else Color.WHITE
            Prefs.setColor(this, color)

            val high = binding.cbHighFreq.isChecked
            Prefs.setHighFreq(this, high)

            if (high) {
                // Check exact alarm permission
                val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                if (!am.canScheduleExactAlarms()) {
                    Toast.makeText(this, "Debes permitir 'Alarmas y recordatorios' para 10 minutos.", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    startActivity(intent)
                }
            }

            // Schedule
            Scheduler.kickNow(this)
            Scheduler.schedule(this)

            // Update immediate
            BtcWidgetProvider.updateAll(this)

            val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            setResult(RESULT_OK, resultValue)
            finish()
        }
    }
}
