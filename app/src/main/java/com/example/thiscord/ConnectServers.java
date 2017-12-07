package com.example.thiscord;

import android.util.Log;

import static com.example.thiscord.LoginActivity.dis;
import static com.example.thiscord.LoginActivity.dos;

/**
 * Created by 안탄 on 2017-12-06.
 */

public class ConnectServers implements Runnable {

    public String[] gmsg;

    @Override
    public void run(){
        //Toast.makeText(getApplicationContext(),"Connect Server",Toast.LENGTH_LONG).show();
        try {

            System.out.println("트라이는 들어옴");
            Send_Msg("로그인 유저"+" "+"refresh"); // 아이디랑를 보낸다

            String msg = dis.readUTF();
            System.out.println("서버에서 옴 : " + msg);
            gmsg = msg.split(" ");


            control_Msg(gmsg[0]);

        }catch (Exception e){
            e.printStackTrace();
            Log.e("client","connectServer() Error");
            return;
        }
    }

    public void Send_Msg(String msg){
        //  byte[] bb;
        //  bb = msg.getBytes();
        Log.e("send","send success");
        try{
            //  dos.write(bb);
            dos.writeUTF(msg);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void control_Msg(String str){ //
        if(str.equals("#refresh")){  // login success
            System.out.println("MainActivity login!!!");

            //main_joinstateAdapter.addItem(new Contacts(user_arrayList.get(i).getUrl(), user_arrayList.get(i).getBackurl(), user_arrayList.get(i).getId(),user_arrayList.get(i).getName(), user_arrayList.get(i).getStats()));
            //main_joinstateAdapter.addItem(new Contacts(R.drawable.user, R.drawable.background1, "안형우", "온라인")); // 이미지 url, 이름 name


        }
        else if(str.equals("#NOK")){ // pwd error
            System.out.println("password error!");
        }
        else
            System.out.println("id/pw error!");
    }

}
