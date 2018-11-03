package com.example.coyg.bakingapp.step_details;

import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coyg.bakingapp.R;
import com.example.coyg.bakingapp.recipeDetails.steps.StepsItem;
import com.example.coyg.bakingapp.stepsDetials.StepDetailsItems;
import com.example.coyg.bakingapp.stepsDetials.StepsDetails2;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivityHaveFragments extends AppCompatActivity
        implements RecipeDetailsFragment.OnFragmentInteractionListener
{
    boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);

        if(getResources ().getBoolean (R.bool.landscape_only))
        {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isTablet=true;
        }

        setContentView (R.layout.activity_recipe_have_fragments);

        if(findViewById (R.id.step_details_framelayout) != null) isTablet=true;

        if(savedInstanceState == null)
        {
            RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment ();
            ChatngeToRecipeDetails(recipeDetailsFragment);
        }
    }

    void ChatngeToRecipeDetails(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_details_framelayout, fragment)
                .commit();
    }

    void ChatngeToStepDetails(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_details_framelayout, fragment)
                .commit();
    }

    @Override
    public void onFragmentInteraction(int position, int length, String videoURL, String dec, List<StepsItem> listItemsSteps )
    {
        if(isTablet)
        {
            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment ();
            stepDetailsFragment.setData (position, length, videoURL, dec, isTablet);
            stepDetailsFragment.setList (listItemsSteps);
            ChatngeToStepDetails(stepDetailsFragment);
        }
        else
        {
            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment ();
            stepDetailsFragment.setData (position, length, videoURL, dec, isTablet);
            stepDetailsFragment.setList (listItemsSteps);
            ChatngeToRecipeDetails(stepDetailsFragment);
        }
    }
}
