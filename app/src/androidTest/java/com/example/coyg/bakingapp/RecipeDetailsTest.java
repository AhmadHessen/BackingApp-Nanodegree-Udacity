package com.example.coyg.bakingapp;



import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import com.example.coyg.bakingapp.main.Main2Activity;
import com.example.coyg.bakingapp.recipeDetails.RecipeDetails;
import com.example.coyg.bakingapp.step_details.RecipeActivityHaveFragments;
import com.example.coyg.bakingapp.step_details.RecipeDetailsFragment;

import static org.hamcrest.Matchers.anything;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
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
public class RecipeDetailsTest
{
    @Rule
    public IntentsTestRule<RecipeActivityHaveFragments> mActivityRule
            = new IntentsTestRule<> (RecipeActivityHaveFragments.class);

    @Test
    public void onClickRVI()
    {
        onView(withId(R.id.ingredients_rvf))
                .perform(RecyclerViewActions.actionOnItemAtPosition
                        (1, click()));
    }

    @Test
    public void onClickRVS()
    {
        onView(withId(R.id.steps_rvf))
                .perform(RecyclerViewActions.actionOnItemAtPosition
                        (1, click()));
    }

    @Test
    public void checkTextDisplayedInDynamicallyCreatedFragment()
    {
        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();

        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_details_framelayout, recipeDetailsFragment).commit();
    }
}
