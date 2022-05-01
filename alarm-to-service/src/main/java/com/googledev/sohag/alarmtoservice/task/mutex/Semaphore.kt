package com.googledev.sohag.alarmtoservice.task.mutex

object Semaphore {
    fun unlock(mutex: Mutex) {
        mutex.set(Mutex.UNLOCK)
    }

    fun lock(mutex: Mutex) {
        mutex.set(Mutex.LOCK)
    }

    fun waitForUnlock(mutex: Mutex) {
        while (mutex != Mutex.UNLOCK) {
            doNothing()
        }
    }

    /**
     * Trick the dalvik by making a nothing task !
     *
     * @return null job.
     */
    private fun doNothing(): Void? {
        return null
    }
}