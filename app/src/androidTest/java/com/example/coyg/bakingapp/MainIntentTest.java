package com.example.coyg.bakingapp;


import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.coyg.bakingapp.main.Main2Activity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;


@RunWith(AndroidJUnit4.class)
public class MainIntentTest
{
    @Rule
    public IntentsTestRule<Main2Activity> main2ActivityIntentsTestRule =
            new IntentsTestRule<> (Main2Activity.class);

    @Before
    public void registerIdlingResource()
    {
        intending(not(isInternal())).respondWith
                (new Instrumentation.ActivityResult
                        (Activity.RESULT_OK, null));
    }

    @Test
    public void clickMainListItem_viewItemHasIntent()
    {
        onView(withId(R.id.mainND))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(1, click()));

        intended(hasExtraWithKey("position"));
    }
}
