package com.hls.exo.hlsplayer.Models;

import java.util.List;

public class MatchInfo {
    private String title;
    private String time;
    private List<MatchLinkInfo> linkinfo;

    public MatchInfo(String title, String time, List<MatchLinkInfo> linkinfo) {
        this.title = title;
        this.time = time;
        this.linkinfo = linkinfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() { return time; }

    public  List<MatchLinkInfo> getLinkinfo() { return linkinfo; }
}
