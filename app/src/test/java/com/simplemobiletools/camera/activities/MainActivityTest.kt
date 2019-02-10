package com.simplemobiletools.camera.activities

import com.simplemobiletools.camera.helpers.MyPreference
import android.content.Context
import android.content.SharedPreferences
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner

private const val FAKE_COLOUR = 0xFF0000
private const val FAKE_PREFERENCE = "DockColor"
private const val FAKE_PREFERENCE_NAME = "SharedPreferenceExample"
private const val FAKE_PREFERENCE_MODE = Context.MODE_PRIVATE

@RunWith(MockitoJUnitRunner::class)
class MainActivityTest {

    @Mock
    private lateinit var mockContext: Context
    @Mock
    private lateinit var mockPreferences: SharedPreferences
    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    @Test
    fun helloWorldReturnsGenericMessage() {
        val mainActivity = MainActivity()
        assertEquals("Hello, World!", mainActivity.helloWorld())
    }

    @Test
    fun colourCustomisationTest() {
        `when`(mockContext.getSharedPreferences(FAKE_PREFERENCE_NAME, FAKE_PREFERENCE_MODE)).thenReturn(mockPreferences)
        `when`(mockPreferences.getInt(FAKE_PREFERENCE, -7508381)).thenReturn(FAKE_COLOUR)
        `when`(mockPreferences.edit()).thenReturn(mockEditor)

        // verify that the colour change is applied
        //val mainActivity = MainActivity()
        val preference = MyPreference(mockContext)
        // set docker colour
        preference.setDockerColor(FAKE_COLOUR)
        val customColour = preference.getDockerColor()

        //mainActivity.setDockerColour(preference)
        assertEquals(0xFF0000, customColour)
    }
}
