package com.esprit.findme.models;

import org.json.JSONObject;

/**
 * Created by Med-Amine on 29/11/2016.
 */

public class Image {
    private int id;
    private String url;
    private String name;
    //TODO news_id

    public Image() {
    }

    public Image(int id, String url, String name) {
        this.id = id;
        this.url = url;
        this.name = name;
    }

    public Image(JSONObject j) {
        id = j.optInt("id");
        url = j.optString("url");
        name = j.optString("name");

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
