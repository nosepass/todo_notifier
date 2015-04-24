package com.github.nosepass.todonotifier

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.github.nosepass.todonotifier.kaffeine.v
import javax.inject.Inject

/**
 * Sets or cancels an alarm using setInexactRepeating
 */
open class MyAlarmManagerImpl[Inject](val context: Context, val alarmManager: AlarmManager) : MyAlarmManager {
    override fun setAlarm(intervalInMs: Long) {
        v("setAlarm ${intervalInMs}")
        cancelAlarm()
        val startTime = System.currentTimeMillis() + intervalInMs / 2
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalInMs, alarmIntent())
    }

    override fun cancelAlarm() {
        v("cancel")
        alarmManager.cancel(alarmIntent())
    }

    open fun alarmIntent() : PendingIntent {
        val i = Intent(context, javaClass<AlarmReceiver>())
        return PendingIntent.getBroadcast(context, 0, i, 0)
    }
}