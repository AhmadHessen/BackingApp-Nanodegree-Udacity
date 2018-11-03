package com.example.coyg.bakingapp;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import com.example.coyg.bakingapp.main.Main2Activity;
import com.example.coyg.bakingapp.recipeDetails.RecipeDetails;
import com.example.coyg.bakingapp.stepsDetials.StepsDetails2;

import static org.hamcrest.Matchers.anything;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class StepDetailsTest
{

    @Rule
    public IntentsTestRule<StepsDetails2> mActivityRule = new IntentsTestRule<> (StepsDetails2.class);

    @Test
    public void clickMainListItem_OpenSteps()
    {
        onView(withId(R.id.nxt)).perform(click());
        onView(withId(R.id.prev)).perform(click());
    }
}
