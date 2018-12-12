package com.hls.exo.hlsplayer.Views.InfoView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.hls.exo.hlsplayer.Models.MatchInfo;
import com.hls.exo.hlsplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class MatchInfoView extends FrameLayout implements IMatchInfoClickListener {

    private RecyclerView recyclerView;
    private MatchInfoAdapter adapter;
    private List<MatchInfo> infoList;

    private List<IMatchInfoInteractionListener> listeners = new ArrayList<IMatchInfoInteractionListener>();

    public MatchInfoView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MatchInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MatchInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MatchInfoView, defStyle, 0);

        a.recycle();

        inflate(context, R.layout.match_view,this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        infoList = new ArrayList<MatchInfo>();
        adapter = new MatchInfoAdapter(context, infoList, this);
        recyclerView.setAdapter(adapter);
    }

    public void addLinkInteractionListener(IMatchInfoInteractionListener toAdd){ listeners.add(toAdd); }

    public List<MatchInfo> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<MatchInfo> list) {
        infoList.clear();
        infoList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(MatchInfo matchInfo) {
        for (IMatchInfoInteractionListener listener : listeners) {
            listener.onClick(matchInfo);
        }
    }
}
