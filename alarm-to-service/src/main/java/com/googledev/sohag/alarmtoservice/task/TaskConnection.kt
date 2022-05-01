package com.googledev.sohag.alarmtoservice.task

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.googledev.sohag.alarmtoservice.core.util.Logger
import java.util.logging.Level

class TaskConnection : ServiceConnection {
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
    }

    override fun onServiceDisconnected(name: ComponentName?) {
    }

    override fun onNullBinding(name: ComponentName?) {
        Logger.getLogger().log(Level.INFO, "A Service has been connected")
    }
}