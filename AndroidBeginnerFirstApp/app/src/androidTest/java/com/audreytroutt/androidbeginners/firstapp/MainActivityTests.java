package com.audreytroutt.androidbeginners.firstapp;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTests {

    /**
     * ActivityTestRule will create and launch of the activity for you and also expose
     * the activity under test. To get a reference to the activity you can use
     * the ActivityTestRule#getActivity() method.
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void navigateToPaintingList() throws Exception {
        onView(withId(R.id.drawer_layout)).perform(open());

        onView(withText("Painting List")).perform(click());

        onView(withId(R.id.painting_list_recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToPaintingGrid() throws Exception {
        onView(withId(R.id.drawer_layout)).perform(open());

        onView(withText("Painting Grid")).perform(click());

        onView(withId(R.id.painting_grid_recycler_view)).check(matches(isDisplayed()));
    }
}