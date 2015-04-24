package com.github.nosepass.todonotifier

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.nosepass.todonotifier.kaffeine.e
import com.github.nosepass.todonotifier.kaffeine.v
import javax.inject.Inject

/**
 * This gets called on the configured interval and launches a notification.
 */
class AlarmReceiver: BroadcastReceiver() {
    companion  object {
        val EXTRA_INTERVAL: String = "EXTRA_INTERVAL"
    }
    val CANCEL_INTENT = intentString("CANCEL_INTENT")

    var context: Context? = null
        [Inject] set
    var alarmManager: AlarmManager? = null
        [Inject] set

    init {
        Dagger.graph.inject(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        v("onReceive")
        if (intent.getAction() == null) {
            // the nag interval triggered this
            showNotification(intent.getLongExtra(EXTRA_INTERVAL, -1))
        } else if (CANCEL_INTENT.equals(intent.getAction())) {
            cancelNotification()
        } else {
            e("unexpected action")
        }
    }

    fun setCancelNotificationAlarm() {
        val start = System.currentTimeMillis() + 10 * 1000 // TODO change this offset
        alarmManager!!.set(AlarmManager.RTC, start, cancelAlarmIntent())
    }

    fun showNotification(interval: Long) {

    }

    fun cancelNotification() {

    }

    open fun cancelAlarmIntent(): PendingIntent {
        val i = Intent(context!!, javaClass<AlarmReceiver>())
        i.setAction(CANCEL_INTENT)
        return PendingIntent.getBroadcast(context!!, 0, i, 0)
    }
}