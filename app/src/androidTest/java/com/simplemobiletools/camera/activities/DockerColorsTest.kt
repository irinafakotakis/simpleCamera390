package com.simplemobiletools.camera.activities


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.divyanshu.colorseekbar.ColorSeekBar
import com.simplemobiletools.camera.R
import com.simplemobiletools.camera.helpers.MyPreference
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotSame
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
class DockerColorsTest {

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
    fun dockerColorTest() {

        val myPreference = MyPreference(mActivityTestRule.activity)
        val color_seek_bar = mActivityTestRule.activity.findViewById<ColorSeekBar>(R.id.color_seek_bar)
        val btn_holder = mActivityTestRule.activity.findViewById<LinearLayout>(R.id.btn_holder)
        var color = Color.TRANSPARENT
        var background = btn_holder.background

        // convert background to color drawable
        if (background is ColorDrawable)
            color = background.color

        // initial values
        val initial_docker_color = color
        val initial_saved_color = myPreference.getDockerColor()

         // assert the starting position of the color-seek-bar remains constant.
        assertEquals(initial_docker_color, initial_saved_color)

        // assert that docker color will only be visibility on switch
        assertEquals(color_seek_bar.visibility, View.INVISIBLE)

        // Added a sleep statement to match the app's execution delay.
        Thread.sleep(2000)

        // ------------- flip seek-bar switch -------------
        val switch_ = onView(
                allOf(withId(R.id.seekbar_switch),
                        childAtPosition(
                                allOf(withId(R.id.gridlines),
                                        childAtPosition(
                                                withId(R.id.view_holder),
                                                0)),
                                2),
                        isDisplayed()))
        switch_.perform(click())
        // -------------------------------------------------


        // assert that seek bar has become visible
        assertEquals(color_seek_bar.visibility, View.VISIBLE)

        // Sleep once more to visually confirm seek-bar visibility
        Thread.sleep(3000)

        // ---------- execute a swipe on seek-bar ----------
        val seekbar_swipe = Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.color_seek_bar)))
        seekbar_swipe.perform(ViewActions.swipeRight())
        // -------------------------------------------------

        // convert background to color drawable
        background = btn_holder.background

        if (background is ColorDrawable)
            color = background.color

        // final values
        val final_docker_color = color
        val final_saved_color = myPreference.getDockerColor()

        // confirm that the docker color has changed post-swipe
        assertEquals(final_docker_color, final_saved_color)

        // Confirm that initial values are not the same as final values
        assertNotSame(initial_docker_color, final_docker_color)
        assertNotSame(initial_saved_color, final_saved_color)

        // Sleep to confirm seek-bar thumb has changed position
        Thread.sleep(3000)

        // ------------- flip seek-bar switch -------------
        switch_.perform(click())
        // -------------------------------------------------

        // Confirm that seek-bar has been hidden
        assertEquals(color_seek_bar.visibility, View.INVISIBLE)

        // sleep to confirm visually that seek-bar is hidden
        Thread.sleep(1000)
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
