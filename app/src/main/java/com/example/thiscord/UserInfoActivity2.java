package com.example.thiscord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class UserInfoActivity2 extends AppCompatActivity {

    private ImageView user_info2_imgview;
    private int user_img_url2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info2);

        Intent intent = getIntent();
        user_img_url2 = intent.getIntExtra("userurl", 0);


        System.out.println("유저 url : " + user_img_url2);



        // 유저 모양 메인 사진
        user_info2_imgview = (ImageView)findViewById(R.id.user_info2_userimgview);
        //Glide.with(getApplicationContext()).load(user_img_url2).into(user_info2_imgview);
        user_info2_imgview.setBackgroundResource(user_img_url2);


    }
}
