package com.github.nosepass.todonotifier

/**
 * Represents the parts of lifecycle that are passed from a View to a Presenter
 */
public trait Presenter<T> {
    fun start()
    fun finish()
    fun onTakeView(view: T)
    fun onDropView()
}