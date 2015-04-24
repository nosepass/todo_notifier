package com.github.nosepass.todonotifier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.nosepass.todonotifier.kaffeine.v

/**
 * This gets called on the configured interval and launches a notification.
 */
class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        v("onReceive")
        throw UnsupportedOperationException()
    }
}