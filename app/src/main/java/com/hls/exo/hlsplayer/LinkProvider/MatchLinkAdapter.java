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

public class MatchLinkAdapter extends RecyclerView.Adapter<MatchLinkHolder> {

    private Context context;
    private List<MatchLink> matchLinks;
    private IRecyclerClickListener mListener;

    public MatchLinkAdapter(Context context, ArrayList<MatchLink> matchLinks, IRecyclerClickListener listener) {
        this.context = context;
        this.matchLinks = matchLinks;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MatchLinkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_list, parent, false);
        return new MatchLinkHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchLinkHolder holder, int position) {
        MatchLink link = matchLinks.get(position);
        holder.setMatchLinks(link);
    }

    @Override
    public int getItemCount() {
        return matchLinks.size();
    }
}
