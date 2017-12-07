package com.example.thiscord;

/**
 * Created by 안탄 on 2017-11-22.
 */

public class Contacts {
    int url;
    int backurl;
    String id;
    String name;
    String stats;

    public Contacts(){

    }

    public Contacts(int url, int backurl, String id, String name, String stats){
        this.url = url;
        this.backurl = backurl;
        this.id = id;
        this.name = name;
        this.stats = stats;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBackurl() {
        return backurl;
    }

    public void setBackurl(int backurl) {
        this.backurl = backurl;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }



    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
