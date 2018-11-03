package com.example.coyg.bakingapp.step_details;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class StepDetailsFragment extends Fragment
{
    public List<StepsItem> listItemsSteps = new ArrayList<> ();;
    int position, index, length;
    boolean isTablet;
    String title;
    TextView decView;
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer player;
    Button next;
    Button prev;
    boolean playWhenReady = true;
    Uri videoUri;

    public static final String VIDEO_POSITION = "VideoPosition";
    public static final String PLAY_WHEN_READY = "playWhenReady";
    public static final String VIDEO_URL = "VideoURL";
    public static final String POSITION = "position";
    public static final String DEC = "dec";

    private static long videoPosition = -1;
    String VideoURL;
    String dec;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);

        simpleExoPlayerView = view.findViewById (R.id.simpleExoPlayerView_fragment);
        decView = view.findViewById (R.id.dec_fragment);
        next = view.findViewById (R.id.nxt_fragment);
        prev = view.findViewById (R.id.prev_fragment);

        if (savedInstanceState != null)
        {
            videoPosition = savedInstanceState.getLong(VIDEO_POSITION);
            position = savedInstanceState.getInt(POSITION);

            playWhenReady = savedInstanceState.getBoolean (PLAY_WHEN_READY);

            VideoURL = savedInstanceState.getString (VIDEO_URL);

            dec = savedInstanceState.getString (DEC);

            setExoPLayer ();
            player.seekTo(videoPosition);
            player.setPlayWhenReady(playWhenReady);

            setPosition (videoPosition, playWhenReady);
        }

        Bundle data = getActivity ().getIntent().getExtras();
        if (data != null)
        {
            index = data.getInt ("position");
            title = data.getString ("title");
        }

        if(savedInstanceState == null && listItemsSteps.get (position) != null)
        {
            dec = listItemsSteps.get (position).getDec ();
            VideoURL = listItemsSteps.get (position).getVideoURL ();
        }

        decView.setText (dec);
        setExoPLayer ();
        setButtonsVisibility();

        prev.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View view)
            {
                prev_fun ();
            }
        });

        next.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View view)
            {
                next_fun ();
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putLong(VIDEO_POSITION, videoPosition);
        outState.putInt (POSITION, position);
        outState.putString (VIDEO_URL, VideoURL);
        outState.putBoolean (PLAY_WHEN_READY, playWhenReady);
        outState.putString (DEC, dec);
    }

    private void setExoPLayer()
    {
        if(player != null) return;
        TrackSelector trackSelector = new DefaultTrackSelector ();
        LoadControl loadControl = new DefaultLoadControl ();

        player = ExoPlayerFactory.newSimpleInstance(getActivity (), trackSelector, loadControl);
        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.requestFocus();

        if(videoPosition != -1) player.seekTo(videoPosition);

        simpleExoPlayerView.setPlayer(player);
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

        DefaultDataSourceFactory defaultDataSourceFactory =
                new DefaultDataSourceFactory
                        (getActivity (), Util.getUserAgent(getActivity (), getString(R.string.app_name))
                                , defaultBandwidthMeter);

        simpleExoPlayerView.setVisibility(View.VISIBLE);

        String uri = VideoURL;

        if(uri.equals (""))
        {
            simpleExoPlayerView.setVisibility (View.GONE);
            return;
        }

        videoUri = Uri.parse(uri);

        MediaSource mediaSource = new ExtractorMediaSource (videoUri,
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
                player.setPlayWhenReady(playWhenReady);

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
        player.setPlayWhenReady(playWhenReady);
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

    void setButtonsVisibility()
    {
        if(isTablet)
        {
            next.setVisibility (View.GONE);
            prev.setVisibility (View.GONE);
        }
        else if(position == 1)
        {
            next.setVisibility (View.VISIBLE);
            prev.setVisibility (View.GONE);
        }
        else if(position == length-2)
        {
            next.setVisibility (View.GONE);
            prev.setVisibility (View.VISIBLE);
        }
        else
        {
            next.setVisibility (View.VISIBLE);
            prev.setVisibility (View.VISIBLE);
        }
    }

    public void next_fun()
    {
        releasePlayer ();
        videoPosition = -1;

        if(position<length-1)
        {
            prev.setVisibility (View.VISIBLE);
            ++position;
            dec = listItemsSteps.get (position).getDec ();
            VideoURL = listItemsSteps.get (position).getVideoURL ();
            decView.setText (dec);
            setExoPLayer ();
        }
        else
        {
            next.setVisibility (View.GONE);
            Toast.makeText (getActivity (), "THE END", Toast.LENGTH_LONG).show ();
        }
    }

    public void prev_fun()
    {
        releasePlayer ();
        videoPosition = -1;

        if(position>0)
        {
            next.setVisibility (View.VISIBLE);
            --position;
            dec = listItemsSteps.get (position).getDec ();
            VideoURL = listItemsSteps.get (position).getVideoURL ();
            decView.setText (dec);
            setExoPLayer ();
        }
        else
        {
            prev.setVisibility (View.GONE);
            Toast.makeText (getActivity (), "THE START", Toast.LENGTH_LONG).show ();
        }
    }

    void setPosition(long videoPosition, boolean playWhenReady)
    {
        this.videoPosition = videoPosition;
        this.playWhenReady = playWhenReady;
    }

    public void setList(List<StepsItem> listItemsSteps)
    {
        this.listItemsSteps = listItemsSteps;
    }

    public void setData(int position, int length, String videoURL, String dec, boolean isTablet)
    {
        this.position = position;
        this.length = length;
        this.dec = dec;
        this.VideoURL = videoURL;
        this.isTablet = isTablet;
    }
}
