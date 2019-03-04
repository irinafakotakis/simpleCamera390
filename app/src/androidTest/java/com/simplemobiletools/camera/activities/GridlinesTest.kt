package com.simplemobiletools.camera.activities


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
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.simplemobiletools.camera.R
import junit.framework.Assert.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class GridlinesTest {

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
    fun gridlinesTest() {

        // Added a sleep statement to match the app's execution delay.
        Thread.sleep(2000)

        // retrieve the gridlines layout and icon from the UI
        val gridlines_layout = mActivityTestRule.activity.findViewById<RelativeLayout>(R.id.gridlines)
        val gridline_icon = mActivityTestRule.activity.findViewById<ImageView>(R.id.gridlines_icon)

        // Assert that the gridlines are hidden on startup
        assertNull(gridlines_layout.foreground)
        // Assert that the gridlines icon color is white on startup
        assertEquals(gridline_icon.tag, R.drawable.gridlines_white)

        // ------------- Toggle gridlines icon -------------
        val appCompatImageView = onView(
                allOf(withId(R.id.gridlines_icon),
                        childAtPosition(
                                allOf(withId(R.id.gridlines),
                                        childAtPosition(
                                                withId(R.id.view_holder),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageView.perform(click())
        // -------------------------------------------------

        // Assert that an image exists upon toggling gridlines icon
        assertNotNull(gridlines_layout.foreground)
        // Assert that gridlines are displayed on the foreground
        assertEquals(gridlines_layout.tag, R.drawable.gridlines43)
        // Assert that the toggle switched the icon color to black
        assertEquals(gridline_icon.tag, R.drawable.gridlines_black)

        // sleep once more to facilitate process tracking
        Thread.sleep(3000)

        // ------------- Toggle gridlines icon -------------
        val appCompatImageView2 = onView(
                allOf(withId(R.id.gridlines_icon),
                        childAtPosition(
                                allOf(withId(R.id.gridlines),
                                        childAtPosition(
                                                withId(R.id.view_holder),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageView2.perform(click())
        // -------------------------------------------------

        // Assert that the gridlines are hidden on second toggle
        assertNull(gridlines_layout.foreground)
        // Assert that the icon color has reverted back to white on toggle
        assertEquals(gridline_icon.tag, R.drawable.gridlines_white)

        // sleep once more to visually confirm gridlines revert process
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
