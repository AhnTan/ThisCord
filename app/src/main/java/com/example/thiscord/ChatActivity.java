package com.example.thiscord;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity{


    private static boolean count=true;

    private SharedPreferences sharedPreferences;
    private TextView room_name_title;
    private EditText input_chattext;
    private Button send_btn;
    private Button invite_btn;
    private String user_id;
    private String now_room_name;
    private String nowingChat;
    private String[] gmsg; // 받은 데이터 split으로 잘라서 넣어두는곳

    private String msg3;

    private Handler handler;

    private long now ;
    private Date date;
    private SimpleDateFormat simpleDateFormat;
    private String formatDate;
    private String imgDate="";

    private RecvThread recvThread;
    private RecyclerView recyclerView;

    private ArrayList<MessageContact> user_arrayList = new ArrayList<>();
    private Chat_Adapter chat_adapter;

    private Intent intent;
    Socket socket;
    //private String ip = "10.0.2.2";   // 안드로이드 에뮬레이터에서는 localhost가 아니라 10.0.2.2로 접근!!
    //private int port =30000;
    DataInputStream dis;

    private InputStream chat_is;
    private DataInputStream chat_dis;

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

        try {

            chat_is = this.socket.getInputStream();
            chat_dis = new DataInputStream(chat_is);

        }catch (Exception e){

        }
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

        chat_adapter = new Chat_Adapter(getApplicationContext());
        recvThread = new RecvThread();
        intent = getIntent();
        now_room_name = intent.getStringExtra("roomname");
        room_name_title = (TextView)findViewById(R.id.chat_room_name_title);
        room_name_title.setText(now_room_name);
        input_chattext = (EditText)findViewById(R.id.input_chat);
        nowingChat = input_chattext.getText().toString();
        this.recyclerView = (RecyclerView)findViewById(R.id.chat_recyclerview);

        // 현재 날짜 가져오는 것
        now = System.currentTimeMillis();
        date = new Date(now);

        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
        formatDate = simpleDateFormat.format(date);
        String[] nowDate = formatDate.split("/");

        imgDate = nowDate[3]+":";
        imgDate+= nowDate[4];

        System.out.println("시간 : " + imgDate);


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //user_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chat_adapter);


        handler = new Handler(){
            public void handleMessage(Message msg){
                Bundle bundle = new Bundle();
                bundle = msg.getData();

                String name = bundle.getString("name");
                String message = bundle.getString("message");
                String time = bundle.getString("time");
                int index = bundle.getInt("index");
                System.out.println("출력 : " + name + message + time + index);

                System.out.println("나 어댑터에 들어감 : " + index);
                addAdapter(name, message, time, index);
                /*if(name.equals(""))
                chat_adapter.addItem(new MessageContact(name, message, time, index));   // 인덱스 0이면 나임*/
            }
        };

        send_btn = (Button)findViewById(R.id.input_chat_send);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    System.out.println("서버로 보냄 : ");
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    Send_Msg(user_id + " " + "message " + imgDate + " " + now_room_name + "%3" + input_chattext.getText().toString()); // 아이디랑를 보낸다
                    //Send_Msg(user_id + " " + "message " + now_room_name + "%3" + input_chattext.getText().toString()); // 아이디랑를 보낸다

                    input_chattext.setText("");
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.e("client","connectServer() Error");
                    return;
                }


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

        Send_Msg(user_id + " " + "getchat " + now_room_name); // 아이디랑를 보낸다
        count=true;
        recvThread.start();
    }


    public void Send_Msg(String msg){
        /*byte[] bb;
        bb = msg.getBytes();*/
        //Log.e("send","send success");
        try{
            System.out.println("진짜로 보냄 : " + msg);
            dos.writeUTF(msg);
            Log.e("send","send success");
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void addAdapter(String name, String message, String time, int index){
        if(!message.equals("null")) {
            switch (name) {
                case "ahntan":
                    chat_adapter.addItem(new MessageContact("안형우", message, time, index, R.drawable.user));
                    recyclerView.scrollToPosition(chat_adapter.getItemCount() - 1);
                    break;
                case "shbaek":
                    chat_adapter.addItem(new MessageContact("백승환", message, time, index, R.drawable.user2));
                    recyclerView.scrollToPosition(chat_adapter.getItemCount() - 1);
                    break;
                case "jsr":
                    chat_adapter.addItem(new MessageContact("진소린", message, time, index, R.drawable.user3));
                    recyclerView.scrollToPosition(chat_adapter.getItemCount() - 1);
                    break;
                case "sje":
                    chat_adapter.addItem(new MessageContact("송정은", message, time, index, R.drawable.user4));
                    recyclerView.scrollToPosition(chat_adapter.getItemCount() - 1);
                    break;
                case "hkd":
                    chat_adapter.addItem(new MessageContact("한경동", message, time, index, R.drawable.user5));
                    recyclerView.scrollToPosition(chat_adapter.getItemCount() - 1);
                    break;
                default:
                    break;

            }
        }
    }



    class RecvThread extends Thread{
        @Override
        public void run() {
            try {
            while (count) {


                    // 사용자에게 받는 메세지
            /*
             * byte[] b = new byte[128]; dis.read(b); String msg = new String(b);
             */
                    System.out.println("쓰레드 시작22");
                    //String msg = dis.readUTF();
                    String msg3 = chat_dis.readUTF();
                    System.out.println("데이터 받음 자르기시작");
                    msg3 = msg3.trim();
                    String[] msg1 = msg3.split("%3");
                    String cmd = msg1[0];
                    if (cmd.equals("#message")) {
                        System.out.println("첫번째 : " + msg1[0]);
                        System.out.println("두번째 : " + msg1[1]);
                        System.out.println("세번째 : " + msg1[2]);
                        System.out.println("네번째 : " + msg1[3]);
                        System.out.println("5번째 : " + msg1[4]);
                        if(msg1[1].equals(user_id)){

                            Bundle ontimebundle = new Bundle();
                            ontimebundle.putString("name", msg1[1]);
                            ontimebundle.putString("message", msg1[4]);
                            ontimebundle.putString("time", msg1[2]);
                            ontimebundle.putInt("index", 0);
                            Message timermsg = new Message();
                            timermsg.setData(ontimebundle);
                            handler.sendMessage(timermsg);

                      /*  System.out.println("나 어댑터에 들어감 : ");
                        chat_adapter.addItem(new MessageContact(msg1[1], msg1[4], msg1[2], 0));   // 인덱스 0이면 나임*/
                        }
                        else{

                            Bundle ontimebundle = new Bundle();
                            ontimebundle.putString("name", msg1[1]);
                            ontimebundle.putString("message", msg1[4]);
                            ontimebundle.putString("time", msg1[2]);
                            ontimebundle.putInt("index", 1);
                            Message timermsg = new Message();
                            timermsg.setData(ontimebundle);
                            handler.sendMessage(timermsg);

                        /*System.out.println("타인 어댑터에 들어감 : ");
                        chat_adapter.addItem(new MessageContact(msg1[1], msg1[4], msg1[2], 0));   // 인덱스 0이면 나임
                        */
                            //user_arrayList.add(new MessageContact(msg1[1], msg1[4], msg1[2], 1));   // 인덱스 1이면 타인임
                        }

                    }
                    else if(cmd.equals("invite")) {
                        //InviteUser(msg1);
                    }
                    //InMessage(msg);
                    System.out.println(msg3);
                } }
                catch (Exception e) {
                    return;
                }
                // 바깥 catch문끝

            }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try{

            count=false;
            dos.writeUTF("ahntan" + " " + "message " + imgDate + " " + now_room_name + "%3" + "null");
            System.out.println("백버튼 : " + msg3);

        }catch (Exception e){

        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.e("destory", "파괴됨");
    }
}
