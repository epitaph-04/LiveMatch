package com.hls.exo.hlsplayer.LinkProvider;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hls.exo.hlsplayer.R;

import java.util.List;

public class MatchInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView title;
    private TextView linkCount;
    private ImageButton image;
    private IRecyclerClickListener mListener;

    private List<MatchLinkInfo> matchurls;

    public MatchInfoHolder(View itemView, IRecyclerClickListener listener) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        linkCount = itemView.findViewById(R.id.link);
        image = itemView.findViewById(R.id.more);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(matchurls);
            }
        });

        mListener = listener;
        itemView.setOnClickListener(this);
    }

    public void setLinks(MatchInfo link) {
        int urlCount = 0;
        for (MatchLinkInfo ii : link.getLinkinfo()) {
            urlCount += ii.getLinks().size();
        }

        String count = "live link: " + urlCount;
        title.setText(link.getTitle());
        linkCount.setText(count);
        matchurls = link.getLinkinfo();
        if(urlCount == 1)
            image.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        mListener.onClick(matchurls.get(0).getLinks().get(0));
    }
}
