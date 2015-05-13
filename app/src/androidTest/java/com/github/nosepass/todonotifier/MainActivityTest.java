package com.github.nosepass.todonotifier;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.github.nosepass.todonotifier.main.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

//    @Before
//    public void setUp() {
//        //MyApplication.getGraph().inject(this);
//    }

//    //TODO I haven't figured out whether that suffix is significant
//    @Test
//    public void testSettingsForm_sameActivity() {
//        onView(withId(R.id.intervalSpinner))
//                .check(matches(withText(R.string.interval_label)));
//    }

    @Test
    public void testSpinnerStaticTitle() {
        // The settings spinner adapter has a modification to show a static label instead of the choice
        // as the main text, with the choice in smaller text underneath it
        ViewInteraction spinner = onView(withId(R.id.intervalSpinner));
        spinner.check(matches(hasDescendant(withText(R.string.interval_label))));
        String first = getFirstIntervalOptionLabel();
        spinner.check(matches(hasDescendant(withText(first))));
        spinner.check(matches(withSpinnerText(first)));
    }

    @Test
    public void testSpinnerDropdownText() {
        // In spite of the modification, the dropdown items should show only the choice labels
        DataInteraction spinnerEntry = onData(anything())
                .inAdapterView(withId(R.id.intervalSpinner))
                .atPosition(0);
        String first = getFirstIntervalOptionLabel();
        spinnerEntry.check(matches(withChild(withText(first))));
    }

    private String getFirstIntervalOptionLabel() {
        return rule.getActivity().getResources().getStringArray(R.array.nag_interval_labels)[0];
    }
}