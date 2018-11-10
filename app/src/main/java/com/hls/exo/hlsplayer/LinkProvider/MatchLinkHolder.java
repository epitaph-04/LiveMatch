package com.hls.exo.hlsplayer.LinkProvider;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hls.exo.hlsplayer.R;

public class MatchLinkHolder extends RecyclerView.ViewHolder {
    private TextView linkView;
    private TextView languageView;
    private TextView qualityView;
    private ImageButton playButton;
    private IRecyclerClickListener mListener;

    private String matchurl;

    public MatchLinkHolder(View itemView, IRecyclerClickListener listener) {
        super(itemView);
        linkView = itemView.findViewById(R.id.linkTitle);
        languageView = itemView.findViewById(R.id.language);
        qualityView = itemView.findViewById(R.id.quality);
        playButton = itemView.findViewById(R.id.linkPlay);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(matchurl);
            }
        });

        mListener = listener;
    }

    public void setMatchLinks(MatchLink link) {
        linkView.setText(link.getTitle());
        languageView.setText(link.getLanguage());
        qualityView.setText(link.getQuality());
        matchurl = link.getLink();
    }
}
