package com.hls.exo.hlsplayer;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.hls.exo.hlsplayer.LinkProvider.IRecyclerClickListener;
import com.hls.exo.hlsplayer.LinkProvider.MatchInfo;
import com.hls.exo.hlsplayer.LinkProvider.MatchInfoAdapter;
import com.hls.exo.hlsplayer.LinkProvider.MatchLink;
import com.hls.exo.hlsplayer.LinkProvider.MatchLinkAdapter;
import com.hls.exo.hlsplayer.LinkProvider.MatchLinkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class LinkView extends Fragment implements IRecyclerClickListener {
    private RecyclerView recyclerView;
    private MatchInfoAdapter adapter;
    private ArrayList<MatchInfo> linkArrayList;

    private LinkDialog linkDialog;
    private ProgressDialog busyDialog;

    private ILinkInteractionListener listener;

    public LinkView() {

    }

    public static LinkView newInstance(ILinkInteractionListener listener) {
        LinkView fragment = new LinkView();
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.link_view, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        linkArrayList = new ArrayList<>();
        adapter = new MatchInfoAdapter(getContext(), linkArrayList, this);
        recyclerView.setAdapter(adapter);
        initializeProgressDialog();
        createListData();
    }

    @Override
    public void onResume() {
        initializeProgressDialog();
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(String url) {
        if(linkDialog != null) linkDialog.dismiss();
        listener.onClick(url);
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
        MatchLinkAdapter matchadapter = new MatchLinkAdapter(getContext(), links, this);
        showMatchUrlsDialog(matchadapter);
    }

    public void refresh() {
        createListData();
    }

    private void createListData() {

        busyDialog.dismiss();
        busyDialog.show();
        linkArrayList.clear();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="http://87.210.113.95:60000/api/sport";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            Map<String, MatchInfo> infoHolder = new HashMap<String, MatchInfo>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject c = response.getJSONObject(i);

                                JSONObject linkinfo = c.getJSONObject("linkInfo");
                                String title = linkinfo.getString("title");
                                String language = linkinfo.getString("language");
                                String time = linkinfo.getString("time");
                                String quality = linkinfo.getString("quality");

                                JSONArray links = c.getJSONArray("links");
                                ArrayList<String> videolink = new ArrayList<>();
                                for (int j = 0; j < links.length(); j++) {
                                    videolink.add(links.getString(j));
                                }

                                List<MatchLinkInfo> link = new ArrayList<MatchLinkInfo>();
                                link.add(new MatchLinkInfo(language, quality, videolink));

                                if(infoHolder.containsKey(title)) {
                                    List<MatchLinkInfo> ll = infoHolder.get(title).getLinkinfo();
                                    link.addAll(ll);
                                }
                                infoHolder.put(title, new MatchInfo(title, time, link));
                            }
                            linkArrayList.addAll(infoHolder.values());
                            adapter.notifyDataSetChanged();
                        }
                        catch (JSONException ex){}
                        finally {
                            busyDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        busyDialog.dismiss();
                    }
                });

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    private void initializeProgressDialog() {
        if(busyDialog == null) {
            busyDialog = new ProgressDialog(getContext());
            busyDialog.setMessage("Please wait.");
            busyDialog.setCancelable(false);
        }
    }

    private void showMatchUrlsDialog(MatchLinkAdapter matchLinkAdapter) {
        FragmentManager fm = getFragmentManager();
        linkDialog = LinkDialog.newInstance(matchLinkAdapter);
        linkDialog.show(fm, "fragment_edit_name");
    }
}
