package com.hls.exo.hlsplayer.Activities;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hls.exo.hlsplayer.R;
import com.hls.exo.hlsplayer.Views.HlsView.HlsView;
import com.hls.exo.hlsplayer.Views.HlsView.IHlsFullScreenClickListener;

public class MatchActivity extends AppCompatActivity {

    private HlsView mHlsView;

    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match);

        mVisible = true;
        mHlsView = findViewById(R.id.fullscreenhlsviewid);
        mHlsView.addFullScreenClickListener(new IHlsFullScreenClickListener() {
            @Override
            public void OnFullscreenButtonClick(Boolean fullScreen) {
                MatchActivity.this.onBackPressed();
            }
        });

        mHlsView.startPlayer(getIntent().getStringExtra("Url"));
        toggle();
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHlsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        mVisible = false;
    }

    @SuppressLint("InlinedApi")
    private void show() {
        mHlsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
    }
}
