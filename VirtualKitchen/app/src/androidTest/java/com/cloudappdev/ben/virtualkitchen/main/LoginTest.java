package com.cloudappdev.ben.virtualkitchen.main;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.cloudappdev.ben.virtualkitchen.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Benit Kibabu on 26/05/2017.
 */
public class LoginTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mainActivity = null;

    @Rule
    public ActivityTestRule<Login> loginActivityTestRule = new ActivityTestRule<Login>(Login.class);
    private Login login = null;

    Instrumentation.ActivityMonitor mainActivityMonitor = getInstrumentation()
            .addMonitor(MainActivity.class.getSimpleName(), null, false);

    @Before
    public void setUp() throws Exception {
        login = loginActivityTestRule.getActivity();
        mainActivity = mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch() throws Exception {
        View view = login.findViewById(R.id.activity_login);
        assertNotNull(view);
    }

    @Test
    public void launchMainActivityOnSignIn() throws  Exception{
        assertNotNull(login.signinBtn);

        login.username.setText("benitkibabu");
        login.password.setText("B3nitskibabu");

        onView(withId(R.id.signinBtn)).perform(click());

        Activity mainActivity = getInstrumentation()
                .waitForMonitorWithTimeout(mainActivityMonitor, 5000);
        assertNotNull(mainActivity);
        mainActivity.finish();
    }

    @After
    public void tearDown() throws Exception {
        login = null;
    }

}