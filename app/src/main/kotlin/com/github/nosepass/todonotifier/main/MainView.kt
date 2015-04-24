package com.github.nosepass.todonotifier.main

import com.github.nosepass.todonotifier.db.TodoPrefData
import rx.subjects.BehaviorSubject

/**
 * Represents what actions MainPresenter can take on the view
 */
public trait MainView {
    public val enableObservable: BehaviorSubject<Boolean>
    public val intervalObservable: BehaviorSubject<Int>
    fun setLoadInProgress(stillLoading: Boolean)
    fun updateFromModel(model: TodoPrefData)
    fun onError(err: Throwable)
}