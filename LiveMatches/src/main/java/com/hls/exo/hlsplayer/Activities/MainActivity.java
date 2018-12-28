package com.hls.exo.hlsplayer.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hls.exo.hlsplayer.Models.MatchInfo;
import com.hls.exo.hlsplayer.Models.MatchLinkInfo;
import com.hls.exo.hlsplayer.R;
import com.hls.exo.hlsplayer.Views.InfoView.IMatchInfoInteractionListener;
import com.hls.exo.hlsplayer.Views.InfoView.MatchInfoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements IMatchInfoInteractionListener {

    private MatchInfoView matchInfoView;
    private List<MatchInfo> linkList;

    private ProgressDialog busyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        busyDialog = new ProgressDialog(this.getApplicationContext());
        busyDialog.setMessage("Please wait.");
        busyDialog.setCancelable(false);

        matchInfoView = findViewById(R.id.matchlinkviewid);
        matchInfoView.addLinkInteractionListener(this);
        createListData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void onClick(MatchInfo match) {
        Intent intent = new Intent(this, DetailsMatchActivity.class);
        intent.putExtra("match", new Gson().toJson(match));
        intent.putExtra("data", new Gson().toJson(linkList));
        startActivity(intent);
    }

    private void createListData() {

        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
        String url ="http://87.210.113.95:60000/api/sport";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            linkList = new ArrayList<MatchInfo>();
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
                            linkList.addAll(infoHolder.values());
                            matchInfoView.setInfoList(linkList);
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
}
