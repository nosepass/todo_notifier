package com.github.nosepass.todonotifier;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Alternate Dagger component graph used in tests.
 */
@Singleton
@Component(modules = DebugAppModule.class)
public interface DebugAppComponent extends AppComponent {
}
