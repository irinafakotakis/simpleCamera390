package com.simplemobiletools.camera.activities


import android.hardware.camera2.CameraMetadata
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.simplemobiletools.camera.R
import com.simplemobiletools.camera.views.CameraPreview
import junit.framework.Assert.assertEquals
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class BlackAndWhiteFilterTest {

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
    fun blackAndWhiteFilterTest() {
        // Added a sleep statement to match the app's execution delay.
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

        val appCompatImageView4 = onView(
                allOf(withId(R.id.bw),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()))
        appCompatImageView4.perform(click())
    }

    @Test
    fun bwUnitTest() {
        // Added a sleep statement to match the app's execution delay.
        Thread.sleep(5000)

        val filter_icon = mActivityTestRule.activity.findViewById<ImageView>(R.id.filter)
        val saturation_icon = mActivityTestRule.activity.findViewById<ImageView>(R.id.bw)
        val textureView = mActivityTestRule.activity.findViewById<RelativeLayout>(R.id.camera_texture_view)

        val testedPreview = CameraPreview(mActivityTestRule.activity)

        assertEquals(testedPreview.getMCameraEffect(), CameraMetadata.CONTROL_EFFECT_MODE_OFF)

        testedPreview.setCameraEffect("black_and_white")

        assertEquals(testedPreview.getMCameraEffect(), CameraMetadata.CONTROL_EFFECT_MODE_MONO)
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
