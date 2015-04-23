package com.github.nosepass.todonotifier.kaffeine

class TestLog : MyLog {
    override fun v(tag: String, message: String, exception: Throwable?) { System.out.println("${message} ${exception}") }
    override fun d(tag: String, message: String, exception: Throwable?) { System.out.println("${message} ${exception}") }
    override fun i(tag: String, message: String, exception: Throwable?) { System.out.println("${message} ${exception}") }
    override fun w(tag: String, message: String, exception: Throwable?) { System.out.println("${message} ${exception}") }
    override fun e(tag: String, message: String, exception: Throwable?) { System.err.println("${message} ${exception}") }
    override fun wtf(tag: String, message: String, exception: Throwable?) { System.err.println("${message} ${exception}") }
}

