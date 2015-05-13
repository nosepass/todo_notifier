package com.github.nosepass.todonotifier;

import android.app.AlarmManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.nosepass.todonotifier.main.MainPresenter;

import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.qbusict.cupboard.DatabaseCompartment;
import rx.Scheduler;

/**
 * A Dagger module that can be switched to output mocked representations of dependencies.
 */
@Module
public class DebugAppModule {
    private AppModule appModule;
    private final boolean mock;
    private final Map<Class<?>, Object> externallyMocked = new HashMap<>();

    public DebugAppModule(boolean mock, Context appContext) {
        this.mock = mock;
        this.appModule = new AppModule(appContext);
    }

    private <T> T mock(Class<T> classToMock) {
        if (externallyMocked.containsKey(classToMock)) {
            return (T) externallyMocked.get(classToMock);
        }
        return Mockito.mock(classToMock);
    }

    /**
     * This lets me define different mocked behavior depending on the test involved.
     * It may not be necessary.
     */
    public <T> void setExternallyMockedSingleton(Class<T> mockedClass, T mockedInstance) {
        externallyMocked.put(mockedClass, mockedInstance);
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        if (mock) {
            return mock(Context.class);
        } else {
            return appModule.provideApplicationContext();
        }
    }

    @Provides
    @Singleton
    AlarmManager provideAlarmManager(Context c) {
        if (mock) {
            return mock(AlarmManager.class);
        } else {
            return appModule.provideAlarmManager(c);
        }
    }

    @Provides
    @Singleton
    MyAlarmManager provideMyAlarmManager(Context c, AlarmManager am) {
        if (mock) {
            return mock(MyAlarmManager.class);
        } else {
            return appModule.provideMyAlarmManager(c, am);
        }
    }

    @Provides
    @Singleton
    SQLiteDatabase providePrefDb(Context c) {
        if (mock) {
            return mock(SQLiteDatabase.class);
        } else {
            return appModule.providePrefDb(c);
        }
    }

    @Provides
    DatabaseCompartment provideCupboardCompartment(SQLiteDatabase prefDb) {
        if (mock) {
            return mock(DatabaseCompartment.class);
        } else {
            return appModule.provideCupboardCompartment(prefDb);
        }
    }

    @Provides
    @Singleton
    MainPresenter providePresenter() {
        if (mock) {
            return mock(MainPresenter.class);
        } else {
            return appModule.providePresenter();
        }
    }

    @Provides
    @Singleton
    @Named("MainThread")
    Scheduler provideAndroidMainThreadScheduler() {
        if (mock) {
            // TODO have the mock mechanism handle @Named
            return mock(Scheduler.class);
        } else {
            return appModule.provideAndroidMainThreadScheduler();
        }
    }
}
