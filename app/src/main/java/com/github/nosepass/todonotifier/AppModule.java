package com.github.nosepass.todonotifier;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.nosepass.todonotifier.db.CupboardSQLiteOpenHelper;
import com.github.nosepass.todonotifier.main.MainPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.qbusict.cupboard.DatabaseCompartment;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * A module for various android-specific dependencies.
 */
@Module
public class AppModule {
    private final Context appContext;

    public AppModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return appContext;
    }

    @Provides
    @Singleton
    SQLiteDatabase providePrefDb(Context c) {
        return new CupboardSQLiteOpenHelper(c).getWritableDatabase();
    }

    @Provides
    DatabaseCompartment provideCupboardCompartment(SQLiteDatabase prefDb) {
        return cupboard().withDatabase(prefDb);
    }

    /**
     * I am making the presenter a singleton for the moment to let it live outside
     * of the activity lifecycle. Ideally it should have some kind of scope that lets it survive
     * confiuration changes yet also be cleaned up when no longer needed.
     */
    @Provides
    @Singleton
    MainPresenter providePresenter() {
        return new MainPresenter();
    }

//    @Provides
//    @Singleton
//    @Named("MainThread")
//    Scheduler provideAndroidMainThreadScheduler() {
//        return AndroidSchedulers.mainThread();
//    }
}
