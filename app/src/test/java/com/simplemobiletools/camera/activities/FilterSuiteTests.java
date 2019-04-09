package com.simplemobiletools.camera.activities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class FilterSuiteTests {

    private MainActivity activity = null;

    @Before
    public void setup(){
        activity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void testEnableInversionFunction(){

        // verify that the camera effect is empty
        assert(activity.getCameraEffect().equals(""));

        // verify that filter toggle is false
        assert(!activity.getCurrentFilter());

        // call the function to test
        activity.enable_invert_filter();

        // assert that the above function changed the camera effect from empty to invert
        assert(activity.getCameraEffect().equals("invert"));

        // assert that the current filter toggle is switched to true
        assert(activity.getCurrentFilter());
    }

    @Test
    public void testEnableSepiaFunction(){

        // verify that the camera effect is empty
        assert(activity.getCameraEffect().equals(""));

        // verify that filter toggle is false
        assert(!activity.getCurrentFilter());

        // call the function to test
        activity.enable_sepia_filter();

        // assert that the above function changed the camera effect from empty to invert
        assert(activity.getCameraEffect().equals("sepia"));

        // assert that the current filter toggle is switched to true
        assert(activity.getCurrentFilter());
    }

    @Test
    public void testEnableAquaFunction(){

        // verify that the camera effect is empty
        assert(activity.getCameraEffect().equals(""));

        // verify that filter toggle is false
        assert(!activity.getCurrentFilter());

        // call the function to test
        activity.enable_aqua_filter();

        // assert that the above function changed the camera effect from empty to invert
        assert(activity.getCameraEffect().equals("aqua"));

        // assert that the current filter toggle is switched to true
        assert(activity.getCurrentFilter());
    }

    @Test
    public void testEnablePosterizeFunction(){

        // verify that the camera effect is empty
        assert(activity.getCameraEffect().equals(""));

        // verify that filter toggle is false
        assert(!activity.getCurrentFilter());

        // call the function to test
        activity.enable_posterize_filter();

        // assert that the above function changed the camera effect from empty to invert
        assert(activity.getCameraEffect().equals("posterize"));

        // assert that the current filter toggle is switched to true
        assert(activity.getCurrentFilter());
    }

    @Test
    public void testEnableBlackboardFunction(){

        // verify that the camera effect is empty
        assert(activity.getCameraEffect().equals(""));

        // verify that filter toggle is false
        assert(!activity.getCurrentFilter());

        // call the function to test
        activity.enable_blackboard_filter();

        // assert that the above function changed the camera effect from empty to invert
        assert(activity.getCameraEffect().equals("blackboard"));

        // assert that the current filter toggle is switched to true
        assert(activity.getCurrentFilter());
    }

    @Test
    public void testEnableBWFunction(){

        // verify that the camera effect is empty
        assert(activity.getCameraEffect().equals(""));

        // verify that filter toggle is false
        assert(!activity.getCurrentFilter());

        // call the function to test
        activity.enable_BW_Filter();

        // assert that the above function changed the camera effect from empty to invert
        assert(activity.getCameraEffect().equals("black_and_white"));

        // assert that the current filter toggle is switched to true
        assert(activity.getCurrentFilter());
    }

}
