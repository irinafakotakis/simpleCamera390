package com.simplemobiletools.camera.activities.test

import android.Manifest
import android.app.Activity
import android.os.Environment
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.* // ktlint-disable no-wildcard-imports
import org.junit.Assert
import com.simplemobiletools.camera.R
import com.simplemobiletools.commons.extensions.isVisible
import kotlinx.android.synthetic.main.activity_settings.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowEnvironment
import org.junit.contrib.java.lang.system.EnvironmentVariables
import org.junit.Rule
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowApplication
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import org.robolectric.android.controller.ActivityController
import com.simplemobiletools.camera.activities.MainActivity


@RunWith(RobolectricTestRunner::class)
open class selfieFlashUnitTest {

//@get:Rule
//public final var environmentVariables = EnvironmentVariables()
//@Test
//fun selfieFlashUnitTest() {
//    environmentVariables.set("EMULATED_STORAGE_TARGET", "VALUE")
//
//    val controller = Robolectric.buildActivity(MainActivity::class.java)
//        var activity = controller.create().start().get()
//
//        var application = Shadows.shadowOf(activity?.getApplication())
//
//        application?.grantPermissions(Manifest.permission.CAMERA)
//        application?.grantPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//
//        // Get the toggle_camera button
//        var toggle_camera = activity?.findViewById<ImageView>(R.id.toggle_camera)
//        // Get the flash button
//        var flash = activity?.findViewById<ImageView>(R.id.toggle_flash)
//        // Flash icon is expected to be off
//        val expected_flash_icon = (activity?.getDrawable(R.drawable.ic_flash_off) as BitmapDrawable).bitmap
//        // Using front camera
//        flash?.performClick()
//
//        Assert.assertEquals(true, flash?.isVisible())

}



}
