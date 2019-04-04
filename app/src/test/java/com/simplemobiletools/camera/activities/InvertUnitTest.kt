package com.simplemobiletools.camera.activities

import android.widget.ImageView
import com.simplemobiletools.camera.R
import com.simplemobiletools.commons.extensions.isGone
import com.simplemobiletools.commons.extensions.isVisible
import junit.framework.Assert.assertNotNull
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Robolectric
import org.junit.Before
import org.junit.Test


@RunWith(RobolectricTestRunner::class)

open class InvertUnitTest {

    var activity: MainActivity? = null

    @Before
    @Throws(Exception::class)
    open fun setUp() {
        activity = Robolectric.buildActivity(MainActivity::class.java)
                .create()
                .resume()
                .visible()
                .get()
    }

    @Test
    @Throws(Exception::class)
    open fun invertFilter() {
        //assertNotNull(activity)

        //val invert = activity?.findViewById<ImageView>(R.id.invert)

        //assert(invert!!.isGone())

        //invert!!.performClick()

        //assert(invert.isVisible())
    }
}

