package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class StatisticsActivity : AppCompatActivity() {

    private lateinit var dbHandler: StepsDatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        dbHandler = StepsDatabaseHandler(this)

        val statsTextView: TextView = findViewById(R.id.statsTextView)

        val stepsStatistics = getStepsStatistics()

        statsTextView.text = stepsStatistics

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_view_stats -> {
                    true
                }
                R.id.nav_today -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        bottomNav.selectedItemId = R.id.nav_view_stats
    }

    private fun getStepsStatistics(): String {
        val statisticsList = dbHandler.getAllSteps()
        if (statisticsList.isEmpty()) {
            return "Немає статистики."
        }

        val stringBuilder = StringBuilder()
        for (stat in statisticsList) {
            stringBuilder.append("Дата: ${stat.first}, Кроки: ${stat.second}\n")
        }

        return stringBuilder.toString()
    }

}
