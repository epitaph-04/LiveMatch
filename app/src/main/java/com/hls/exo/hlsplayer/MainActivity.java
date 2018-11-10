package com.hls.exo.hlsplayer;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity implements ILinkInteractionListener{

    private LinkView linkView;
    private String currentUrl;
    private AppBarLayout appBarLayout;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBarLayout = findViewById(R.id.app_bar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeFragment();
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linkView.refresh();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void initializeFragment(){
        HlsPlayer hlsPlayer = HlsPlayer.newInstance(currentUrl);
        ManageFragment(hlsPlayer, R.id.playerfragment);
        linkView = LinkView.newInstance(this);
        ManageFragment(linkView, R.id.listfragment);
    }

    public void onClick(String url) {
        StartPlayer(url);
    }

    private void StartPlayer(String url) {
        currentUrl = url;
        HlsPlayer hlsPlayer = HlsPlayer.newInstance(url);
        ManageFragment(hlsPlayer, R.id.playerfragment);
    }

    private void ManageFragment(android.support.v4.app.Fragment fragment, int viewId) {
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(viewId, fragment, fragmentTag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(backStateName)
                    .commit();
        }
    }
}
