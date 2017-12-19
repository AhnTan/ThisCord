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
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity{

    public Recv_Voice recev;
    public Send_Voice send;

    private static boolean count=true;



    private SharedPreferences sharedPreferences;
    private TextView room_name_title;
    private EditText input_chattext;
    private Button send_btn;
    private Button invite_btn;

    private Button mic_on_btn;
    private Button mic_off_btn;
    private Button spk_on_btn;
    private Button spk_off_btn;


    private Button call_btn;

    private String user_id;
    private int user_port;

    private int now_room_num;
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

    private String name;
    private String time;
    private String message;
    private int index;

    private RecvThread recvThread;
    private RecyclerView recyclerView;

    private ArrayList<InChatRoomUserList> userList = new ArrayList<>();
    private ArrayList<InChatRoomUserList> user_arrayList = new ArrayList<>();
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

        System.out.println("유저정보 : " + UserDao.user_ip + " " + UserDao.id + " " + UserDao.name + " " + UserDao.port1 + " " + UserDao.port2 + " ");

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
        now_room_num = intent.getIntExtra("roomnum", 0);
        Toast.makeText(getApplicationContext(), "들어온 방번호 : " + now_room_num, Toast.LENGTH_SHORT).show();
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

                name = bundle.getString("name");
                message = bundle.getString("message");
                time = bundle.getString("time");
                index = bundle.getInt("index");
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

                    for(int t=0; t<userList.size(); t++){
                        System.out.println("여기요 : " + userList.get(t).getId() + " ip : " + userList.get(t).getIp() + " " + userList.get(t).getPort());
                    }
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
                for(int t=0; t<user_arrayList.size(); t++){
                    System.out.println("여기요2 : " + user_arrayList.get(t).getId() + " ip : " + user_arrayList.get(t).getIp() + " " + user_arrayList.get(t).getPort());
                }
                Intent intent = new Intent(getApplicationContext(), Invite_Room_Activity.class);
                System.out.println("인텐트 전 : " + now_room_name);
                intent.putExtra("room_name", now_room_name);
                startActivity(intent);

            }
        });


        // 전화 연결 버튼
        call_btn = (Button)findViewById(R.id.chat_call_btn);
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_btn.setVisibility(View.INVISIBLE);
                mic_on_btn.setVisibility(View.VISIBLE);
                spk_on_btn.setVisibility(View.VISIBLE);


                recev = new Recv_Voice(user_port+now_room_num);
                recev.start();


                for(int i=0; i<user_arrayList.size(); i++){
                    send = new Send_Voice(user_arrayList.get(i).getIp(), user_arrayList.get(i).getPort()+now_room_num);
                    send.start();
                }

            }
        });





        // 마이크 on -> off
        mic_on_btn = (Button)findViewById(R.id.chat_mic_on_btn);
        mic_on_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mic_on_btn.setVisibility(View.INVISIBLE);
                mic_off_btn.setVisibility(View.VISIBLE);

                // 마이크 쓰레드 닫는 거 구현
                send.interrupt();
            }
        });

        // 마이크 off -> on
        mic_off_btn = (Button)findViewById(R.id.chat_mic_off_btn);
        mic_off_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mic_on_btn.setVisibility(View.VISIBLE);
                mic_off_btn.setVisibility(View.INVISIBLE);

                // 마이크 연결
                for(int i=0; i<user_arrayList.size(); i++){
                    send = new Send_Voice(user_arrayList.get(i).getIp(), user_arrayList.get(i).getPort()+now_room_num);
                    send.start();
                }

            }
        });


        // 듣기 on -> off
        spk_on_btn = (Button)findViewById(R.id.chat_spk_on_btn);
        spk_on_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spk_on_btn.setVisibility(View.INVISIBLE);
                spk_off_btn.setVisibility(View.VISIBLE);

                recev.rere = false;
                recev.track.stop();
                recev.socket.disconnect();
                recev.socket.close();
                recev.interrupt();
            }
        });


        // 듣기 off -> on
        spk_off_btn = (Button)findViewById(R.id.chat_spk_off_btn);
        spk_off_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spk_on_btn.setVisibility(View.VISIBLE);
                spk_off_btn.setVisibility(View.INVISIBLE);

                // 전화 연결 쓰레드 시작
                recev = new Recv_Voice(user_port+now_room_num);
                recev.start();
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

                        }

                    }
                    else if(cmd.equals("invite")) {
                        //InviteUser(msg1);
                    }
                    else if(cmd.equals("#userlist")){
                        for(int i = 1; i<msg1.length; i+=3){
                            // 사용자 이름, ip, port 추가 해야됨
                            int count=0;
                            for(int j=0; j<userList.size(); j++){
                                if(userList.get(j).getId().equals(msg1[i])){
                                    count++;
                                }
                            }

                            if(count==0) {
                                userList.add(new InChatRoomUserList(msg1[i], msg1[i + 1], Integer.parseInt(msg1[i + 2])));
                            }
                            //main_roomlistAdapter.addItem(new RoomContacts(Integer.parseInt(gmsg[i+1]) ,gmsg[i]));
                        }


                        // 전화 연결 쓰레드 시작
                        for(int k=0; k<userList.size(); k++){
                            // 지금 사용자의 port를 찾아서 초기화
                            if(userList.get(k).getId().equals(user_id)){
                                user_port = userList.get(k).getPort();
                            }

                            else if(!userList.get(k).getId().equals(user_id)){

                                int count=0;
                                for(int m=0; m<user_arrayList.size(); m++){
                                    if(user_arrayList.get(m).getId().equals(userList.get(k).getId())){
                                        count++;
                                    }
                                }

                                if(count==0) {
                                    //나를 제외한 나머지 사람들의 정보를 저장해놓은 리스트
                                    user_arrayList.add(new InChatRoomUserList(userList.get(k).getId(), userList.get(k).getIp(), userList.get(k).getPort()));
                                }

                            }
                        }

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
            // 채팅 닫기
            count=false;
            dos.writeUTF("ahntan" + " " + "message " + imgDate + " " + now_room_name + "%3" + "null");
            System.out.println("백버튼 : " + msg3);

            // 음성 채팅 다 닫기
            try {
                recev.rere = false;
                recev.track.stop();
                recev.socket.disconnect();
                recev.socket.close();
                recev.interrupt();
                send.interrupt();
            }catch (Exception e){

            }

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
