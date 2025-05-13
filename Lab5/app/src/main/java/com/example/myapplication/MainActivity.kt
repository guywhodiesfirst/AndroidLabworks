package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.os.Build
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var stepCountTextView: TextView

    private val stepsUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val steps = intent?.getIntExtra("steps", 0) ?: 0
            stepCountTextView.text = "Кроки: $steps"

            val sharedPref = context?.getSharedPreferences("StepData", MODE_PRIVATE)
            sharedPref?.edit()?.putInt("steps", steps)?.apply()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stepCountTextView = findViewById(R.id.stepCountTextView)

        val sharedPref = getSharedPreferences("StepData", MODE_PRIVATE)
        val steps = sharedPref.getInt("steps", 0)
        stepCountTextView.text = "Кроки: $steps"

        val serviceIntent = Intent(this, StepService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }

        val filter = IntentFilter("com.example.myapplication.STEPS_UPDATED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(stepsUpdateReceiver, filter, RECEIVER_NOT_EXPORTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(stepsUpdateReceiver, filter, RECEIVER_EXPORTED)
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_today -> {
                    true
                }
                R.id.nav_view_stats -> {
                    val intent = Intent(this, StatisticsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        bottomNav.selectedItemId = R.id.nav_today
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(stepsUpdateReceiver)
    }
}

