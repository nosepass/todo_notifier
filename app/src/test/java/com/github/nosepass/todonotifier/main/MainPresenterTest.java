package com.github.nosepass.todonotifier.main;

import com.github.nosepass.todonotifier.Dagger;
import com.github.nosepass.todonotifier.DaggerDebugAppComponent;
import com.github.nosepass.todonotifier.DebugAppModule;
import com.github.nosepass.todonotifier.db.TodoPrefData;
import com.github.nosepass.todonotifier.kaffeine.LogHolder;
import com.github.nosepass.todonotifier.kaffeine.MyLog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

    DebugAppModule module;
    DatabaseCompartment cupboard;
    TodoPrefData model;
    TestScheduler testIoScheduler = new TestScheduler();

    @Before
    public void setUp() {
        LogHolder.log = new TestLog();

        cupboard = mock(DatabaseCompartment.class);
        model = new TodoPrefData();
        DatabaseCompartment.QueryBuilder<TodoPrefData> mockQuery = mock(DatabaseCompartment.QueryBuilder.class);
        when(cupboard.query(TodoPrefData.class)).thenReturn(mockQuery);
        when(mockQuery.get()).thenReturn(model);

        module = new DebugAppModule(true, null);
        module.setExternallyMockedSingleton(DatabaseCompartment.class, cupboard);
        Dagger.graph = DaggerDebugAppComponent.builder().debugAppModule(module).build();

        // Let the tests control the async operations done in MainPresenter
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            public Scheduler getIOScheduler() {
                return testIoScheduler;
            }
        });
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
    public void shouldNotifyViewOfModelLoadProgress() {
        assertTrue(false);
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
    public void shouldSaveModelOnEnableCheckboxChange() {
        assertTrue(false);
    }

    @Test
    public void shouldUpdateAlarmOnEnableCheckboxChange() {
        assertTrue(false);
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
    public void shouldSaveModelOnStop() {
        assertTrue(false);
    }

    @Test
    public void shouldUpdateAlarmOnStop() {
        assertTrue(false);
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

    private class TestLog implements MyLog {
        void println(String message, Throwable t) {
            System.out.printf("%s %s\n", message, t);
        }

        @Override
        public void v(@NotNull String tag, @NotNull String message, @Nullable Throwable exception) {
            println(message, exception);
        }

        @Override
        public void d(@NotNull String tag, @NotNull String message, @Nullable Throwable exception) {
            println(message, exception);
        }

        @Override
        public void i(@NotNull String tag, @NotNull String message, @Nullable Throwable exception) {
            println(message, exception);
        }

        @Override
        public void w(@NotNull String tag, @NotNull String message, @Nullable Throwable exception) {
            println(message, exception);
        }

        @Override
        public void e(@NotNull String tag, @NotNull String message, @Nullable Throwable exception) {
            println(message, exception);
        }

        @Override
        public void wtf(@NotNull String tag, @NotNull String message, @Nullable Throwable exception) {
            println(message, exception);
        }
    }
}
