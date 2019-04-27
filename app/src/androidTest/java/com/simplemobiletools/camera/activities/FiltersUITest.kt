package com.simplemobiletools.camera.activities


import android.hardware.camera2.CameraMetadata
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.simplemobiletools.camera.R
import com.simplemobiletools.camera.views.CameraPreview
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class FiltersUITest {

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
    fun filtersUITest() {

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(1000)

        // retrieve camera preview
        val testedPreview = (mainActivityTestRule.activity.getMPreview()) as CameraPreview

        // open filters suite
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



        // close filters suite
        val appCompatImageView2 = onView(
                allOf(withId(R.id.filter),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageView2.perform(click())

        // open filters suite
        val appCompatImageView3 = onView(
                allOf(withId(R.id.filter),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageView3.perform(click())

        // confirm no filters are applied
        Assert.assertEquals(testedPreview.getMCameraEffect(), CameraMetadata.CONTROL_EFFECT_MODE_OFF)

        // activate invert filter
        val appCompatImageView4 = onView(
                allOf(withId(R.id.invert),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()))
        appCompatImageView4.perform(click())

        // confirm invert filter is applied
        Assert.assertEquals(testedPreview.getMCameraEffect(), CameraMetadata.CONTROL_EFFECT_MODE_NEGATIVE)

        // activate b&w filter
        val appCompatImageView5 = onView(
                allOf(withId(R.id.bw),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                8),
                        isDisplayed()))
        appCompatImageView5.perform(click())

        // confirm b&w filter
        Assert.assertEquals(testedPreview.getMCameraEffect(), CameraMetadata.CONTROL_EFFECT_MODE_MONO)

        // activate solar filter
        val appCompatImageView6 = onView(
                allOf(withId(R.id.solar),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                7),
                        isDisplayed()))
        appCompatImageView6.perform(click())

        // confirm solarize filter
        Assert.assertEquals(testedPreview.getMCameraEffect(), CameraMetadata.CONTROL_EFFECT_MODE_SOLARIZE)

        // activate aqua filter
        val appCompatImageView7 = onView(
                allOf(withId(R.id.aqua),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()))
        appCompatImageView7.perform(click())

        // confirm aqua filter
        Assert.assertEquals(testedPreview.getMCameraEffect(), CameraMetadata.CONTROL_EFFECT_MODE_AQUA)

        // activate posterize filter
        val appCompatImageView8 = onView(
                allOf(withId(R.id.posterize),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                6),
                        isDisplayed()))
        appCompatImageView8.perform(click())

        // confirm posterize filter
        Assert.assertEquals(testedPreview.getMCameraEffect(), CameraMetadata.CONTROL_EFFECT_MODE_POSTERIZE)

        // activate sepia filter
        val appCompatImageView9 = onView(
                allOf(withId(R.id.sepia),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()))
        appCompatImageView9.perform(click())

        // confirm sepia filter
        Assert.assertEquals(testedPreview.getMCameraEffect(), CameraMetadata.CONTROL_EFFECT_MODE_SEPIA)

        // activate blackboard filter
        val appCompatImageView10 = onView(
                allOf(withId(R.id.blackboard),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()))
        appCompatImageView10.perform(click())

        // confirm blackboard filter
        Assert.assertEquals(testedPreview.getMCameraEffect(), CameraMetadata.CONTROL_EFFECT_MODE_BLACKBOARD)

        // de-activate filters
        val appCompatImageView11 = onView(
                allOf(withId(R.id.no_filter),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                9),
                        isDisplayed()))
        appCompatImageView11.perform(click())

        // confirm normal filter
        Assert.assertEquals(testedPreview.getMCameraEffect(), CameraMetadata.CONTROL_EFFECT_MODE_OFF)

        // close filters suite
        val appCompatImageView12 = onView(
                allOf(withId(R.id.filter),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageView12.perform(click())
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
