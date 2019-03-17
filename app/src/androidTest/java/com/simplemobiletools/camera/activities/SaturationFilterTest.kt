package com.simplemobiletools.camera.activities


import android.hardware.camera2.CameraMetadata
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.simplemobiletools.camera.R
import com.simplemobiletools.camera.views.CameraPreview
import junit.framework.Assert
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SaturationFilterTest {

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
    fun saturationFilterTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(5000)

        val appCompatImageView = onView(
                allOf(withId(R.id.filter),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageView.perform(click())

        val imageView = onView(
                allOf(withId(R.id.solar),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()))
        imageView.check(matches(isDisplayed()))

        val appCompatImageView2 = onView(
                allOf(withId(R.id.solar),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()))
        appCompatImageView2.perform(click())

        val appCompatImageView3 = onView(
                allOf(withId(R.id.settings),
                        childAtPosition(
                                allOf(withId(R.id.gridlines),
                                        childAtPosition(
                                                withId(R.id.view_holder),
                                                0)),
                                4),
                        isDisplayed()))
        appCompatImageView3.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(5000)

        val appCompatImageView4 = onView(
                allOf(withId(R.id.settings),
                        childAtPosition(
                                allOf(withId(R.id.gridlines),
                                        childAtPosition(
                                                withId(R.id.view_holder),
                                                0)),
                                4),
                        isDisplayed()))
        appCompatImageView4.perform(click())

        val appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(5000)

        val view = onView(
                allOf(withId(R.id.camera_texture_view),
                        childAtPosition(
                                allOf(withId(R.id.gridlines),
                                        childAtPosition(
                                                withId(R.id.view_holder),
                                                2)),
                                0),
                        isDisplayed()))
        view.check(matches(isDisplayed()))
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

    @Test
    fun testSaturationFilter() {

        val testedPreview = (mainActivityTestRule.activity.getMPreview()) as CameraPreview

        Assert.assertEquals(testedPreview.getMCameraEffect(), CameraMetadata.CONTROL_EFFECT_MODE_OFF)

        testedPreview.setCameraEffect("solarize")

        Assert.assertEquals(testedPreview.getMCameraEffect(), CameraMetadata.CONTROL_EFFECT_MODE_SOLARIZE)
    }
}
