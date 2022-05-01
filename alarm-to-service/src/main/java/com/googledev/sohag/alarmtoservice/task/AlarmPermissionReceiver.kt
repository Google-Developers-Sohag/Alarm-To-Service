package com.googledev.sohag.alarmtoservice.task

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.googledev.sohag.alarmtoservice.core.util.Logger.getLogger
import com.googledev.sohag.alarmtoservice.core.util.TaskManager
import com.googledev.sohag.alarmtoservice.core.util.TaskManager.DATA_BUNDLE
import com.googledev.sohag.alarmtoservice.task.model.ExecutionModel
import java.util.logging.Level

@RequiresApi(31)
class AlarmPermissionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        getLogger().log(Level.INFO, "Received broadcast" + AlarmPermissionReceiver::class.java.name)
        val executionModel = intent.getParcelableExtra<Parcelable>(DATA_BUNDLE) as ExecutionModel?
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (alarmManager.canScheduleExactAlarms()) {
            for (task in executionModel!!.tasks) {
                val taskWrapper = Intent(context, task)
                taskWrapper.putExtra(DATA_BUNDLE, executionModel)
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    executionModel.triggerTime,
                    createPendingIntent(taskWrapper, context)
                )
            }
        }
    }

    private fun createPendingIntent(intent: Intent, context: Context): PendingIntent {
        return PendingIntent.getService(
            context,
            TaskManager.ALARM_REQ_CODE,
            intent,
            0
        )
    }
}