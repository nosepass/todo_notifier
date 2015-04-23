package com.github.nosepass.todonotifier;

import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ActivityTestCase {

    @Before
    public void setUp() {
        //MyApplication.getGraph().inject(this);
    }

    @Test
    public void testSettingsForm() {
        // The settings spinner adapter has a modification to show a label instead of the choice
        // as the main text, with the choice in smaller text underneath it
        onView(withId(R.id.intervalSpinner))
                .check(matches(withText(R.string.interval_label)));
    }
}