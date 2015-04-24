package com.github.nosepass.todonotifier;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import com.github.nosepass.todonotifier.kaffeine.LogHolder;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MyAlarmManagerTest {

    MyAlarmManagerImpl subject;
    Context context;
    AlarmManager alarmManager;
    PendingIntent alarmIntent;

    @Before
    public void setUp() {
        LogHolder.log = new TestLog();
        context = mock(Context.class);
        alarmManager = mock(AlarmManager.class);
        alarmIntent = mock(PendingIntent.class);
        subject = spy(new MyAlarmManagerImpl(context, alarmManager) {
            // I'd rather doReturn(alarmIntent).when(subject).alarmIntent()
            // but something about Kotlin prevents that
            @Override
            public PendingIntent alarmIntent() {
                return alarmIntent;
            }
        });
    }

    @Test
    public void shouldSetAlarm() {
        long interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        subject.setAlarm(interval);
        verify(alarmManager).setInexactRepeating(eq(AlarmManager.RTC_WAKEUP), anyLong(),
                eq(interval), eq(alarmIntent));
    }

    @Test
    public void shouldCancelOldOnSetAlarm() {
        long interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        subject.setAlarm(interval);
        verify(alarmManager).cancel(alarmIntent);
    }

    @Test
    public void shouldCancelAlarm() {
        subject.cancelAlarm();
        verify(alarmManager).cancel(alarmIntent);
    }
}
