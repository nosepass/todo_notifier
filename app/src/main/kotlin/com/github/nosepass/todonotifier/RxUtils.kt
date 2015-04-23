package com.github.nosepass.todonotifier

import com.github.nosepass.todonotifier.kaffeine.e
import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.schedulers.Schedulers
import rx.subjects.Subject
import javax.inject.Inject
import javax.inject.Named


/**
 * Shorthand to run a lambda to load an object of type T on an IO thread, giving
 * the result to any observers on the main thread.
 * @param f the function to run on the IO thread
 */
fun sqlObservable<T>(f: () -> T) : Observable<T> {
    return Observable.create(Observable.OnSubscribe<T>() { it.onNext(f()) })
            .subscribeOn(Schedulers.io())
            // Currently I am settling for always creating this observable on the main thread, since AndroidSchedulers is hard to mock
            //.observeOn(AndroidSchedulers.mainThread())
}

/**
 * A shorthand for a subscriber onNext function that also passes on any onErrors to errorSubject.
 * The errors are also logged to logcat.
 * @param errorSubject the subject that gets passed any onError calls that occur
 * @param f the onNext function
 */
fun errorPassingSubscriber<T>(errorSubject: Subject<Throwable, Throwable>, f: (T) -> Unit) : Subscriber<T> {
    return object : Subscriber<T>() {
        override fun onNext(it: T) { f(it) }
        override fun onCompleted() { }
        override fun onError(ex: Throwable) {
            e("Error loading settings from sql!", ex)
            errorSubject.onNext(ex) // TODO classify the types of errors a Presenter can emit
        }
    }
}

/**
 * So I want to mock the mainThread scheduler, but doing it in this Kotlin file with Dagger
 * is kind of a pain in the ass. This was an attempt to inject the scheduler instead of
 * using AndroidSchedulers.mainThread(). However Dagger didn't recognize the Inject annotation
 * for some reason.
 */
fun mainThreadScheduler() : Scheduler? {
    // This class needs to be outside to be seen by Dagger
    class InjectedSchedulerWorkaround {
        var mainThreadScheduler: Scheduler? = null
            [Inject] set([Named("MainThread")] v) { $mainThreadScheduler = v }
        init {
            //Dagger.graph.inject(this)
        }
    }
    return InjectedSchedulerWorkaround().mainThreadScheduler
}