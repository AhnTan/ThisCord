package com.example.thiscord;

/**
 * Created by 안탄 on 2017-11-24.
 */

public class RoomContacts {
    String room_num;
    String room_tile;

    public RoomContacts(){

    }

    public RoomContacts(String room_num, String room_tile){
        this.room_num = room_num;
        this.room_tile = room_tile;
    }

    public String getRoom_num() {
        return room_num;
    }

    public void setRoom_num(String room_num) {
        this.room_num = room_num;
    }

    public String getRoom_tile() {
        return room_tile;
    }

    public void setRoom_tile(String room_tile) {
        this.room_tile = room_tile;
    }
}
