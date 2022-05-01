package com.googledev.sohag.alarmtoservice.task.mutex

object Definition {
    val INIT_MUTEX: Mutex = Mutex.UNLOCK
    val DEST_MUTEX: Mutex = Mutex.UNLOCK
}

enum class Mutex(private var state: String?) {

    LOCK("LOCKED"), UNLOCK(null);

    fun set(mutex: Mutex) {
        this.state = mutex.state
    }

    fun getState(): String? {
        return this.state
    }
}
