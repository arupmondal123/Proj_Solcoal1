package com.at.drgrep.data;

/**
 * Created by Arup on 7/20/2016.
 */
public class Shop {

    private String name;
    private String location;
    private int thumbnail;

    public Shop() {
    }

    public Shop(String name, String location, int thumbnail) {
        this.name = name;
        this.location = location;
        this.thumbnail = thumbnail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

}
