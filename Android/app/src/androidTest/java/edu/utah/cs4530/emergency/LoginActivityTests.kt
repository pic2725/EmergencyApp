package edu.utah.cs4530.emergency

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import edu.utah.cs4530.emergency.activity.LoginActivity
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LoginActivityTests {

    @Test
    fun loginButtonClick_MatchesIsDisplayed() {
        val firstActivity: ActivityTestRule<LoginActivity> = ActivityTestRule<LoginActivity>(LoginActivity::class.java);
        firstActivity.launchActivity(Intent());
        onView(withId(R.id.btn_googleLogin)).perform(click()).check(matches(isDisplayed()));
    }

    @Test
    fun showProgressBar_IsVisible() {
        val firstActivity: ActivityTestRule<LoginActivity> = ActivityTestRule<LoginActivity>(LoginActivity::class.java);
        firstActivity.launchActivity(Intent());
        onView(withId(R.id.btn_googleLogin)).perform(click());
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()));
    }
}
