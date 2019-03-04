package com.simplemobiletools.camera.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.doubleClick
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.simplemobiletools.camera.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ShutterNotificationTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.CAMERA",
                    "android.permission.WRITE_EXTERNAL_STORAGE")

    @Test
    fun splashActivityTest() {

        // Added a sleep statement to match the app's execution delay. ml
        Thread.sleep(2000)

        // ---------------- double-click shutter for notification to appear -------------
        val appCompatImageView = onView(
                allOf(withId(R.id.shutter),
                        childAtPosition(
                                allOf(withId(R.id.btn_holder),
                                        childAtPosition(
                                                withId(R.id.view_holder),
                                                1)),
                                1),
                        isDisplayed()))
        appCompatImageView.perform(doubleClick())
        // ------------------------------------------------------------------------------

        val appCompatImageView2 = Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.gridlines)))

        // ---------------- swipe up on screen to close notification --------------------
        appCompatImageView2.perform(ViewActions.swipeUp())
        // ------------------------------------------------------------------------------

        Thread.sleep(5000)

        // ---------------- toggle camera to test notification  -------------------------
        val appCompatImageView3 = onView(
                allOf(withId(R.id.toggle_camera),
                        childAtPosition(
                                allOf(withId(R.id.btn_holder),
                                        childAtPosition(
                                                withId(R.id.view_holder),
                                                1)),
                                0),
                        isDisplayed()))
        appCompatImageView3.perform(click())
        // ------------------------------------------------------------------------------

        Thread.sleep(2000)

        // ---------------- double-click shutter for notification to appear -------------
        val appCompatImageView4 = onView(
                allOf(withId(R.id.shutter),
                        childAtPosition(
                                allOf(withId(R.id.btn_holder),
                                        childAtPosition(
                                                withId(R.id.view_holder),
                                                1)),
                                1),
                        isDisplayed()))
        appCompatImageView4.perform(doubleClick())
        // ------------------------------------------------------------------------------

        Thread.sleep(2000)

    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
