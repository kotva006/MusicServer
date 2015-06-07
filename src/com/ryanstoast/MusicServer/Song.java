package com.ryanstoast.MusicServer;

import com.google.gson.JsonObject;

import java.io.Serializable;

public class Song implements Serializable {

    private String artist;
    private String album;
    private String title;
    private String trackNumber;
    private String duration;
    private String path;
    private int id;

    public String getPath() {return path;}

    public Song(){}
    public Song(String art, String al, String t, String d, String track, String p) {
        this.artist = art;
        this.album = al;
        this.title = t;
        this.trackNumber = track;
        this.duration = d;
        this.path = p;
        this.id = p.hashCode();
    }

    public JsonObject toJSONObject() {
        JsonObject j = new JsonObject();
        j.addProperty("artist", artist);
        j.addProperty("album", album);
        j.addProperty("title", title);
        j.addProperty("trackNumber", trackNumber);
        j.addProperty("duration", duration);
        j.addProperty("id", id);

        return j;
    }

}
