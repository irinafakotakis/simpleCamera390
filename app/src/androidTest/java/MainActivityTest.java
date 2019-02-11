import com.simplemobiletools.camera.R;
import com.simplemobiletools.camera.activities.MainActivity;
import org.junit.Rule;
import androidx.test.rule.ActivityTestRule;


public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    /*
    * Must find a way retrieve background color from LinearLayout id: btn-holder
    * and compare its value with a preference_color_value
    * */
}

