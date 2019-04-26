package com.simplemobiletools.camera.activities


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
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class StickerUITest {

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
    fun stickerUITest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        // open stickers container
        val appCompatImageView = onView(
                allOf(withId(R.id.sticker),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                10),
                        isDisplayed()))
        appCompatImageView.perform(click())

        // close stickers container
        val appCompatImageView2 = onView(
                allOf(withId(R.id.smiley),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                11),
                        isDisplayed()))
        appCompatImageView2.perform(click())

        // open stickers container
        val appCompatImageView3 = onView(
                allOf(withId(R.id.smiley),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                11),
                        isDisplayed()))
        appCompatImageView3.perform(click())

        // click on smiley icon to be displayed
        val appCompatImageView4 = onView(
                allOf(withId(R.id.smiley),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                11),
                        isDisplayed()))
        appCompatImageView4.perform(click())

        // click on no sticker icon to remove smiley
        val appCompatImageView5 = onView(
                allOf(withId(R.id.no_sticker),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                13),
                        isDisplayed()))
        appCompatImageView5.perform(click())

        // click on daystamp icon to show the day of week
        val appCompatImageView6 = onView(
                allOf(withId(R.id.clockStamp),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                12),
                        isDisplayed()))
        appCompatImageView6.perform(click())

        // click again on daystamp to remove its sticker from camera preview
        val appCompatImageView7 = onView(
                allOf(withId(R.id.clockStamp),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                12),
                        isDisplayed()))
        appCompatImageView7.perform(click())

        // click on smiley icon
        val appCompatImageView8 = onView(
                allOf(withId(R.id.smiley),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                11),
                        isDisplayed()))
        appCompatImageView8.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(1500)

        // take picture with smiley icon
        val appCompatImageView9 = onView(
                allOf(withId(R.id.shutter),
                        childAtPosition(
                                allOf(withId(R.id.view_holder),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                12),
                        isDisplayed()))
        appCompatImageView9.perform(click())
        
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
