package com.hls.exo.hlsplayer.LinkProvider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hls.exo.hlsplayer.R;

import java.util.ArrayList;
import java.util.List;

public class MatchInfoAdapter extends RecyclerView.Adapter<MatchInfoHolder>{

    private Context context;
    private List<MatchInfo> matchInfos;
    private IRecyclerClickListener mListener;

    public MatchInfoAdapter(Context context, List<MatchInfo> matchInfos, IRecyclerClickListener listener) {
        this.context = context;
        this.matchInfos = matchInfos;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MatchInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.link_list, parent, false);
        return new MatchInfoHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchInfoHolder holder, int position) {
        MatchInfo link = matchInfos.get(position);
        holder.setLinks(link);
    }

    @Override
    public int getItemCount() {
        if(matchInfos == null) return 0;
        return matchInfos.size();
    }
}
