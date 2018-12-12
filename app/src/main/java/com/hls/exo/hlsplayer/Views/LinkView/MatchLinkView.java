package com.hls.exo.hlsplayer.Views.LinkView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.hls.exo.hlsplayer.Models.MatchLink;
import com.hls.exo.hlsplayer.R;

import java.util.ArrayList;
import java.util.List;

public class MatchLinkView extends FrameLayout implements IMatchLinkClickListener {
    private RecyclerView recyclerView;
    private MatchLinkAdapter adapter;
    private List<MatchLink> linkList;

    private List<IMatchLinkInteractionListener> listeners = new ArrayList<IMatchLinkInteractionListener>();
    public MatchLinkView(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public MatchLinkView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MatchLinkView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MatchLinkView, defStyle, 0);

        a.recycle();

        inflate(context, R.layout.match_view,this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        linkList = new ArrayList<MatchLink>();
        adapter = new MatchLinkAdapter(context, linkList, this);
        recyclerView.setAdapter(adapter);
    }

    public void addLinkInteractionListener(IMatchLinkInteractionListener toAdd){ listeners.add(toAdd); }

    public List<MatchLink> getLinkList() {
        return linkList;
    }

    public void setLinkList(List<MatchLink> list) {
        linkList.clear();
        linkList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(MatchLink matchLink) {
        for (IMatchLinkInteractionListener listener : listeners) {
            listener.onClick(matchLink);
        }
    }
}
