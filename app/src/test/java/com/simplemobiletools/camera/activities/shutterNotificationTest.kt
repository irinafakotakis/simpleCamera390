package com.simplemobiletools.camera.activities

import android.app.*
import com.simplemobiletools.camera.helpers.MyPreference
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat.getSystemService
import com.simplemobiletools.camera.R
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
lateinit var notificationManager : NotificationManager
lateinit var notificationChannel : NotificationChannel
private val mockchannelId = "com.simplemobiletools.camera.activities"
private val mockdescription = "Test notification"
lateinit var mockBuilder : Notification.Builder



@RunWith(MockitoJUnitRunner::class)
class shutterNotificationTest {

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

    @Test
    fun shutterNotificationTest(){

        //tests to be completed/added/replaced, unable to mock builder

        //initialize mainActivity to access MainActivity()
        //val mainActivity = MainActivity()


        //mocking the builder and initialize it with mockchannelID and set its values
       /* mockBuilder = Notification.Builder(mainActivity, mockchannelId)
                .setContentTitle("Picture Taken")
                .setContentText("Saving...")
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(mainActivity.resources, R.drawable.ic_launcher))

        //assertEquals statements to verify that our mockBuilder values equals the values of the builder in MainActivity()
        assertEquals(mockBuilder.setContentTitle("Picture Taken"), mainActivity.builder.setContentTitle("Picture Taken"))
        assertEquals(mockBuilder.setContentText("Saving..."), mainActivity.builder.setContentTitle("Saving..."))
        assertEquals(mockBuilder.setSmallIcon(R.drawable.ic_launcher_round), mainActivity.builder.setSmallIcon(R.drawable.ic_launcher_round))
        assertEquals(mockBuilder.setLargeIcon(BitmapFactory.decodeResource(mainActivity.resources, R.drawable.ic_launcher)), mainActivity.builder.setLargeIcon(BitmapFactory.decodeResource(mainActivity.resources, R.drawable.ic_launcher)))
*/

    }
}

