package com.example.thiscord;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class Main_JoinState_Fragment extends Fragment {
    SharedPreferences sharedPreferences;

    RecyclerView user_recyclerView ;
    private  Socket socket;
    private  DataInputStream dis;
    private InputStream is;
    private  DataOutputStream dos;
    private OutputStream os;

    private String login_user; // 접속한 유저 아이디
    private String[] gmsg; // 받은 데이터 split으로 잘라서 넣어두는곳

    private int start_count = 0;
    private int btn_count = 0;

    private FloatingActionButton refresh_btn;

    private Main_JoinStateAdapter main_joinstateAdapter;

    private ArrayList<Contacts> user_arrayList = new ArrayList<>();

    ConnectServers connectServers ;

    public Main_JoinState_Fragment() {
        this.socket = LoginActivity.socket;
        this.dis = LoginActivity.dis;
        this.is = LoginActivity.is;
        this.dos = LoginActivity.dos;
        this.os = LoginActivity.os;

        user_arrayList.add(new Contacts(R.drawable.user, R.drawable.background, "ahntan", "안형우", "오프라인"));
        user_arrayList.add(new Contacts(R.drawable.user2, R.drawable.background2, "shbaek","백승환", "오프라인"));
        user_arrayList.add(new Contacts(R.drawable.user3, R.drawable.background3, "jsr", "진소린", "오프라인"));
        user_arrayList.add(new Contacts(R.drawable.user4, R.drawable.background2, "sje","송정은", "오프라인"));
        user_arrayList.add(new Contacts(R.drawable.user5, R.drawable.background1, "hkd","한경동", "오프라인"));

    }

    // 생성자 다음으로 불리는 데이터 처리하는곳 (리사이클 처리)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
        View view = inflater.inflate(R.layout.fragment_main__join_state_, container, false);
        user_recyclerView = (RecyclerView)view.findViewById(R.id.main_joinstate_recycleview);
        refresh_btn = (FloatingActionButton)view.findViewById(R.id.refresh_fab);
        return view;
    }


    // 최종적으로 액티비티에 붙여주는곳
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        user_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        main_joinstateAdapter = new Main_JoinStateAdapter(getActivity().getApplicationContext());
        user_recyclerView.setAdapter(main_joinstateAdapter);


        sharedPreferences = this.getActivity().getSharedPreferences("user_id", Context.MODE_PRIVATE);
        login_user = sharedPreferences.getString("userid", "01012345678");
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("클릭버튼1 : ");
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    Send_Msg(login_user + " " + "refresh"); // 아이디랑를 보낸다
                    System.out.println("클릭버튼2 : " );
                    String msg = dis.readUTF();
                    System.out.println("클릭버튼-서버에서 옴 : " + msg);
                    gmsg = msg.split(" ");

                    control_Msg(gmsg[0]);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.e("client","connectServer() Error");
                    return;
                }
            }
        });

        //connectServer();
        addUser();


    }


    public void connectServer(){

        new Thread(){
            @Override
            public void run() {

                    //Toast.makeText(getApplicationContext(),"Connect Server",Toast.LENGTH_LONG).show();
                    try {
                        System.out.println("트라이는 들어옴");
                        Send_Msg(login_user + " " + "refresh"); // 아이디랑를 보낸다

                        String msg = dis.readUTF();
                        System.out.println("유저프래그먼트 서버에서 옴 : " + msg);
                        gmsg = msg.split(" ");

                        control_Msg(gmsg[0]);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("client", "connectServer() Error");
                        return;
                    }
                }

        }.start();
    }

    public void Send_Msg(String msg){
        Log.e("send","send success");
        try{
            //  dos.write(bb);
            dos.writeUTF(msg);
        }catch(Exception e){
            e.printStackTrace();
            Log.e("send","send failed");
        }

    }

    private void control_Msg(String str){ //
        if(str.equals("#refresh")){  // login success
            System.out.println("MainActivity login!!!");

            for(int i=1; i<gmsg.length; i++){
                System.out.println("접속한 유저 : " + gmsg[i] + user_arrayList.size());
                for(int j=0; j<user_arrayList.size(); j++){
                    System.out.println(j + "번째 유저 : " + user_arrayList.get(j).getId());
                    if(user_arrayList.get(j).getId().equals(gmsg[i])){
                        System.out.println("상태 바뀔 유저 : " + user_arrayList.get(j).getId());
                        user_arrayList.get(j).setStats("온라인");
                    }
                }
            }
            removed();

        }
        else if(str.equals("#NOK")){ // pwd error
            System.out.println("password error!");
        }
        else
            System.out.println("id/pw error!");
    }


    public void removed(){
        main_joinstateAdapter.removeItem();
        //main_joinstateAdapter.notifyDataSetChanged();
        try{
            Thread.sleep(100);
        }
        catch (Exception e) {

        }
        addUser();
    }

    public void addUser(){

        for(int k=0; k<user_arrayList.size(); k++){
            if(login_user.equals(user_arrayList.get(k).getId())){
                System.out.println("본인 추가 : " + user_arrayList.get(k).getId() + " " + login_user);
                user_arrayList.get(k).setStats("온라인");
                main_joinstateAdapter.addItem(new Contacts(user_arrayList.get(k).getUrl(), user_arrayList.get(k).getBackurl(), user_arrayList.get(k).getId(), user_arrayList.get(k).getName(), user_arrayList.get(k).getStats()));
            }
        }

        // 유저들 어댑터에 붙임
        for(int i=0; i<user_arrayList.size(); i++){
            if(!user_arrayList.get(i).getId().equals(login_user)){
                main_joinstateAdapter.addItem(new Contacts(user_arrayList.get(i).getUrl(), user_arrayList.get(i).getBackurl(), user_arrayList.get(i).getId(), user_arrayList.get(i).getName(), user_arrayList.get(i).getStats()));
                //main_joinstateAdapter.notifyDataSetChanged();
            }
        }

    }



}
