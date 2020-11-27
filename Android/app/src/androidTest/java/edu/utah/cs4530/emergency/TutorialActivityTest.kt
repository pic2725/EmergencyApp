package edu.utah.cs4530.emergency

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import edu.utah.cs4530.emergency.activity.MainActivity
import edu.utah.cs4530.emergency.activity.TutorialActivity
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.hamcrest.Matchers.notNullValue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TutorialActivityTest {

    @Test
    fun skipButton_IsVisible() {
        val activity: ActivityTestRule<TutorialActivity> = ActivityTestRule<TutorialActivity>(TutorialActivity::class.java);
        activity.launchActivity(Intent());
        onView(withId(R.id.textSkipIntro)).check(matches(isDisplayed()));
    }
}