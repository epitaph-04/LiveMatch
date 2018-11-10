package com.hls.exo.hlsplayer.LinkProvider;

import android.view.View;

import java.util.List;

public interface IRecyclerClickListener {
    void onClick(String url);
    void onClick(List<MatchLinkInfo> urls);
}