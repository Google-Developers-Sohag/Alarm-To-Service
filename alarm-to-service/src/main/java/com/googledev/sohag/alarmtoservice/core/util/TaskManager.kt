package com.googledev.sohag.alarmtoservice.core.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.googledev.sohag.alarmtoservice.task.AlarmPermissionReceiver
import com.googledev.sohag.alarmtoservice.task.TaskConnection
import com.googledev.sohag.alarmtoservice.task.model.ExecutionModel
import java.util.logging.Level

/**
 * Schedules a [TaskManager] to run a non-ui android service.
 *
 * @author pavl_g.
 */
object TaskManager {

    const val DATA_BUNDLE = "Executor-Bundle"
    const val ALARM_REQ_CODE = 90
    private const val ACTION_SEND_TASK_DATA = "com.sendaction.sendtask"
    const val TASK_DATA = "TASK-DATA"

    fun scheduleOnce(data: ExecutionModel, context: Context) {
        data.isRepetitiveEvent = false
        start(data, context)
        Logger.getLogger().log(
            Level.INFO,
            "Scheduled a work thread task wrapper for once with data executionModel object $data"
        )
    }


    fun scheduleRepetitively(data: ExecutionModel, context: Context) {
        data.isRepetitiveEvent = true
        start(data, context)
        Logger.getLogger().log(
            Level.INFO,
            "Scheduled a work thread task wrapper periodically with data executionModel object $data"
        )
    }

    private fun start(executionModel: ExecutionModel, context: Context) {

        // injects data to the executor runtime
        for (task in executionModel.tasks) {
            // register a broadcast receiver for ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED


            val taskWrapper = Intent(context, task)
            taskWrapper.putExtra(DATA_BUNDLE, executionModel)
            // binds the service to a starting intent when started
            context.bindService(taskWrapper, TaskConnection(), 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (executionModel.isOfIdleEvent && executionModel.isOfLowBatteryEvent) {
                when {
                    Build.VERSION.SDK_INT >= 31 -> {
                        val intent = Intent(ACTION_SEND_TASK_DATA)
                        intent.putExtra(DATA_BUNDLE, executionModel)
                        context.sendBroadcast(intent)

                        val intentFilter = IntentFilter(AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED)
                        context.registerReceiver(AlarmPermissionReceiver(), intentFilter, android.Manifest.permission.SCHEDULE_EXACT_ALARM, null)

                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            executionModel.triggerTime,
                            createPendingIntent(taskWrapper, context)
                        )
                    }
                    else -> {
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            executionModel.triggerTime,
                            createPendingIntent(taskWrapper, context)
                        )
                    }
                }
            } else {
                alarmManager[AlarmManager.RTC_WAKEUP, executionModel.triggerTime] =
                    createPendingIntent(taskWrapper, context)
            }
        }
    }
    private fun createPendingIntent(intent: Intent, context: Context): PendingIntent? {
        return PendingIntent.getService(
            context,
            ALARM_REQ_CODE,
            intent,
            0
        )
    }


}
