package com.example.coyg.bakingapp.stepsDetials;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.coyg.bakingapp.main.MainAdapter;
import com.example.coyg.bakingapp.recipeDetails.steps.StepsAdapter;
import com.example.coyg.bakingapp.recipeDetails.steps.StepsItem;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class StepsDetails2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , StepsAdapter.onItemClickListener
{
    private StepDetailsItems stepDetailsItems;
    private List<StepsItem> listItemsSteps = new ArrayList<> ();
    public RecyclerView.Adapter stepsAdapter;
    private RecyclerView steps;

    private int position, index, length;
    TextView dec;
    SimpleExoPlayerView simpleExoPlayerView;
    ProgressBar loading;
    SimpleExoPlayer player;
    Button next;
    Button prev;
    NavigationView navigationView;
    DrawerLayout drawer;

    public static final String VIDEO_POSITION = "VideoPosition";
    public static final String PLAY_WHEN_READY = "playWhenReady";
    public static final String VIDEO_URL = "VideoURL";
    public static final String POSITION = "position";

    private static long videoPosition = -1;
    String VideoURL;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_steps_details2);
        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);
        steps = findViewById (R.id.steps2);

        simpleExoPlayerView = findViewById (R.id.simpleExoPlayerView);
        loading = findViewById(R.id.loading);
        dec = findViewById (R.id.dec);
        next = findViewById (R.id.nxt);
        prev = findViewById (R.id.prev);


        if (savedInstanceState != null)
        {
            Long savedVideoPosition = savedInstanceState.getLong(VIDEO_POSITION, 0);
            position = savedInstanceState.getInt(POSITION);

            Boolean playWhenReady = savedInstanceState.getBoolean (PLAY_WHEN_READY, true);

            String URL = savedInstanceState.getString (VIDEO_URL, "");

            setExoPLayer ();
            player.seekTo (savedVideoPosition);
            player.setPlayWhenReady (playWhenReady);
            setData (savedVideoPosition, position);
        }


        Bundle data = getIntent().getExtras();
        if(data==null)
            return;
        position = data.getInt ("position");
        index = data.getInt ("index");
        length = data.getInt ("length");

        if(getSupportActionBar () != null)
            getSupportActionBar ().setTitle ("Steps "+position);

        steps.setHasFixedSize (true);
        LinearLayoutManager layoutManagerSteps = new LinearLayoutManager
                (this,LinearLayoutManager.VERTICAL, false);
        steps.setLayoutManager(layoutManagerSteps);
        stepsAdapter = new StepsAdapter (listItemsSteps , StepsDetails2.this, this);
        steps.setAdapter (stepsAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        steps.addItemDecoration(decoration);

        loadRecyclerViewDataSteps();
        loadRecyclerViewData();
        setExoPLayer ();

        if(prev != null && position == 0)
        {
            prev.setVisibility (View.GONE);
        }

        if(next != null && position == length)
        {
            next.setVisibility (View.GONE);
        }

        drawer = (DrawerLayout) findViewById (R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener (toggle);
        toggle.syncState ();

        navigationView = (NavigationView) findViewById (R.id.nav_view);
        navigationView.setNavigationItemSelectedListener (this);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout);
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

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putLong(VIDEO_POSITION, videoPosition);
        outState.putInt (POSITION, position);
        outState.putString (VIDEO_URL, VideoURL);
        outState.putBoolean (PLAY_WHEN_READY, true);
    }

    private void loadRecyclerViewDataSteps()
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
                            JSONObject o = jsonArray.getJSONObject(index);
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
                            Toast.makeText (StepsDetails2.this,"steps"+e.toString (),Toast.LENGTH_LONG).show ();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText (StepsDetails2.this,"steps2"+error.getMessage (),Toast.LENGTH_LONG).show ();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(StepsDetails2.this);
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
                            JSONObject o = jsonArray.getJSONObject(index);
                            JSONArray jsonArrayING = o.getJSONArray ("steps");

                            JSONObject o2 = jsonArrayING.getJSONObject(position);
                            dec.setText (o2.getString ("description"));

                            stepDetailsItems = new StepDetailsItems
                                    (o2.getString ("description"), o2.getString ("videoURL"));


