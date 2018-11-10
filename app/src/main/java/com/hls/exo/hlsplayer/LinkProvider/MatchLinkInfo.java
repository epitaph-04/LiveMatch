package com.hls.exo.hlsplayer.LinkProvider;

import java.util.List;

public class MatchLinkInfo {
    private String language;
    private String quality;
    private List<String> links;

    public MatchLinkInfo(String language, String quality, List<String> links) {
        this.language = language;
        this.quality = quality;
        this.links = links;
    }

    public String getLanguage() { return language; }

    public String getQuality() { return quality; }

    public List<String> getLinks() {
        return links;
    }
}
