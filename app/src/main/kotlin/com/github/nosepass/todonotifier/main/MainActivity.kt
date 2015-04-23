package com.github.nosepass.todonotifier.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.Toast
import com.github.nosepass.todonotifier.Dagger
import com.github.nosepass.todonotifier.R
import com.github.nosepass.todonotifier.db.TodoPrefData
import kotlinx.android.synthetic.activity_main.enable
import kotlinx.android.synthetic.activity_main.intervalSpinner
import rx.Observable
import rx.android.view.OnClickEvent
import rx.android.view.ViewObservable
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import javax.inject.Inject

/**
 * This acts as the view implementation for MainPresenter.
 */
public class MainActivity : AppCompatActivity(), MainView {

    var presenter: MainPresenter? = null
        [Inject] set
    override var enableObservable: BehaviorSubject<Boolean> = BehaviorSubject.create()
    override var intervalObservable: BehaviorSubject<Int> = BehaviorSubject.create()
    var intervalValues: IntArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        Dagger.graph.inject(this)
        setContentView(R.layout.activity_main)
        assert(enable != null)
        assert(intervalSpinner != null)

        presenter!!.onTakeView(this)

        val labels = getResources().getStringArray(R.array.nag_interval_labels)
        intervalValues = getResources().getIntArray(R.array.nag_interval_values)
        intervalSpinner.setAdapter(ArrayAdapter(this, R.layout.spinner_summary_item, R.id.summary, labels))
        enable.setEnabled(false)
        intervalSpinner.setEnabled(false)

        ViewObservable.clicks(enable, false)
                .map { e: OnClickEvent -> (e.view() as CheckedTextView).isChecked()  }
                .subscribe { enableObservable.onNext(it) }
        observeSelect(intervalSpinner, javaClass<String>())
                .map { intervalValues!![labels.indexOf(it)] } // transform label string to int interval
                .subscribe { intervalObservable.onNext(it) }
    }

    override fun onResume() {
        super<AppCompatActivity>.onResume()
        presenter!!.start()
    }

    override fun onPause() {
        super<AppCompatActivity>.onPause()
        presenter!!.finish()
    }

    override fun onDestroy() {
        presenter?.onDropView()
    }

    override fun updateFromModel(model: TodoPrefData) {
        enable.setChecked(model.enable)
        val pos = intervalValues!!.indexOf(model.interval)
        if (pos >= 0) intervalSpinner.setSelection(pos)
    }

    override fun setLoadInProgress(stillLoading: Boolean) {
        enable.setEnabled(!stillLoading)
        intervalSpinner.setEnabled(!stillLoading)
    }

    override fun onError(t: Throwable) {
        Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
    }

    // rx isn't terribly convenient for spinners
    // this helper creates a spinner observable
    fun observeSelect<T>(view: AdapterView<*>, adapterItemClass:Class<T>): Observable<T> {
        val subject = PublishSubject.create<T>()
        view.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position:Int, id:Long) {
                val item = adapterItemClass.cast(parent.getItemAtPosition(position))
                subject.onNext(item)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        })
        return subject
    }
}