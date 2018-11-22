package com.hls.exo.hlsplayer;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hls.exo.hlsplayer.LinkProvider.MatchInfo;
import com.hls.exo.hlsplayer.Views.HlsView;
import com.hls.exo.hlsplayer.Views.IHlsFullScreenClickListener;
import com.hls.exo.hlsplayer.Views.MatchLinkView;

import java.lang.reflect.Type;
import java.util.List;

public class ViewListActivity extends AppCompatActivity implements ILinkInteractionListener {

    private String currentUrl;
    private MatchLinkView matchLinkView;
    private HlsView hlsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Title");

        matchLinkView = findViewById(R.id.linkviewid);
        hlsView = findViewById(R.id.hlsviewid);
        hlsView.addFullScreenClickListener(new IHlsFullScreenClickListener() {
            @Override
            public void OnFullscreenButtonClick(Boolean fullScreen) {
                Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                intent.putExtra("Url", currentUrl);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        currentUrl = intent.getStringExtra("Url");

        Gson gson = new Gson();
        Type type = new TypeToken<List<MatchInfo>>(){}.getType();
        List<MatchInfo> data = gson.fromJson(intent.getStringExtra("data"), type);

        matchLinkView.setLinkList(data);
        matchLinkView.addLinkInteractionListener(this);
        hlsView.SetHlsSource(currentUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hlsView.SetFullScreen(false);
    }

    @Override
    public void onClick(String url) {
        hlsView.SetHlsSource(url);
    }
}
