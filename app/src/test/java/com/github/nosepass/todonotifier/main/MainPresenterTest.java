package com.github.nosepass.todonotifier.main;

import com.github.nosepass.todonotifier.Dagger;
import com.github.nosepass.todonotifier.DaggerDebugAppComponent;
import com.github.nosepass.todonotifier.DebugAppModule;
import com.github.nosepass.todonotifier.MyAlarmManager;
import com.github.nosepass.todonotifier.TestLog;
import com.github.nosepass.todonotifier.db.TodoPrefData;
import com.github.nosepass.todonotifier.kaffeine.LogHolder;

import org.junit.Before;
import org.junit.Test;

import nl.qbusict.cupboard.DatabaseCompartment;
import rx.Scheduler;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.TestScheduler;
import rx.subjects.BehaviorSubject;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

    DebugAppModule module;
    DatabaseCompartment cupboard;
    TodoPrefData model;
    MyAlarmManager alarmManager;
    static TestScheduler testIoScheduler = new TestScheduler();
    static RxJavaSchedulersHook schedulerHook;

    @Before
    public void setUp() {
        LogHolder.log = new TestLog();

        cupboard = mock(DatabaseCompartment.class);
        model = new TodoPrefData();
        DatabaseCompartment.QueryBuilder<TodoPrefData> mockQuery = mock(DatabaseCompartment.QueryBuilder.class);
        when(cupboard.query(TodoPrefData.class)).thenReturn(mockQuery);
        when(mockQuery.get()).thenReturn(model);

        alarmManager = mock(MyAlarmManager.class);

        module = new DebugAppModule(true, null);
        module.setExternallyMockedSingleton(DatabaseCompartment.class, cupboard);
        module.setExternallyMockedSingleton(MyAlarmManager.class, alarmManager);
        Dagger.graph = DaggerDebugAppComponent.builder().debugAppModule(module).build();

        // Let the tests control the async operations done in MainPresenter with a TestScheduler
        if (schedulerHook == null) {
            schedulerHook = new RxJavaSchedulersHook() {
                public Scheduler getIOScheduler() {
                    return testIoScheduler;
                }
            };
            // this can only be called once per vm :< TODO think of a better way to mock schedulers
            RxJavaPlugins.getInstance().registerSchedulersHook(schedulerHook);
        }
    }

    @Test
    public void modelShouldBeLoadedOnStart() {
        MainPresenter p = new MainPresenter();
        p.start();
        testIoScheduler.triggerActions();
        verify(cupboard).query(TodoPrefData.class); // TODO this is prolly unnecessary
        assertNotNull("model was not loaded", p.getModel());
    }

    @Test
    public void shouldCreateModelRowIfNotPresent() {
        assertTrue(false);
    }

    @Test
    public void shouldNotifyViewOfModelLoadProgress() {
        MainPresenter p = new MainPresenter();
        MainView view = mockView();
        p.onTakeView(view);
        verify(view).setLoadInProgress(true);

        p.start();
        testIoScheduler.triggerActions();
        verify(view).setLoadInProgress(false);
    }

    @Test
    public void shouldSendModelToViewWhenLoaded() {
        MainPresenter p = new MainPresenter();
        MainView view = getMockedViewInStartedState(p);
        verify(view).updateFromModel(model);
    }

    @Test
    public void shouldSendErrorsToView() {
        Throwable err = new RuntimeException("the sqlite failed for mysterious reasons!");
        module.setExternallyMockedSingleton(DatabaseCompartment.class, mockErrorCupboard(err));

        MainPresenter p = new MainPresenter();
        MainView view = getMockedViewInStartedState(p);
        verify(view).onError(err);
    }

    @Test
    public void shouldUpdateModelOnEnableCheckboxChange() {
        MainPresenter p = new MainPresenter();
        MainView view = getMockedViewInStartedState(p);

        assertEquals(false, model.enable);
        view.getEnableObservable().onNext(true);
        assertEquals("the model was not updated when the checkbox changed!", true, model.enable);
    }

    @Test
    public void shouldUpdateAlarmOnEnableCheckboxChange() {
        MainPresenter p = new MainPresenter();
        MainView view = getMockedViewInStartedState(p);

        view.getEnableObservable().onNext(true);
        verify(alarmManager).setAlarm(0);

        reset(alarmManager);
        view.getEnableObservable().onNext(false);
        verify(alarmManager, never()).setAlarm(anyLong());
        verify(alarmManager).cancelAlarm();
    }

    @Test
    public void shouldUpdateModelOnIntervalSpinnerChange() {
        MainPresenter p = new MainPresenter();
        MainView view = getMockedViewInStartedState(p);

        assertEquals(0, model.interval);
        view.getIntervalObservable().onNext(15);
        assertEquals("the model was not updated when the interval changed!", 15, model.interval);
    }

    @Test
    public void shouldSaveModelOnFinish() {
        MainPresenter p = new MainPresenter();
        p.start();
        p.finish();
        testIoScheduler.triggerActions();
        verify(cupboard).put(model);
    }

    @Test
    public void shouldUpdateAlarmOnFinish() {
        MainPresenter p = new MainPresenter();

        model.enable = true;
        model.interval = 15;
        p.start();
        testIoScheduler.triggerActions();
        p.finish();
        verify(alarmManager).setAlarm(15);
    }

    @Test
    public void shouldUnsubscribeFromView() {
        MainPresenter p = new MainPresenter();
        MainView view = getMockedViewInStartedState(p);
        p.onDropView();

        assertEquals(false, model.enable);
        assertEquals(0, model.interval);
        view.getEnableObservable().onNext(true);
        view.getIntervalObservable().onNext(15);
        String msg = "the view was still able to influence the Presenter after being dropped!";
        assertEquals(msg, false, model.enable);
        assertEquals(msg, 0, model.interval);
    }

    @Test
    public void shouldUnsubscribeFromView2() {
        MainPresenter p = new MainPresenter();
        MainView view = mockView();
        p.onTakeView(view);
        p.onDropView();
        p.start();
        testIoScheduler.triggerActions();

        verify(view, never()).updateFromModel(model);
    }

    private MainView getMockedViewInStartedState(MainPresenter p) {
        MainView view = mockView();
        p.onTakeView(view);
        p.start();
        testIoScheduler.triggerActions();
        return view;
    }

    private MainView mockView() {
        MainView view = mock(MainView.class);
        when(view.getEnableObservable()).thenReturn(BehaviorSubject.<Boolean>create());
        when(view.getIntervalObservable()).thenReturn(BehaviorSubject.<Integer>create());
        return view;
    }

    private DatabaseCompartment mockErrorCupboard(Throwable t) {
        DatabaseCompartment cupboard = mock(DatabaseCompartment.class);
        when(cupboard.query(TodoPrefData.class)).thenThrow(t);
        return cupboard;
    }
}
