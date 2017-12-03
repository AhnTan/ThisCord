package com.example.thiscord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class UserInfoActivity extends AppCompatActivity {

    private ImageView user_info_imgview;
    private ImageView user_back_imgview;
    private TextView user_name_textview;
    private TextView user_stats_textview;

    private int user_img_url;
    private int user_backimg_url;
    private String user_name;
    private String user_stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Intent intent = getIntent();
        user_img_url = intent.getIntExtra("userurl", 0);
        user_backimg_url = intent.getIntExtra("userbackurl", 0);
        user_name = intent.getStringExtra("username");
        user_stats = intent.getStringExtra("userstats");

        System.out.println("유저 url : " + user_img_url);
        System.out.println("유저 name : " + user_name);
        System.out.println("유저 stats : " + user_stats);


        // 유저 동그라미 모양 메인 사진
        user_info_imgview = (ImageView)findViewById(R.id.user_info_userimgview);
        Glide.with(getApplicationContext()).load(user_img_url).into(user_info_imgview);
        user_info_imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserInfoActivity2.class);
                intent.putExtra("userurl", user_img_url);
                startActivity(intent);
            }
        });

        // 배경이미지 처리
        user_back_imgview = (ImageView)findViewById(R.id.user_background_imgview);
        user_back_imgview.setBackgroundResource(user_backimg_url);
        //Glide.with(getApplicationContext()).load(user_backimg_url).into(user_back_imgview);

        // 유저 이름
        user_name_textview = (TextView)findViewById(R.id.user_name_textview);
        user_name_textview.setText(user_name);

        // 유저 상태 - 얘는 빼야 될 수도 있음
        user_stats_textview = (TextView)findViewById(R.id.user_stats_textview);
        user_stats_textview.setText(user_stats);

    }
}
