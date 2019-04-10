package com.simplemobiletools.camera.activities;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.simplemobiletools.camera.R;
import org.apache.tools.ant.Main;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class BurstShotTests {

    private MainActivity activity = null;

    @Before
    public void setup() {
        activity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void testSinglePictureDoesNotEnableBurstMode() {

        // burst mode should not be enabled initially
        Assert.assertEquals(false, activity.isBurstModeEnabled());

        // click shutter button to take single picture, should not enable burst mode
        activity.shutterPressed();

        Assert.assertEquals(false, activity.isBurstModeEnabled());
    }

    @Test
    public void testBurstMode() {

        // test that burst mode is enabled
        Handler mockBurstHandler = Mockito.mock(Handler.class);

        Runnable mockBurstRunnable = Mockito.mock(Runnable.class);
        // burst mode should not be enabled initially

        Assert.assertEquals(false, activity.isBurstModeEnabled());

        Assert.assertEquals(true, activity.isInPhotoMode());

        // enableBurstMode should enable burst mode
        activity.enableBurstMode(mockBurstHandler, mockBurstRunnable);

        Assert.assertEquals(true, activity.isBurstModeEnabled());
    }

    @Test
    public void testBurstFlash() {

        // mock burst image and burst handler
        ImageView mockBurst = Mockito.mock(ImageView.class);

        Handler mockBurstHandler = Mockito.mock(Handler.class);

        // call burstFlash method
        activity.burstFlash(mockBurst, mockBurstHandler);

        // verify that the burst flash visibility is properly set
        Mockito.verify(mockBurst).setVisibility(View.VISIBLE);
    }
}