//                            if(o2.getString ("videoURL").equals (""))
//                            {
//                                simpleExoPlayerView.setVisibility (View.GONE);
//                            }
//                            else
//                            {
//                                simpleExoPlayerView.setVisibility (View.VISIBLE);
//                                setExoPLayer (o2.getString ("videoURL"));
//                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText (StepsDetails2.this,"data"+e.toString (),Toast.LENGTH_LONG).show ();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText (StepsDetails2.this,"data2"+error.getMessage (),Toast.LENGTH_LONG).show ();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(StepsDetails2.this);
        requestQueue.add(stringRequest);
    }

    private void setExoPLayer()
    {
        if (player != null) return;

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(StepsDetails2.this, trackSelector, loadControl);
        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.requestFocus();

        if (videoPosition != -1)
            player.seekTo(videoPosition);

        simpleExoPlayerView.setPlayer(player);
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

        DefaultDataSourceFactory defaultDataSourceFactory =
                new DefaultDataSourceFactory
                        (StepsDetails2.this, Util.getUserAgent(StepsDetails2.this, getString(R.string.app_name))
                                , defaultBandwidthMeter);


        simpleExoPlayerView.setVisibility(View.VISIBLE);
        String url = stepDetailsItems.getVideoURL ();
        Uri videoUri = Uri.parse(url);

        MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                defaultDataSourceFactory,
                new DefaultExtractorsFactory (),
                null,
                null
        );

        player.prepare(mediaSource);
        player.addListener(new Player.EventListener()
        {

            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason)
            {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups,
                                        TrackSelectionArray trackSelections
            )
            {

            }

            @Override
            public void onLoadingChanged(boolean isLoading)
            {
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState)
            {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode)
            {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled)
            {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error)
            {
                player.stop();
                player.setPlayWhenReady(true);

            }

            @Override
            public void onPositionDiscontinuity(int reason)
            {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters)
            {

            }

            @Override
            public void onSeekProcessed()
            {

            }
        });
        player.setPlayWhenReady(true);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(VideoURL != null)
            setExoPLayer ();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        setExoPLayer ();
        releasePlayer();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        releasePlayer();

    }

    private void releasePlayer()
    {
        if (player != null)
        {
            player.stop();
            videoPosition = player.getCurrentPosition();
            player.release();
            player = null;
        }
    }

    public void next_fun(View view)
    {
        videoPosition = 0;

        if(position<length)
        {
            if(prev != null && position == 0)
            {
                prev.setVisibility (View.VISIBLE);
            }
            position++;
            loadRecyclerViewData();
            getSupportActionBar ().setTitle ("Steps "+position);
        }
        else
        {
            if(next != null && position == listItemsSteps.size ())
            {
                next.setVisibility (View.GONE);
            }
            Toast.makeText (StepsDetails2.this, "THE END", Toast.LENGTH_LONG).show ();
        }
    }

    public void prev_fun(View view)
    {
        videoPosition = 0;

        if(position>0)
        {
            if(next != null && position == listItemsSteps.size ())
            {
                next.setVisibility (View.VISIBLE);
            }
            position--;
            loadRecyclerViewData();
            getSupportActionBar ().setTitle ("Steps "+position);
        }
        else
        {
            if(prev != null && position == 0)
            {
                prev.setVisibility (View.GONE);
            }
            Toast.makeText (StepsDetails2.this, "THE START", Toast.LENGTH_LONG).show ();
        }
    }


    public void setData(Long videoPosition, int position)
    {
        this.videoPosition = videoPosition;
        this.position = position;
    }

    @Override
    public void onItemClickActionListener(int position)
    {
        if (position != length-1)
        {
            next.setVisibility (View.VISIBLE);
        }
        else
        {
            next.setVisibility (View.GONE);
        }

        if(position == 0)
        {
            prev.setVisibility (View.GONE);
        }
        else
        {
            prev.setVisibility (View.VISIBLE);
        }

        this.position = position;
        loadRecyclerViewData();
        drawer.closeDrawers ();
    }
}
