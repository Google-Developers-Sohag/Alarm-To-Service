package com.googledev.sohag.alarmtoservice.core.util

import java.util.logging.Logger

object Logger {
    fun getLogger(): Logger {
        return Logger.getLogger("Alarm-To-Service")
    }
}