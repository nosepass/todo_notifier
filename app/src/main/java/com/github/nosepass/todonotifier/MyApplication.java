package com.github.nosepass.todonotifier;

import android.app.Application;

/**
 * This is in java because Dagger's code generation runs after Kotlin compile, so it needs to be
 * this way to access the generated DaggerAppComponent
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Debug.waitForDebugger();
        Dagger.graph = createAppComponent();
        Dagger.graph.inject(this);
    }

    protected AppComponent createAppComponent() {
        return DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }
}
