package com.example.thiscord;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ChatActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText input_chattext;
    private Button send_btn;
    private Button invite_btn;
    private String user_id;
    private String now_room_name;
    private String nowingChat;
    private String[] gmsg; // 받은 데이터 split으로 잘라서 넣어두는곳



    private Intent intent;
    Socket socket;
    //private String ip = "10.0.2.2";   // 안드로이드 에뮬레이터에서는 localhost가 아니라 10.0.2.2로 접근!!
    //private int port =30000;
    private Thread thread;
    DataInputStream dis;
    InputStream is;
    DataOutputStream dos;
    OutputStream os;
    private String login_id;
    private String login_pwd;
    private String recv_msg; // 받은 메세지

    public ChatActivity(){
        this.socket = LoginActivity.socket;
        this.dis = LoginActivity.dis;
        this.is = LoginActivity.is;
        this.dos = LoginActivity.dos;
        this.os = LoginActivity.os;
    }


    // onStart에서 서버를 미리 연결 해둠 (서버로 보낼 멀티쓰레드 구현 -> ??)
    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences("user_id", MODE_PRIVATE);
        user_id = sharedPreferences.getString("userid", "0");
        System.out.println("채팅액티비티 : 유저 - " + user_id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        intent = getIntent();
        now_room_name = intent.getStringExtra("roomname");

        input_chattext = (EditText)findViewById(R.id.input_chat);
        nowingChat = input_chattext.getText().toString();
        send_btn = (Button)findViewById(R.id.input_chat_send);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("유저아이디 : " + user_id);
                //connectServer();
            }
        });

        // 사용자 초대
        invite_btn = (Button)findViewById(R.id.invite_btn);
        invite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Invite_Room_Activity.class);
                System.out.println("인텐트 전 : " + now_room_name);
                intent.putExtra("room_name", now_room_name);
                startActivity(intent);

            }
        });


    }



    public void connectServer(){

        new Thread(){
            @Override
            public void run(){
                //Toast.makeText(getApplicationContext(),"Connect Server",Toast.LENGTH_LONG).show();
                try {


                    System.out.println("트라이는 들어옴");
                    Send_Msg(login_id+" "+nowingChat); // 현재 입력 되어 있는(에디트텍스트) 값을 보냄

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

    private void control_Msg(String str){
        if(str.equals("#OK")){  // login success
            System.out.println("login!");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else if(str.equals("#NOK")){ // pwd error
            System.out.println("password error!");
        }
        else
            System.out.println("id/pw error!");
    }

}
