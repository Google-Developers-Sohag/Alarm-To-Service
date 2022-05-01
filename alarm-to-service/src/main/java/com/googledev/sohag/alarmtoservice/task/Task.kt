package com.googledev.sohag.alarmtoservice.task

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Parcelable
import com.googledev.sohag.alarmtoservice.core.throwable.SharedDataNotFoundException
import com.googledev.sohag.alarmtoservice.core.util.Logger
import com.googledev.sohag.alarmtoservice.core.util.TaskManager
import com.googledev.sohag.alarmtoservice.task.model.ExecutionModel
import com.googledev.sohag.alarmtoservice.task.mutex.Definition.DEST_MUTEX
import com.googledev.sohag.alarmtoservice.task.mutex.Definition.INIT_MUTEX
import com.googledev.sohag.alarmtoservice.task.mutex.Mutex
import com.googledev.sohag.alarmtoservice.task.mutex.Semaphore
import java.util.logging.Level

/**
 * Represents a non-ui background android task.
 *
 * @author pavl_g.
 */
abstract class Task : Service() {
    private lateinit var executionModel: ExecutionModel

    @Deprecated("Use Task#OnTaskCreated()")
    override fun onBind(intent: Intent): IBinder? {
        Logger.getLogger().log(Level.INFO, "Task bounded")
        val parcelable: Parcelable = intent.getParcelableExtra(TaskManager.DATA_BUNDLE)
            ?: throw SharedDataNotFoundException("Cannot find the Executor Shared data !")
        this.executionModel = parcelable as ExecutionModel

        waitForUnlock(executionModel.areSynchronousTasks(), INIT_MUTEX)
        lock(executionModel.areSynchronousTasks(), INIT_MUTEX)
        onTaskCreated(executionModel)
        unlock(executionModel.areSynchronousTasks(), INIT_MUTEX)

        stopSelf()
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Logger.getLogger().log(Level.INFO, "Task unbounded")
        return super.onUnbind(intent)
    }

    @Deprecated("Use Task#OnTaskCreated()")
    override fun onCreate() {
    }

    @Deprecated("Use Task#onTaskDestroyed()")
    override fun onDestroy() {
        waitForUnlock(executionModel.areSynchronousTasks(), DEST_MUTEX)
        lock(executionModel.areSynchronousTasks(), DEST_MUTEX)
        onTaskDestroyed(executionModel)
        unlock(executionModel.areSynchronousTasks(), DEST_MUTEX)

        Logger.getLogger().log(Level.INFO, "Task Destroyed")
        Logger.getLogger().log(Level.INFO, "${executionModel.tasks}")


        if (executionModel.isRepetitiveEvent) {
            // restart the event with the same params
            TaskManager.scheduleRepetitively(executionModel, applicationContext)
            Logger.getLogger().log(Level.INFO, "Task Recovered")
        }
    }

    protected abstract fun onTaskCreated(executionModel: ExecutionModel)
    protected abstract fun onTaskDestroyed(executionModel: ExecutionModel)

    private fun lock(enabled: Boolean, mutex: Mutex) {
        if (!enabled) {
            return
        }
        Semaphore.lock(mutex)
    }

    private fun unlock(enabled: Boolean, mutex: Mutex) {
        if (!enabled) {
            return
        }
        Semaphore.unlock(mutex)
    }

    private fun waitForUnlock(enabled: Boolean, mutex: Mutex) {
        if (!enabled) {
            return
        }
        Semaphore.waitForUnlock(mutex)
    }
}