package com.example.coyg.bakingapp.stepsDetials;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.os.Handler;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coyg.bakingapp.R;
import com.example.coyg.bakingapp.recipeDetails.RecipeDetails;
import com.example.coyg.bakingapp.recipeDetails.ingredients.IngredientsItem;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StepsDetalis extends AppCompatActivity
{
    private int position, index, length;
    TextView dec;
    PlayerView playerView;
    ProgressBar loading;
    SimpleExoPlayer player;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_steps_detalis);
        //playerView = findViewById (R.id.playerView);
        loading = findViewById(R.id.loading);

        Bundle data = getIntent().getExtras();
        if(data==null)
            return;
        position = data.getInt ("position");
        index = data.getInt ("index");
        length = data.getInt ("length");
    }

    private void loadRecyclerViewData(final int position)
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
                            initializePlayer(o2.getString ("videoURL"));
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText (StepsDetalis.this,e.toString (),Toast.LENGTH_LONG).show ();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText (StepsDetalis.this,error.getMessage (),Toast.LENGTH_LONG).show ();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(StepsDetalis.this);
        requestQueue.add(stringRequest);
    }

    private void initializePlayer(String video_url)
    {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(video_url);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri)
    {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory ("-")).
                createMediaSource(uri);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (Util.SDK_INT > 23)
        {
            loadRecyclerViewData(position);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null))
        {
            loadRecyclerViewData(position);
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi()
    {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (Util.SDK_INT <= 23)
        {
            releasePlayer();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (Util.SDK_INT > 23)
        {
            releasePlayer();
        }
    }

    private void releasePlayer()
    {
        if (player != null)
        {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    public void next_fun(View view)
    {
        if(position<length)
        {
            loadRecyclerViewData(position++);
        }
        else
        {
            Toast.makeText (StepsDetalis.this, "THE END", Toast.LENGTH_LONG).show ();
        }
    }

    public void prev_fun(View view)
    {
        if(position>0)
        {
            loadRecyclerViewData(position--);
        }
        else
        {
            Toast.makeText (StepsDetalis.this, "THE START", Toast.LENGTH_LONG).show ();
        }
    }
}
