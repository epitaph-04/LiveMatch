package com.hls.exo.hlsplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.hls.exo.hlsplayer.LinkProvider.MatchInfoAdapter;
import com.hls.exo.hlsplayer.LinkProvider.MatchLinkAdapter;

import java.util.ArrayList;

public class LinkDialog extends DialogFragment {

    private RecyclerView recyclerView;
    private MatchLinkAdapter matchLinkAdapter;

    public LinkDialog() {

    }

    public static LinkDialog newInstance(MatchLinkAdapter matchLinkAdapter) {
        LinkDialog frag = new LinkDialog();
        frag.matchLinkAdapter = matchLinkAdapter;
        Bundle args = new Bundle();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.link_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.dialogRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(matchLinkAdapter);
        matchLinkAdapter.notifyDataSetChanged();

        getDialog().setTitle("Available links");
        getDialog().getWindow();
    }
}