package com.example.coyg.bakingapp;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static java.util.regex.Pattern.matches;
import static org.hamcrest.Matchers.anything;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.coyg.bakingapp.main.Main2Activity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest
{
    @Rule
    public ActivityTestRule<Main2Activity> main2ActivityActivityTestRule =
            new ActivityTestRule<>(Main2Activity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource()
    {
        idlingResource = main2ActivityActivityTestRule
                .getActivity().getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void clickRecyclerViewItem_OpensActivity()
    {
        onView(withId(R.id.mainND)).perform(RecyclerViewActions
                        .actionOnItemAtPosition(1, click()));

        onData(anything()).inAdapterView(withId(R.id.steps_rvf))
                .atPosition(0).perform(click());

    }

}
