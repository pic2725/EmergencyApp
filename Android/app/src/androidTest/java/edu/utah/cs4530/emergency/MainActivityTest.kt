package edu.utah.cs4530.emergency

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import edu.utah.cs4530.emergency.activity.MainActivity
import edu.utah.cs4530.emergency.activity.TutorialActivity
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun toolBar_IsVisible() {
        val activity: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(MainActivity::class.java);
        activity.launchActivity(Intent());
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    fun drawerLayout_IsVisible() {
        val activity: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(MainActivity::class.java);
        activity.launchActivity(Intent());
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
    }
}
