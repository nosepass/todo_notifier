package com.github.nosepass.todonotifier;

import android.app.AlarmManager;
import android.content.Context;

import com.github.nosepass.todonotifier.kaffeine.LogHolder;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class MyAlarmManagerTest {

    Context context;
    AlarmManager alarmManager;

    @Before
    public void setUp() {
        LogHolder.log = new TestLog();
        context = mock(Context.class);
        alarmManager = mock(AlarmManager.class);
    }

    @Test
    public void shouldDoSomething() {
        // TODO not quite sure how to test this yet
    }
}
