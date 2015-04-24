package com.github.nosepass.todonotifier

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import javax.inject.Inject

/**
 * TODO
 * Handle the details of setting or canceling Android alarms
 * This gets called on the configured interval and launches a notification.
 */
open class MyAlarmManagerImpl[Inject](val context: Context, val alarmManager: AlarmManager) : MyAlarmManager {
    override fun setAlarm(intervalInMinutes: Int) {
        System.out.println("test-ori")
        val i = Intent(context, javaClass<AlarmReceiver>())
        val alarmIntent = PendingIntent.getBroadcast(context, 0, i, 0);
        //alarmManager.get
    }

    override fun cancelAlarm() {

    }
}