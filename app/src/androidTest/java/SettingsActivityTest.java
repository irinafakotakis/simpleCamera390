import com.simplemobiletools.camera.R;
import com.simplemobiletools.camera.activities.SettingsActivity;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class SettingsActivityTest {

    @Rule
    public ActivityTestRule<SettingsActivity> settingsActivityActivityTestRule =
            new ActivityTestRule<>(SettingsActivity.class);

    @Test
    public void testTextOfDockerColorOption(){

        String expected = "App dock color";
        // Checking if app dock color text appears as an option in settings window
        onView(withId(R.id.customization_app_docker_color_label)).check(matches(withText(expected)));
    }

    @Test
    public void testSeekBar(){
        /*
        * Unable, as of yet, to implement a custom function to move seek bar.
        * Even if seek bar is moved, capturing its value by the event listener can't be done
        * using Espresso because its onView function only provides string performs/assertions
        * while we work with int for colors.
        * */
    }

    @Test
    public void testMyPreference(){
        /*
        * Not implemented. Getting thread errors because of this classes coupling
        * with the MainActivity and SettingsActivity and their corresponding resource files.
        * */
    }
}
