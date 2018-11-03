package com.example.coyg.bakingapp.step_details;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.coyg.bakingapp.recipeDetails.RecipeDetails;
import com.example.coyg.bakingapp.recipeDetails.ingredients.IngredientsAdapter;
import com.example.coyg.bakingapp.recipeDetails.ingredients.IngredientsItem;
import com.example.coyg.bakingapp.recipeDetails.steps.StepsAdapter;
import com.example.coyg.bakingapp.recipeDetails.steps.StepsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


public class RecipeDetailsFragment extends Fragment implements StepsAdapter.onItemClickListener
{

    private RecyclerView ingredients;
    private RecyclerView steps;
    private int position;
    private String title;
    private List<IngredientsItem> listItemsING = new ArrayList<> ();
    public RecyclerView.Adapter ingredientsAdapter;

    public List<StepsItem> listItemsSteps = new ArrayList<> ();
    public RecyclerView.Adapter stepsAdapter;
    StepsItem stepsItem;

    private OnFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        ingredients = view.findViewById (R.id.ingredients_rvf);
        steps = view.findViewById (R.id.steps_rvf);

        Bundle data = getActivity ().getIntent().getExtras();

        if (data != null)
        {
            position = data.getInt ("position");
            title = data.getString ("title");
        }


        ingredients.setHasFixedSize (true);
        LinearLayoutManager layoutManagerCrew = new LinearLayoutManager
                (getActivity (),LinearLayoutManager.VERTICAL, false);
        ingredients.setLayoutManager(layoutManagerCrew);
        ingredientsAdapter = new IngredientsAdapter (listItemsING , getActivity ());
        ingredients.setAdapter (ingredientsAdapter);
        loadRecyclerViewDataING(position);


        steps.setHasFixedSize (true);
        LinearLayoutManager layoutManagerSteps = new LinearLayoutManager
                (getActivity (),LinearLayoutManager.VERTICAL, false);

        steps.setLayoutManager(layoutManagerSteps);
        stepsAdapter = new StepsAdapter (listItemsSteps , getActivity (),this);
        steps.setAdapter (stepsAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getActivity (), VERTICAL);
        steps.addItemDecoration(decoration);

        loadRecyclerViewDataSteps(position);

        SharedPreferences sharedPreferences = getActivity ().getSharedPreferences
                ("shared_preferences", Context.MODE_PRIVATE);
        sharedPreferences.edit ().putString
                ("recipe_name", title+ " ingredients")
                .apply ();

        return view;
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
                                stepsItem = new StepsItem
                                        (o2.getString ("description"), o2.getString ("shortDescription"),
                                                o2.getString ("videoURL"),position);

                                listItemsSteps.add(stepsItem);
                            }
                            stepsAdapter.notifyDataSetChanged ();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText (getActivity (),e.toString (),Toast.LENGTH_LONG).show ();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText (getActivity (),error.getMessage (),Toast.LENGTH_LONG).show ();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity ());
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

                                listItemsING.add(ingredientsItem);
                            }
                            ingredientsAdapter.notifyDataSetChanged ();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText (getActivity (),e.toString (),Toast.LENGTH_LONG).show ();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText (getActivity (),error.getMessage (),Toast.LENGTH_LONG).show ();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity ());
        requestQueue.add(stringRequest);
    }



    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
            {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {

        void onFragmentInteraction(int position, int length, String videoURL, String dec, List<StepsItem> listItemsSteps );
    }

    @Override
    public void onItemClickActionListener(int position)
    {
        mListener.onFragmentInteraction
                (position, stepsAdapter.getItemCount (), listItemsSteps.get (position).getVideoURL ()
                        , listItemsSteps.get (position).getDec (), listItemsSteps );
    }
}
