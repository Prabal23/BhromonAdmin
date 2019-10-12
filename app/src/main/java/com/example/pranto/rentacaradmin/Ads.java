package com.example.pranto.rentacaradmin;

/**
 * Created by Pranto on 9/23/2017.
 */

public class Ads {
    private String id;
    private String url;
    private String title;

    public Ads(String id, String url, String title) {
        this.id = id;
        this.url = url;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String image) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
}
