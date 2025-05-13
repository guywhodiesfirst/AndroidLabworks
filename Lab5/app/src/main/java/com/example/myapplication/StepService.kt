package com.example.myapplication

import android.app.*
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import java.text.SimpleDateFormat
import java.util.*

class StepService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var stepsAtStart = 0f
    private var initialized = false
    private lateinit var dbHandler: StepsDatabaseHandler

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, createNotification())
        } else {
            val notification = createNotification()
            startForeground(1, notification)
        }

        dbHandler = StepsDatabaseHandler(this)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)

        val prefs = getSharedPreferences("step_prefs", MODE_PRIVATE)
        stepsAtStart = prefs.getFloat("steps_start", 0f)
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        if (prefs.getString("last_date", "") != today) {
            stepsAtStart = 0f
            prefs.edit().putString("last_date", today).apply()
        }
    }


    override fun onSensorChanged(event: SensorEvent) {
        val totalSteps = event.values[0]
        if (!initialized) {
            stepsAtStart = totalSteps
            getSharedPreferences("step_prefs", MODE_PRIVATE)
                .edit().putFloat("steps_start", stepsAtStart).apply()
            initialized = true
        }

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentSteps = (totalSteps - stepsAtStart).toInt()

        dbHandler.saveSteps(today, currentSteps)

        val intent = Intent("com.example.myapplication.STEPS_UPDATED")
        intent.putExtra("steps", currentSteps)
        sendBroadcast(intent)
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "step_channel", "Step Counter", NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For API 26 and above, create a notification channel
            val serviceChannel = NotificationChannel(
                "step_channel", "Step Counter", NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)

            // Notification for API 26 and above
            return Notification.Builder(this, "step_channel")
                .setContentTitle("Крокомір")
                .setContentText("Підрахунок кроків активний")
                .setSmallIcon(R.drawable.ic_walk)
                .setContentIntent(pendingIntent)
                .build()
        } else {
            // For API 24 and below, no need for a notification channel
            return Notification.Builder(this)
                .setContentTitle("Крокомір")
                .setContentText("Підрахунок кроків активний")
                .setSmallIcon(R.drawable.ic_walk)
                .setContentIntent(pendingIntent)
                .build()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}
