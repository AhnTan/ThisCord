package com.example.thiscord;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import static com.example.thiscord.LoginActivity.dis;
import static com.example.thiscord.LoginActivity.dos;


public class Main_roomlist_Fragment extends Fragment {
    private FloatingActionButton floatingActionButton;
    private RecyclerView room_recyclerView ;
    private static final int request_code = 0;
    private Main_roomlistAdapter main_roomlistAdapter;

    private FloatingActionButton refresh_btn;

    private int room_num; // 방 번호
    private String room_id; // 방이름
    private String login_user; // 접속한 유저 아이디
    private String[] gmsg; // 받은 데이터 split으로 잘라서 넣어두는곳
    SharedPreferences sharedPreferences;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
        View view = inflater.inflate(R.layout.fragment_main_roomlist_, container, false);
        room_recyclerView = (RecyclerView)view.findViewById(R.id.room_recycleview);
        refresh_btn = (FloatingActionButton)view.findViewById(R.id.refresh_room_fab);
        return view;
    }


    // 최종적으로 액티비티에 붙여주는곳
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        room_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        main_roomlistAdapter = new Main_roomlistAdapter(getActivity().getApplicationContext());
        room_recyclerView.setAdapter(main_roomlistAdapter);

        sharedPreferences = this.getActivity().getSharedPreferences("user_id", Context.MODE_PRIVATE);
        login_user = sharedPreferences.getString("userid", "01012345678");

        // 맨처음 방플래그먼트에 들어올때 서버에 저장되어있는 방목록 가져옴
        init();

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("겟룸클릭버튼1 : ");
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    Send_Msg(login_user + " " + "getroom"); // 아이디랑를 보낸다
                    System.out.println("겟룸클릭버튼2 : " );
                    String msg = dis.readUTF();
                    System.out.println("겟룸클릭버튼-서버에서 옴 : " + msg);
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



        floatingActionButton = (FloatingActionButton)getActivity().findViewById(R.id.room_add_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            // 방 추가하는 버튼
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), " 방생성 버튼이다아아!!!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), Create_Room_Activity.class);
                startActivityForResult(intent, request_code);

            }
        });

        //connectServer();
    }


    // 다른 액티비티에서 넘어온 값을 처리해줌(-> 방생성하기 액티비티)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        room_id = "";
        try {
            room_id += data.getStringExtra("Result");
            room_num = data.getIntExtra("num", 0);
            // 플로팅 버튼에서 취소버튼이 아닌 확인 버튼을 누르게 되면 방목록을 추가함
            if(!room_id.equals("cancle")){
                System.out.println("내가 방만든 번호 : " + room_num);
                main_roomlistAdapter.addItem(new RoomContacts(room_num,room_id));
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }



    }


    public void Send_Msg(String msg){

        try{
            //  dos.write(bb);
            dos.writeUTF(msg);
            Log.e("send","send success");
        }catch(Exception e){
            e.printStackTrace();
            Log.e("send","send failed");
        }
    }

    private void control_Msg(String str){
        if(str.equals("#getroom")){  // login success
            System.out.println("길이 : " + gmsg.length);
            // 어댑터 삭제해야될부분 추가
            removedItem();
            //System.out.println("서버에서 받은 번호 : " + Integer.parseInt(gmsg[2]));
            //main_roomlistAdapter.addItem(new RoomContacts(Integer.parseInt(gmsg[2]), gmsg[1]));
            for(int i = 1; i<gmsg.length; i+=2){
                main_roomlistAdapter.addItem(new RoomContacts(Integer.parseInt(gmsg[i+1]) ,gmsg[i]));
            }
            //main_roomlistAdapter.addItem(new RoomContacts("1",gmsg[1]));
        }
        else if(str.equals("#NOK")){ // pwd error
            System.out.println("password error!");
        }
        else
            System.out.println("id/pw error!");
    }


    public void init(){
        try {
            System.out.println("방쪽 초기화 1 : ");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Send_Msg(login_user + " " + "getroom"); // 아이디랑를 보낸다
            System.out.println("방쪽 초기화 2 : " );
            String msg = dis.readUTF();
            System.out.println("방쪽 초기화-서버에서 옴 : " + msg);
            gmsg = msg.split(" ");

            control_Msg(gmsg[0]);

        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("client","connectServer() Error");
            return;
        }
    }

    public void removedItem(){
        
            main_roomlistAdapter.removeItem();
            //main_joinstateAdapter.notifyDataSetChanged();


    }






}




