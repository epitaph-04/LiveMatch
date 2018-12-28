package com.hls.exo.hlsplayer.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hls.exo.hlsplayer.Models.MatchInfo;
import com.hls.exo.hlsplayer.Models.MatchLink;
import com.hls.exo.hlsplayer.Models.MatchLinkInfo;
import com.hls.exo.hlsplayer.R;
import com.hls.exo.hlsplayer.Views.HlsView.HlsView;
import com.hls.exo.hlsplayer.Views.HlsView.IHlsFullScreenClickListener;
import com.hls.exo.hlsplayer.Views.InfoView.IMatchInfoInteractionListener;
import com.hls.exo.hlsplayer.Views.InfoView.MatchInfoView;
import com.hls.exo.hlsplayer.Views.LinkView.IMatchLinkInteractionListener;
import com.hls.exo.hlsplayer.Views.LinkView.MatchLinkView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailsMatchActivity extends AppCompatActivity
        implements IMatchInfoInteractionListener, IMatchLinkInteractionListener {

    private MatchInfo currentMatchInfo;
    private String url;
    private MatchInfoView matchInfoView;
    private MatchLinkView matchLinkView;
    private HlsView hlsView;
    private ActionBar actionBar;

    private FrameLayout detail_thumbnail_root_layout;
    private ImageView detail_thumbnail_play_button;
    private ImageView detail_thumbnail_image_view;
    private TextView detail_video_title_view;
    private ImageView detail_toggle_link_view;
    private ProgressBar loading_progress_bar;
    private LinearLayout detail_content_root_hiding;
    private TextView detail_language_text_view;
    private TextView detail_view_quality_view;
    private FrameLayout detail_title_root_layout;

    private final String imageUrl = "http://87.210.113.95:60000/StaticFiles/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_match_layout);

        actionBar = getSupportActionBar();

        detail_thumbnail_root_layout = findViewById(R.id.detail_thumbnail_root_layout);
        detail_thumbnail_play_button = findViewById(R.id.detail_thumbnail_play_button);
        detail_thumbnail_image_view = findViewById(R.id.detail_thumbnail_image_view);
        matchInfoView = findViewById(R.id.matchinfoviewid);
        matchLinkView = findViewById(R.id.matchlinkviewid);
        detail_video_title_view = findViewById(R.id.detail_video_title_view);
        detail_toggle_link_view = findViewById(R.id.detail_toggle_link_view);
        detail_content_root_hiding = findViewById(R.id.detail_content_root_hiding);
        detail_language_text_view = findViewById(R.id.detail_language_text_view);
        detail_view_quality_view = findViewById(R.id.detail_view_quality_view);
        detail_title_root_layout = findViewById(R.id.detail_title_root_layout);
        hlsView = findViewById(R.id.hlsviewid);

        setlayoutParam(detail_thumbnail_root_layout);

        hlsView.addFullScreenClickListener(new IHlsFullScreenClickListener() {
            @Override
            public void OnFullscreenButtonClick(Boolean fullScreen) {
                Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                intent.putExtra("Url", url);
                intent.putExtra("Title", currentMatchInfo.getTitle());
                startActivity(intent);
            }
        });
        detail_title_root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detail_content_root_hiding.getVisibility() == View.VISIBLE) {
                    detail_content_root_hiding.setVisibility(View.GONE);
                    detail_toggle_link_view.setImageResource(R.drawable.arrow_down);
                } else {
                    detail_content_root_hiding.setVisibility(View.VISIBLE);
                    detail_toggle_link_view.setImageResource(R.drawable.arrow_up);
                }
            }
        });
        detail_thumbnail_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detail_thumbnail_play_button.getVisibility() == View.VISIBLE) {
                    detail_thumbnail_play_button.setVisibility(View.INVISIBLE);
                    detail_thumbnail_image_view.setVisibility(View.GONE);
                    hlsView.setVisibility(View.VISIBLE);
                    hlsView.startPlayer(url);
                }
            }
        });

        Intent intent = getIntent();

        Gson gson = new Gson();
        Type type = new TypeToken<MatchInfo>(){}.getType();
        currentMatchInfo = gson.fromJson(intent.getStringExtra("match"), type);
        url = currentMatchInfo.getLinkinfo().get(0).getLinks().get(0);

        gson = new Gson();
        type = new TypeToken<List<MatchInfo>>(){}.getType();
        List<MatchInfo> data = gson.fromJson(intent.getStringExtra("data"), type);

        UpdateMatchLink(currentMatchInfo);
        matchLinkView.addLinkInteractionListener(this);

        matchInfoView.setInfoList(data);
        matchInfoView.addLinkInteractionListener(this);

        actionBar.setTitle(currentMatchInfo.getTitle());
        updateDetailsInfo(currentMatchInfo.getTitle(), currentMatchInfo.getLinkinfo().get(0).getLanguage(), currentMatchInfo.getLinkinfo().get(0).getQuality());
        Picasso.get().load(imageUrl+currentMatchInfo.getTitle()+".jpg").placeholder(R.drawable.dummy_thumbnail).into(detail_thumbnail_image_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hlsView.setFullScreen(false);
    }

    @Override
    public void onClick(MatchInfo info) {
        currentMatchInfo = info;
        url = info.getLinkinfo().get(0).getLinks().get(0);
        UpdateMatchLink(info);
        actionBar.setTitle(currentMatchInfo.getTitle());
        updateDetailsInfo(currentMatchInfo.getTitle(), info.getLinkinfo().get(0).getLanguage(), info.getLinkinfo().get(0).getQuality());
        detail_thumbnail_play_button.setVisibility(View.VISIBLE);
        detail_thumbnail_image_view.setVisibility(View.VISIBLE);
        hlsView.setVisibility(View.GONE);
        hlsView.stopPlayer();
        Picasso.get().load(imageUrl+currentMatchInfo.getTitle()+".jpg").placeholder(R.drawable.dummy_thumbnail).into(detail_thumbnail_image_view);
    }

    @Override
    public void onClick(MatchLink match) {
        url = match.getLink();
        hlsView.startPlayer(url);
        updateDetailsInfo(currentMatchInfo.getTitle(), match.getLanguage(), match.getQuality());
    }

    private void UpdateMatchLink(MatchInfo matchInfo) {
        List<MatchLink> data = new ArrayList<MatchLink>();
        int i = 1;
        for(MatchLinkInfo linkInfo : matchInfo.getLinkinfo()) {
            for (String link : linkInfo.getLinks()){
                data.add(new MatchLink("Link " + i, link, linkInfo.getLanguage(), linkInfo.getQuality()));
                i++;
            }
        }
        matchLinkView.setLinkList(data);
    }

    private void updateDetailsInfo(String title, String language, String quality) {
        detail_video_title_view.setText(title);
        detail_language_text_view.setText(language);
        detail_view_quality_view.setText(quality);
    }

    private void setlayoutParam(FrameLayout layout) {
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        params.height = (int) (9 * screenWidth / 16);
        layout.setLayoutParams(params);
    }
}