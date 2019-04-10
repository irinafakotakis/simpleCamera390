package com.simplemobiletools.camera.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
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
class SelfieFlashTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SplashActivity::class.java)

    @Rule
    @JvmField
    var mainActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.CAMERA",
                    "android.permission.WRITE_EXTERNAL_STORAGE")

    @Test
    fun selfieFlashTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(5000)

        val appCompatImageView = onView(
                allOf(withId(R.id.toggle_camera),
                        childAtPosition(
                                allOf(withId(R.id.btn_holder),
                                        childAtPosition(
                                                withId(R.id.view_holder),
                                                6)),
                                0),
                        isDisplayed()))
        appCompatImageView.perform(click())

        Thread.sleep(5000)

        val appCompatImageView2 = onView(
                allOf(withId(R.id.toggle_flash),
                        childAtPosition(
                                allOf(withId(R.id.btn_holder),
                                        childAtPosition(
                                                withId(R.id.view_holder),
                                                6)),
                                2),
                        isDisplayed()))
        appCompatImageView2.perform(click())

        Thread.sleep(5000)

        val shutter =
                Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.shutter)))


        shutter.perform(ViewActions.click())
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
