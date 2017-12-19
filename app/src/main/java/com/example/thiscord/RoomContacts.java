package com.example.thiscord;

/**
 * Created by 안탄 on 2017-11-24.
 */

public class RoomContacts {
    int room_num;
    String room_tile;

    public RoomContacts(){

    }

    public RoomContacts(int room_num, String room_tile){
        this.room_num = room_num;
        this.room_tile = room_tile;
    }

    public int getRoom_num() {
        return room_num;
    }

    public void setRoom_num(int room_num) {
        this.room_num = room_num;
    }

    public String getRoom_tile() {
        return room_tile;
    }

    public void setRoom_tile(String room_tile) {
        this.room_tile = room_tile;
    }
}
