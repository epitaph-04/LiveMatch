package com.hls.exo.hlsplayer.Views.InfoView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hls.exo.hlsplayer.Models.MatchInfo;
import com.hls.exo.hlsplayer.R;
import com.squareup.picasso.Picasso;

public class MatchInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private final String imageUrl = "http://87.210.113.95:60000/StaticFiles/";
    private ImageView itemThumbnailView;
    private TextView itemVideoTitleView;
    private TextView itemDurationView;
    private IMatchInfoClickListener mListener;

    private MatchInfo matchInfo;

    public MatchInfoHolder(View itemView, IMatchInfoClickListener listener) {
        super(itemView);
        itemThumbnailView = itemView.findViewById(R.id.itemThumbnailView);
        itemVideoTitleView = itemView.findViewById(R.id.itemVideoTitleView);
        itemDurationView = itemView.findViewById(R.id.itemDurationView);
        mListener = listener;
        itemView.setOnClickListener(this);
    }

    public void setInfo(MatchInfo info) {
        itemVideoTitleView.setText(info.getTitle());
        itemDurationView.setText(info.getTime());
        Picasso.get().load(imageUrl+info.getTitle()+".jpg").placeholder(R.drawable.dummy_thumbnail).into(itemThumbnailView);
        matchInfo = info;
    }

    @Override
    public void onClick(View v) {
        mListener.onClick(matchInfo);
    }
}
