package com.example.thiscord;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Main_JoinState_Fragment extends Fragment {
    SharedPreferences sharedPreferences;

    RecyclerView user_recyclerView ;
    private  Socket socket;
    private  DataInputStream dis;
    private InputStream is;
    private  DataOutputStream dos;
    private OutputStream os;
    private String login_user;

    public Main_JoinState_Fragment() {
        this.socket = LoginActivity.socket;
        this.dis = LoginActivity.dis;
        this.is = LoginActivity.is;
        this.dos = LoginActivity.dos;
        this.os = LoginActivity.os;


    }

    // 생성자 다음으로 불리는 데이터 처리하는곳 (리사이클 처리)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
        View view = inflater.inflate(R.layout.fragment_main__join_state_, container, false);
        user_recyclerView = (RecyclerView)view.findViewById(R.id.main_joinstate_recycleview);
        return view;
    }


    // 최종적으로 액티비티에 붙여주는곳
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        user_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Main_JoinStateAdapter main_joinstateAdapter = new Main_JoinStateAdapter(getActivity().getApplicationContext());
        user_recyclerView.setAdapter(main_joinstateAdapter);

        main_joinstateAdapter.addItem(new Contacts(R.drawable.user, R.drawable.background1, "안형우", "온라인")); // 이미지 url, 이름 name
        main_joinstateAdapter.addItem(new Contacts(R.drawable.user2, R.drawable.background2, "백승환", "오프라인")); // 이미지 url, 이름 name
        main_joinstateAdapter.addItem(new Contacts(R.drawable.user3, R.drawable.background3, "차연경", "자리비움")); // 이미지 url, 이름 name
        sharedPreferences = this.getActivity().getSharedPreferences("user_id", Context.MODE_PRIVATE);
        login_user = sharedPreferences.getString("userid", "01012345678");
        connectServer();
    }



    public void connectServer(){

        new Thread(){
            @Override
            public void run(){
                //Toast.makeText(getApplicationContext(),"Connect Server",Toast.LENGTH_LONG).show();
                try {

                    /*socket = new Socket(ip, port);
                    is = socket.getInputStream();
                    dis = new DataInputStream(is);
                    os = socket.getOutputStream();
                    dos = new DataOutputStream(os);*/
                    System.out.println("트라이는 들어옴");
                    Send_Msg(login_user+" "+"MainActivity"); // 아이디랑를 보낸다

                    byte[] b = new byte[1024];
                    dis.read(b);

                    String msg = new String(b, "euc-kr");

                    System.out.println(msg);
                    String[] gmsg = msg.split(" ");

                    control_Msg(gmsg[0]);

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("client","connectServer() Error");
                }
            }
        }.start();
    }

    public void Send_Msg(String msg){
        byte[] bb;
        bb = msg.getBytes();
        Log.e("send","send success");
        try{
            dos.write(bb);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void control_Msg(String str){ //
        if(str.equals("#OK")){  // login success
            System.out.println("login!!!");
            /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);*/
        }
        else if(str.equals("#NOK")){ // pwd error
            System.out.println("password error!");
        }
        else
            System.out.println("id/pw error!");
    }



}
