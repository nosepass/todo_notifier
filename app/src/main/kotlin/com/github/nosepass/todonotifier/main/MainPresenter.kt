package com.github.nosepass.todonotifier.main

import android.content.Context
import com.github.nosepass.todonotifier.Dagger
import com.github.nosepass.todonotifier.Presenter
import com.github.nosepass.todonotifier.db.TodoPrefData
import com.github.nosepass.todonotifier.errorPassingSubscriber
import com.github.nosepass.todonotifier.kaffeine.v
import com.github.nosepass.todonotifier.sqlObservable
import nl.qbusict.cupboard.DatabaseCompartment
import rx.Subscription
import rx.subjects.BehaviorSubject
import java.util.LinkedList
import javax.inject.Inject

/**
 * The business logic for the main screen. The main screen is just a settings page
 * with an Enable Nagger checkbox and an interval-in-minutes dropdown.
 */
public open class MainPresenter : Presenter<MainView> {
    var context: Context? = null
        [Inject] set
    var cupboard: DatabaseCompartment? = null
        [Inject] set
    var view: MainView? = null
    var model: TodoPrefData? = null
    public val modelLoad: BehaviorSubject<TodoPrefData> = BehaviorSubject.create()
    public val errors: BehaviorSubject<Throwable> = BehaviorSubject.create()
    val subscriptions = LinkedList<Subscription>()

    init {
        v("init")
        Dagger.graph.inject(this)
    }

    override fun start() {
        v("start")
        if (model == null) {
//            rxPrefDb!!.query(javaClass<TodoPrefData>())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(errorPassingSubscriber(errors, {
//                        v("model loaded from sql")
//                        model = it
//                        modelLoad.onNext(it)
//                    }))
            sqlObservable {
                var todopref = cupboard!!.query(javaClass<TodoPrefData>()).get()
                if (todopref == null) {
                    todopref = TodoPrefData(TodoPrefData.INITIAL_INTERVAL_MINS)
                    cupboard!!.put(todopref)
                }
                todopref
            }.subscribe(errorPassingSubscriber(errors, { modelLoad.onNext(it) }))
        }
    }

    override fun finish() {
        v("finish")
        apply()
    }

    override fun onTakeView(view: MainView) {
        v("onTakeView")
        assert(this.view == null)
        this.view = view
        view.setLoadInProgress(true)
        subscriptions.push(modelLoad.subscribe { view.updateFromModel(it); view.setLoadInProgress(false) })
        subscriptions.push(errors.subscribe { view.onError(it) })
        subscriptions.push(view.enableObservable.subscribe { model?.enable = it; apply() })
        subscriptions.push(view.intervalObservable.subscribe { model?.interval = it })
    }

    override fun onDropView() {
        v("onDropView")
        view = null
        subscriptions.forEach { it.unsubscribe() }
        subscriptions.clear()
    }

    fun apply() {
        v("apply")
    }
}
