package com.hls.exo.hlsplayer.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.hls.exo.hlsplayer.ILinkInteractionListener;
import com.hls.exo.hlsplayer.LinkProvider.IRecyclerClickListener;
import com.hls.exo.hlsplayer.LinkProvider.MatchInfo;
import com.hls.exo.hlsplayer.LinkProvider.MatchInfoAdapter;
import com.hls.exo.hlsplayer.LinkProvider.MatchLink;
import com.hls.exo.hlsplayer.LinkProvider.MatchLinkInfo;
import com.hls.exo.hlsplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class MatchLinkView extends FrameLayout implements IRecyclerClickListener {

    private RecyclerView recyclerView;
    private MatchInfoAdapter adapter;
    private List<MatchInfo> linkList;

    private List<ILinkInteractionListener> listeners = new ArrayList<ILinkInteractionListener>();

    public MatchLinkView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MatchLinkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MatchLinkView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MatchLinkView, defStyle, 0);

        a.recycle();

        inflate(context, R.layout.link_view,this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        linkList = new ArrayList<MatchInfo>();
        adapter = new MatchInfoAdapter(context, linkList, this);
        recyclerView.setAdapter(adapter);
    }

    public void addLinkInteractionListener(ILinkInteractionListener toAdd){ listeners.add(toAdd); }

    public List<MatchInfo> getLinkList() {
        return linkList;
    }

    public void setLinkList(List<MatchInfo> list) {
        linkList.clear();
        linkList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(String url) {
        for (ILinkInteractionListener listener : listeners) {
            listener.onClick(url);
        }
    }

    @Override
    public void onClick(List<MatchLinkInfo> urls) {
        ArrayList<MatchLink> links = new ArrayList<>();
        int i = 0;
        for (MatchLinkInfo url : urls) {
            for (String ll : url.getLinks()) {
                links.add(new MatchLink("Link "+(++i), ll, url.getLanguage(), url.getQuality()));
            }
        }
    }
}
