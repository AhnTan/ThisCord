package com.example.thiscord;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {
    private String login_id;
    private String login_pwd;

    private String recv_msg; // 받은 메세지

    public static Socket socket;
    public static DataInputStream dis;
    public static InputStream is;
    public static DataOutputStream dos;
    public static OutputStream os;

    private Button SetButton;
    private EditText IDText;
    private EditText PWDText;
    private Button LoginButton;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String ip ="223.194.159.58";
    //private String ip = "10.0.2.2";   // 안드로이드 에뮬레이터에서는 localhost가 아니라 10.0.2.2로 접근!!

    private int port =30000;
    private Thread thread;

//첫번째 테스트
    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences("user_id", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        IDText = (EditText)findViewById(R.id.username);
        PWDText = (EditText)findViewById(R.id.password);
        LoginButton = (Button)findViewById(R.id.loginbutton);
        SetButton = (Button)findViewById(R.id.settingButton);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"login processing",Toast.LENGTH_LONG).show();
                login_id = IDText.getText().toString();
                // SharedPreference를 통해 아이디를 저장(다른 곳에서도 쓰기 위하여)
                editor.putString("userid", login_id);
                editor.commit();
                login_pwd = PWDText.getText().toString();
                connectServer();
            }
        });
        SetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void connectServer(){

        new Thread(){
            @Override
            public void run(){
                //Toast.makeText(getApplicationContext(),"Connect Server",Toast.LENGTH_LONG).show();
                try {

                    socket = new Socket(ip, port);
                    is = socket.getInputStream();
                    dis = new DataInputStream(is);
                    os = socket.getOutputStream();
                    dos = new DataOutputStream(os);
                    System.out.println("트라이는 들어옴");
                    Send_Msg(login_id+" "+login_pwd); // 아이디랑를 보낸다

                  /*  byte[] b = new byte[1024];
                    dis.read(b);

                    String msg = new String(b, "euc-kr");
*/
                    String msg = dis.readUTF();
                    System.out.println(msg);
                    String[] gmsg = msg.split(" ");

                    control_Msg(gmsg[0]);

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

    private void control_Msg(String str){ //
        if(str.equals("#OK")){  // login success
            System.out.println("login!!!");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else if(str.equals("#NOK")){ // pwd error
            System.out.println("password error!");
        }
        else
            System.out.println("id/pw error!");
    }


   /* class Recv_Msg implements Runnable{
        @Override
        public void run() {
            Boolean flag=true;
            while(flag) {
                try{
                    byte[] b = new byte[1024];
                    dis.read(b);
                    String msg = new String(b, "euc-kr");
                  //  String msg = new String(b,0,b.length);
                    String[] gmsg;
                    gmsg = msg.split(" ");
                    System.out.println(gmsg);
                    recv_msg = gmsg[0];
                   // recv_msg = msg;

                    //mHandler.sendEmptyMessage(0);
                }
                catch(IOException e) {
                    //e.printStackTrace();
                    //status = false;
                    try {
                        os.close();
                        is.close();
                        dos.close();
                        dis.close();
                        socket.close();
                        break;
                    } catch (IOException e1) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }*/
}
