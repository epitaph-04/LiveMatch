package com.mahbub.hlsplayerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.Util;

public class HlsView extends FrameLayout {

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SimpleExoPlayer player;

    private PlayerView playerView;
    private ProgressBar loading;

    private String hlsSource;

    public HlsView(Context context) {
        this(context, null);
    }

    public HlsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HlsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.hlsplayer_view,this);
        playerView = findViewById(R.id.player_view);
        loading = findViewById(R.id.videoLoading);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.hlsPlayer);
        SetHlsSource(attributes.getString(R.styleable.hlsPlayer_hlsSource));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initializePlayer(getContext());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releasePlayer();
    }

    public void SetHlsSource(String source) {
        hlsSource = source;
    }

    private void initializePlayer(Context mContext) {
        if(player == null) {
            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory adaptiveTrackSelection = new AdaptiveTrackSelection.Factory(
                    defaultBandwidthMeter,
                    10000,
                    25000,
                    25000,
                    .1f,
                    .1f,
                    100000L,
                    Clock.DEFAULT
            );

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    mContext,
                    Util.getUserAgent(mContext, "Exo2"),
                    defaultBandwidthMeter);
            Uri uri = Uri.parse(hlsSource);
            MediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory).setAllowChunklessPreparation(true).createMediaSource(uri);

            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(mContext),
                    new DefaultTrackSelector(adaptiveTrackSelection),
                    new DefaultLoadControl());

            playerView.setPlayer(player);
            player.prepare(mediaSource);
            player.setPlayWhenReady(playWhenReady);
            player.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                }

                @Override
                public void onLoadingChanged(boolean isLoading) {
                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {
                        case ExoPlayer.STATE_READY:
                            loading.setVisibility(View.GONE);
                            break;
                        case ExoPlayer.STATE_BUFFERING:
                            loading.setVisibility(View.VISIBLE);
                            break;
                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {
                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                }

                @Override
                public void onPositionDiscontinuity(int reason) {
                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                }

                @Override
                public void onSeekProcessed() {
                }
            });
            player.seekTo(currentWindow, playbackPosition);
            player.prepare(mediaSource, true, false);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }
}

