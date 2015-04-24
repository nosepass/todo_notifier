package com.github.nosepass.todonotifier

/**
 * Handle the details of setting or canceling Android alarms
 * This gets called on the configured interval and launches a notification.
 */
trait MyAlarmManager {
    fun setAlarm(intervalInMs: Long)
    fun cancelAlarm()
}