package com.googledev.sohag.alarmtoservice.core.throwable

/**
 * Thrown when no service is assigned to start.
 *
 * @author pavl_g.
 */
class TaskNotFoundException(message: String) : IllegalStateException(message)