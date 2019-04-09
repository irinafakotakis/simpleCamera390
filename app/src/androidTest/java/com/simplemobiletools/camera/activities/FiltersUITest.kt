package com.simplemobiletools.camera.activities


import androidx.test.espresso.DataInteraction
import androidx.test.espresso.ViewInteraction
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent

import androidx.test.InstrumentationRegistry.getInstrumentation
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*

import com.simplemobiletools.camera.R

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.`is`

@LargeTest
@RunWith(AndroidJUnit4::class)
class FiltersUITest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SplashActivity::class.java)

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
Thread.sleep(7000)
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
Thread.sleep(7000)
        
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
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
Thread.sleep(7000)
        
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
