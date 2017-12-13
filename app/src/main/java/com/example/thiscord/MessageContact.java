package com.example.thiscord;

/**
 * Created by 안탄 on 2017-12-09.
 */

public class MessageContact {
    String user_name;
    String message;
    String time;
    int index;
    int url;



    public MessageContact(){

    }

    public MessageContact(String user_name, String message, String time, int index, int url){
        this.user_name = user_name;
        this.message = message;
        this.time = time;
        this.index = index;
        this.url = url;
    }

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
