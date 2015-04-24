package com.github.nosepass.todonotifier;

import com.github.nosepass.todonotifier.main.MainActivity;
import com.github.nosepass.todonotifier.main.MainPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(MyApplication a);
    void inject(MainActivity a);
    void inject(MainPresenter a);
    void inject(AlarmReceiver a);
}
