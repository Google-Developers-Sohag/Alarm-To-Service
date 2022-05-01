package com.googledev.sohag.exampleapp

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.googledev.sohag.alarmtoservice.core.util.QuickSetup
import com.googledev.sohag.alarmtoservice.core.util.TaskManager
import java.util.*

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= 31) {
            requestPermissions(arrayOf(android.Manifest.permission.SCHEDULE_EXACT_ALARM, android.Manifest.permission.SET_ALARM),
                TaskManager.ALARM_REQ_CODE)
        }

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 1)
        calendar.set(Calendar.MINUTE, 16)
        QuickSetup.startRepetitiveTask(TestTask::class.java, calendar.timeInMillis, applicationContext)
    }
}