package com.example.thiscord;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.thiscord.LoginActivity.dos;

//사람 초대하는 액티비티
public class Invite_Room_Activity extends Activity {

    private Button invitebtn;
    private Button canclebtn;
    private EditText invite_invite_name;
    private Intent resultIntent;

    private String room_id; // 방이름
    private String invite_user; // 초대받는사람
    private String login_user; // 접속한 유저 아이디
    private String[] gmsg; // 받은 데이터 split으로 잘라서 넣어두는곳
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite__room_);

        resultIntent = new Intent();
        Intent intent = getIntent();
        room_id = intent.getStringExtra("room_name");
        System.out.println("룸인바이트 : " + room_id);

        sharedPreferences = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        login_user = sharedPreferences.getString("userid", "01012345678");
        System.out.println("인바이트 : " + login_user);

        invite_invite_name = (EditText)findViewById(R.id.invite_room_edit);

        // 방생성하기 버튼입니다. 여기서 방생성 제목에 입력한 텍스트를 다시 Main_roomlist_Fragment 쪽의 OnresultActivity로 값(방제목)을 보냅니다
        invitebtn = (Button)findViewById(R.id.room_invite_ok);
        invitebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invite_user = invite_invite_name.getText().toString();
                connectServer();
            }
        });


        canclebtn = (Button)findViewById(R.id.room_invite_cancle);
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //resultIntent  = new Intent();
                resultIntent.putExtra("Result", "cancle");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        // 뒤로가기 버튼시 처리

    }


    public void connectServer(){

        new Thread(){
            @Override
            public void run(){
                //Toast.makeText(getApplicationContext(),"Connect Server",Toast.LENGTH_LONG).show();
                try {

                    Send_Msg(login_user + " " + "invite " + room_id + " " + invite_user); // 아이디랑를 보낸다
                    System.out.println("방생성클릭버튼2 : " + login_user + " " + "invite " + room_id + " " + invite_user );
                    finish();
                    /*
                    String msg = dis.readUTF();

                    System.out.println("넘어온값 전");
                    System.out.println("넘어온값 : " + msg);

                    gmsg = msg.split(" ");

                    control_Msg(gmsg[0]);*/

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("client","connectServer() Error");
                    return;
                }
            }
        }.start();
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

   /* public void control_Msg(String str){ //
        if(str.equals("#create")){  // login success
            System.out.println("InviteActivity login!!!");

            resultIntent.putExtra("Result", gmsg[1]);
            setResult(RESULT_OK, resultIntent);
            finish();

        }
        else if(str.equals("#NOK")){ // pwd error
            System.out.println("password error!");
        }
    }*/

    // 뒤로가기 버튼 처리
    @Override
    public void onBackPressed(){
        resultIntent.putExtra("Result", "cancle");
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
