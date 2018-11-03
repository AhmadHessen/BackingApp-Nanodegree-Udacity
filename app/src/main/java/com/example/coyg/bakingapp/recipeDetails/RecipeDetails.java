package com.example.coyg.bakingapp.recipeDetails;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coyg.bakingapp.R;
import com.example.coyg.bakingapp.homscreen_wedget.WidgetProvider;
import com.example.coyg.bakingapp.main.MainIdlingResource;
import com.example.coyg.bakingapp.recipeDetails.ingredients.IngredientsAdapter;
import com.example.coyg.bakingapp.recipeDetails.ingredients.IngredientsItem;
import com.example.coyg.bakingapp.recipeDetails.steps.StepsAdapter;
import com.example.coyg.bakingapp.recipeDetails.steps.StepsItem;
import com.example.coyg.bakingapp.stepsDetials.StepsDetails2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class RecipeDetails extends AppCompatActivity implements StepsAdapter.onItemClickListener
{
    private RecyclerView ingredients;
    private RecyclerView steps;
    private int position;
    private String title;
    private List<IngredientsItem> listItemsING = new ArrayList<> ();
    public RecyclerView.Adapter ingredientsAdapter;

    private List<StepsItem> listItemsSteps = new ArrayList<> ();
    public RecyclerView.Adapter stepsAdapter;

    private MainIdlingResource mainIdlingResource = new MainIdlingResource ();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_recipe_details);
        ingredients = findViewById (R.id.ingredients);
        steps = findViewById (R.id.steps);

        Bundle data = getIntent().getExtras();
        if(data==null)
            return;
        position = data.getInt ("position");
        title = data.getString ("title");

        getSupportActionBar ().setTitle (title);

        ingredients.setHasFixedSize (true);
        LinearLayoutManager layoutManagerCrew = new LinearLayoutManager
                (this,LinearLayoutManager.VERTICAL, false);
        ingredients.setLayoutManager(layoutManagerCrew);
        ingredientsAdapter = new IngredientsAdapter (listItemsING , RecipeDetails.this);
        ingredients.setAdapter (ingredientsAdapter);
        loadRecyclerViewDataING(position);


        steps.setHasFixedSize (true);
        LinearLayoutManager layoutManagerSteps = new LinearLayoutManager
                (this,LinearLayoutManager.VERTICAL, false);
        steps.setLayoutManager(layoutManagerSteps);
        stepsAdapter = new StepsAdapter (listItemsSteps , RecipeDetails.this,this);
        steps.setAdapter (stepsAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        steps.addItemDecoration(decoration);

        loadRecyclerViewDataSteps(position);

        SharedPreferences sharedPreferences = getSharedPreferences
                ("shared_preferences", MODE_PRIVATE);
        sharedPreferences.edit ().putString
                ("recipe_name", title+ " ingredients")
                .apply ();
    }
    private void loadRecyclerViewDataSteps(final int position)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                getString (R.string.data_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject o = jsonArray.getJSONObject(position);
                            JSONArray jsonArrayING = o.getJSONArray ("steps");
                            for (int i=0 ; i<jsonArrayING.length() ; i++)
                            {
                                JSONObject o2 = jsonArrayING.getJSONObject(i);
                                StepsItem stepsItem = new StepsItem
                                        (o2.getString ("description"), o2.getString ("shortDescription"),
                                                o2.getString ("videoURL"),position);
                                listItemsSteps.add(stepsItem);
                            }
                            stepsAdapter.notifyDataSetChanged ();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText (RecipeDetails.this,e.toString (),Toast.LENGTH_LONG).show ();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText (RecipeDetails.this,error.getMessage (),Toast.LENGTH_LONG).show ();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(RecipeDetails.this);
        requestQueue.add(stringRequest);
    }

    private void loadRecyclerViewDataING(final int position)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                getString (R.string.data_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject o = jsonArray.getJSONObject(position);
                            JSONArray jsonArrayING = o.getJSONArray ("ingredients");
                            for (int i=0 ; i<jsonArrayING.length() ; i++)
                            {
                                JSONObject o2 = jsonArrayING.getJSONObject(i);
                                IngredientsItem ingredientsItem = new IngredientsItem
                                        (o2.getString ("quantity"), o2.getString ("measure")
                                                , o2.getString ("ingredient"));

                                ContentValues contentValues = new ContentValues ();
                                contentValues.put (WidgetProvider.INGREDIENT_COLUMN,
                                        o2.getString ("ingredient"));
                                contentValues.put (WidgetProvider.QUANTITY_COLUMN,
                                        o2.getString ("quantity"));
                                contentValues.put (WidgetProvider.MEASURE_COLUMN,
                                        o2.getString ("measure"));


                                Uri uri = getContentResolver ().insert
                                        (WidgetProvider.URI, contentValues);

                                listItemsING.add(ingredientsItem);
                            }
                            ingredientsAdapter.notifyDataSetChanged ();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText (RecipeDetails.this,e.toString (),Toast.LENGTH_LONG).show ();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText (RecipeDetails.this,error.getMessage (),Toast.LENGTH_LONG).show ();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(RecipeDetails.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemClickActionListener(int position)
    {
        StepsItem stepsItem = listItemsSteps.get (position);

        Intent intent = new Intent (RecipeDetails.this, StepsDetails2.class);

        intent.putExtra ("position", position);
        intent.putExtra ("index", stepsItem.getIndex ());
        intent.putExtra ("length", stepsAdapter.getItemCount ());

        startActivity (intent);
    }
}
