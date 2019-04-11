package com.simplemobiletools.camera.activities;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.simplemobiletools.camera.R;
import com.simplemobiletools.camera.views.CameraPreview;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class SelfieFlashTests {

    private MainActivity activity = null;

    @Before
    public void setup(){
        activity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void testSelfieFlash(){

        // Mock for the selfie flash image
        ImageView mock_selfie = Mockito.mock(ImageView.class);

        // Mock for the fade handler during picture capture
        Handler mock_fade_handler = Mockito.mock(Handler.class);

        // call function to test with appropriate mocks
        activity.selfieFlash(mock_selfie, mock_fade_handler);

        // verify that the visibility setter was called on our mock inside the desired function
        Mockito.verify(mock_selfie).setVisibility(View.VISIBLE);
    }

    @Test
    public void testDisplaySelfieFlash(){

        // flash toggle is mocked
        ImageView mock_toggle = Mockito.mock(ImageView.class);

        // camera preview is mocked
        CameraPreview mock_preview = Mockito.mock(CameraPreview.class);

        // this stub is needed to execute displaySelfieFlash() function to completion
        Mockito.when(mock_preview.isUsingFrontCamera()).thenReturn(true);

        // assert that the selfie flash toggle is not on
        assert(!activity.getSelfieFlashOn());

        // call function to test with appropriate mocks
        activity.displaySelfieFlash(mock_preview, mock_toggle);

        // verify that the flash toggle was mutated during call of desired function
        Mockito.verify(mock_toggle).setImageResource(R.drawable.ic_flash_off);
    }
}
