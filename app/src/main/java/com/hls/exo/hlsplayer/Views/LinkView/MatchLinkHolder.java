package com.hls.exo.hlsplayer.Views.LinkView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hls.exo.hlsplayer.Models.MatchLink;
import com.hls.exo.hlsplayer.R;

public class MatchLinkHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView itemVideoTitleView;
    private TextView itemLanguageView;
    private TextView itemQualityView;
    private IMatchLinkClickListener mListener;

    private MatchLink matchLink;

    public MatchLinkHolder(View itemView, IMatchLinkClickListener listener) {
        super(itemView);
        itemVideoTitleView = itemView.findViewById(R.id.itemVideoTitleView);
        itemLanguageView = itemView.findViewById(R.id.itemLanguageView);
        itemQualityView = itemView.findViewById(R.id.itemQualityView);

        mListener = listener;
        itemView.setOnClickListener(this);
    }

    public void setMatchLinks(MatchLink link) {
        itemVideoTitleView.setText(link.getTitle());
        itemLanguageView.setText(link.getLanguage());
        itemQualityView.setText(link.getQuality());
        matchLink = link;
    }

    public void onClick(View v) {
        mListener.onClick(matchLink);
    }
}
