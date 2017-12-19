package com.example.thiscord;

/**
 * Created by 안탄 on 2017-12-19.
 */

public class InChatRoomUserList {
    String id;
    String ip;
    int port;

    public InChatRoomUserList(){

    }

    public InChatRoomUserList(String id, String ip, int port){
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
