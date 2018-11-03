package com.example.coyg.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static java.util.regex.Pattern.matches;
import static org.hamcrest.Matchers.anything;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.coyg.bakingapp.main.Main2Activity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class IdlingResourcesMainActivityTest
{
    @Rule
    public ActivityTestRule<Main2Activity> activityActivityTestRule
            = new ActivityTestRule<> (Main2Activity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource()
    {
        idlingResource = activityActivityTestRule.getActivity ().getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }


    @Test
    public void idlingResourceTest()
    {
        onData(anything())
                .inAdapterView(withId(R.id.mainRV))
                .atPosition(0).perform(click());
    }

    @Test
    public void onMainActivity_displayRV()
    {
        onView (withId(R.id.mainND))
                .check(matches(isDisplayed()));

    }

    @Test
    public void clickRecipeListItem_openRecipeDetailsActivity()
    {

        onView(withId(R.id.mainND))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(1, click()));


        onView(withId(R.id.ingredients_rvf))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource()
    {
        if (idlingResource != null)
            Espresso.unregisterIdlingResources(idlingResource);
    }
}
