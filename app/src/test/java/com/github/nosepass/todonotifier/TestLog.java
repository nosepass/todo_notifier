package com.github.nosepass.todonotifier;

import com.github.nosepass.todonotifier.kaffeine.MyLog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A log wrapper that redirects all outout to stdout instead of calling
 * the Android Log.
 */
public class TestLog implements MyLog {
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
