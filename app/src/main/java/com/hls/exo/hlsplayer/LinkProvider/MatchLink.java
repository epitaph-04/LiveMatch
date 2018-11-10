package com.hls.exo.hlsplayer.LinkProvider;

public class MatchLink {

    private String link;
    private  String language;
    private String title;
    private String quality;

    public MatchLink(String title, String link, String language, String quality) {
        this.title = title;
        this.link = link;
        this.language = language;
        this.quality = quality;
    }

    public String getTitle() {
        return title;
    }
    public String getLink() {
        return link;
    }
    public String getLanguage() {
        return language;
    }
    public String getQuality() { return quality; }
}
