package com.example.coyg.bakingapp.main;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coyg.bakingapp.R;
import com.example.coyg.bakingapp.homscreen_wedget.WidgetProvider;
import com.example.coyg.bakingapp.recipeDetails.RecipeDetails;
import com.example.coyg.bakingapp.recipeDetails.ingredients.IngredientsItem;
import com.example.coyg.bakingapp.step_details.RecipeActivityHaveFragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainAdapter.onItemClickListener
{

    private RecyclerView recyclerView2;
    public RecyclerView.Adapter adapter2;
    private LinearLayoutManager linearLayoutManager2;
    public List<ListItem> listItems2 = new ArrayList<> ();

    private RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    private GridLayoutManager gridLayoutManager;
    public List<ListItem> listItems = new ArrayList<> ();

    public List<IngredientsItem> ING_listItems = new ArrayList<> ();

    @Nullable
    private MainIdlingResource mainIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);

        if(getResources ().getBoolean (R.bool.landscape_only))
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView (R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);

        recyclerView = findViewById(R.id.mainRV);
        adapter = new MainAdapter (listItems,Main2Activity.this, this);
        recyclerView.setAdapter (adapter);
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5)
        {
            gridLayoutManager = new GridLayoutManager (Main2Activity.this ,3) ;
        }
        else {
            gridLayoutManager = new GridLayoutManager (Main2Activity.this ,1) ;
        }

        recyclerView.setLayoutManager(gridLayoutManager);
        loadRecyclerViewData();

        recyclerView2 = findViewById(R.id.mainND);
        adapter2 = new MainAdapter (listItems2,Main2Activity.this, this);
        recyclerView2.setAdapter (adapter2);
        linearLayoutManager2 = new LinearLayoutManager (Main2Activity.this, LinearLayoutManager.VERTICAL,false);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);

        recyclerView.addItemDecoration(decoration);
        recyclerView2.addItemDecoration(decoration);


        DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener (toggle);
        toggle.syncState ();

        NavigationView navigationView = (NavigationView) findViewById (R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener (this);
        loadRecyclerViewData2();

        getIdlingResource();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout2);
        if (drawer.isDrawerOpen (GravityCompat.START))
        {
            drawer.closeDrawer (GravityCompat.START);
        } else
        {
            super.onBackPressed ();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId ();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected (item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId ();

        DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout);
        drawer.closeDrawer (GravityCompat.START);
        return true;
    }

    private void loadRecyclerViewData2()
    {
        if (mainIdlingResource != null)
            mainIdlingResource.setIdle (false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                getString (R.string.data_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            if (mainIdlingResource != null)
                                mainIdlingResource.setIdle (true);

                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0 ; i<jsonArray.length() ; i++)
                            {
                                JSONObject o = jsonArray.getJSONObject(i);
                                ListItem item = new ListItem (o.getString ("name"), o.getString ("id"));
                                listItems2.add(item);
                            }
                            adapter2.notifyDataSetChanged ();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText (Main2Activity.this,error.getMessage (),Toast.LENGTH_LONG).show ();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(Main2Activity.this);
        requestQueue.add(stringRequest);
    }

    private void loadRecyclerViewData()
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
                            for (int i=0 ; i<jsonArray.length() ; i++)
                            {
                                JSONObject o = jsonArray.getJSONObject(i);
                                ListItem item = new ListItem (o.getString ("name"), o.getString ("id"));
                                listItems.add(item);
                            }
                            adapter.notifyDataSetChanged ();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText (Main2Activity.this,error.getMessage (),Toast.LENGTH_LONG).show ();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(Main2Activity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemClickActionListener(int position)
    {
        Intent intent = new Intent (Main2Activity.this, RecipeActivityHaveFragments.class);

        intent.putExtra ("position",position);
        intent.putExtra ("title",listItems.get (position).getRecipe_name ());

        startActivity(intent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource()
    {
        if(mainIdlingResource == null)
            mainIdlingResource = new MainIdlingResource ();
        return mainIdlingResource;
    }
}
