package com.github.nosepass.todonotifier.kaffeine

/*
 * Copyright (C) 2015 Mobs & Geeks
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.util.Log
import kotlin.platform.platformStatic

// This junk is here to let tests mock out log calls, since mockable-android.jar doesn't want you calling it by default

fun log() : MyLog { return LogHolder.log }

trait MyLog {
    fun v(tag: String, message: String, exception: Throwable?)
    fun d(tag: String, message: String, exception: Throwable?)
    fun i(tag: String, message: String, exception: Throwable?)
    fun w(tag: String, message: String, exception: Throwable?)
    fun e(tag: String, message: String, exception: Throwable?)
    fun wtf(tag: String, message: String, exception: Throwable?)
}

class AndroidLog : MyLog {
    override fun v(tag: String, message: String, exception: Throwable?) { Log.v(tag, message, exception) }
    override fun d(tag: String, message: String, exception: Throwable?) { Log.d(tag, message, exception) }
    override fun i(tag: String, message: String, exception: Throwable?) { Log.i(tag, message, exception) }
    override fun w(tag: String, message: String, exception: Throwable?) { Log.w(tag, message, exception) }
    override fun e(tag: String, message: String, exception: Throwable?) { Log.e(tag, message, exception) }
    override fun wtf(tag: String, message: String, exception: Throwable?) { Log.wtf(tag, message, exception) }
}

public class LogHolder {
    companion object {
        platformStatic public var log: MyLog = AndroidLog()
    }
}

// The rest of these are just easy shortcuts for use from Kotlin

fun Any.logTag(): String { return this.javaClass.getSimpleName() }

public fun Any.v(message: String) {
    v(logTag(), message)
}

public fun Any.d(message: String) {
    d(logTag(), message)
}

public fun Any.i(message: String) {
    i(logTag(), message)
}

public fun Any.w(message: String) {
    w(logTag(), message)
}

public fun Any.e(message: String) {
    e(logTag(), message)
}

public fun Any.e(message: String, t: Throwable) {
    e(logTag(), message, t)
}

public fun Any.wtf(message: String) {
    wtf(logTag(), message)
}

public fun Any.v(tag: String, message: String) {
    v(tag, message, null)
}

public fun Any.d(tag: String, message: String) {
    d(tag, message, null)
}

public fun Any.i(tag: String, message: String) {
    i(tag, message, null)
}

public fun Any.w(tag: String, message: String) {
    w(tag, message, null)
}

public fun Any.e(tag: String, message: String) {
    e(tag, message, null)
}

public fun Any.wtf(tag: String, message: String) {
    wtf(tag, message, null)
}

public fun Any.v(tag: String, message: String, exception: Throwable?) {
    log().v(tag, message, exception)
}

public fun Any.d(tag: String, message: String, exception: Throwable?) {
    log().d(tag, message, exception)
}

public fun Any.i(tag: String, message: String, exception: Throwable?) {
    log().i(tag, message, exception)
}

public fun Any.w(tag: String, message: String, exception: Throwable?) {
    log().w(tag, message, exception)
}

public fun Any.e(tag: String, message: String, exception: Throwable?) {
    log().e(tag, message, exception)
}

public fun Any.wtf(tag: String, message: String, exception: Throwable?) {
    log().wtf(tag, message, exception)
}