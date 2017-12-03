package com.example.thiscord;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by 안탄 on 2017-11-19.
 */

public class Main_JoinStateAdapter extends RecyclerView.Adapter<Main_JoinStateAdapter.UnknownViewHolder>{

    private int userinfo_position;  // 현재 버튼눌린 유저목록 위치
    Context context;
    ArrayList<Contacts> arrayList;


    public Main_JoinStateAdapter(Context context){
        this.context = context;
        arrayList = new ArrayList<>();
    }

    public class UnknownViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView userImg;
        private TextView username;
        private TextView userstats;


        // 뷰홀더 -> UI(?)부분에 해당되는 것들을 뷰에 미리 붙여서 고정
        public UnknownViewHolder(View itemView){
            super(itemView);

            userImg = (ImageView)itemView.findViewById(R.id.user_img);
            username = (TextView)itemView.findViewById(R.id.user_name);
            userstats = (TextView)itemView.findViewById(R.id.user_stat);

            userImg.setOnClickListener(this);
            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view){
            int position = getLayoutPosition();

            // 프로필 눌렀을시 나오는 화면 구현
            Intent intent = new Intent(context, UserInfoActivity.class);
            intent.putExtra("userurl", arrayList.get(position).getUrl());
            intent.putExtra("username", arrayList.get(position).getName());
            intent.putExtra("userstats", arrayList.get(position).getStats());
            intent.putExtra("userbackurl", arrayList.get(position).getBackurl());
            context.startActivity(intent);



        }

    }

    // 포지션값 가져오는 부분
    @Override
    public int getItemCount(){

       return arrayList.size();
    }

    public void addItem(Contacts contacts){
        arrayList.add(contacts);
        notifyDataSetChanged();

    }

    @Override //아이템을 위한 뷰를 만들어서 뷰홀더에 넣어서 리턴
    public Main_JoinStateAdapter.UnknownViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contatctview = inflater.inflate(R.layout.fragment_main_join_state_item, parent, false);
        UnknownViewHolder viewHolder = new UnknownViewHolder(contatctview);

        return viewHolder;
    }

    @Override   // 뷰홀더의 뷰에 position에 해당되는 데이터를 넣음 = 데이터 처리하는곳
    public void onBindViewHolder(Main_JoinStateAdapter.UnknownViewHolder viewHolder, int position){

        Glide.with(context.getApplicationContext()).load(arrayList.get(position).getUrl()).into(viewHolder.userImg);
        viewHolder.username.setText(arrayList.get(position).getName());
        viewHolder.userstats.setText(arrayList.get(position).getStats());


      if(viewHolder.userstats.getText().equals("온라인")){
            //viewHolder.userstats.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            viewHolder.userstats.setTextColor(Color.parseColor("#5d52e8"));

            viewHolder.userstats.setText(arrayList.get(position).getStats());

        }
       if(viewHolder.userstats.getText().equals("자리비움")){
            viewHolder.userstats.setTextColor(Color.parseColor("#BBA293"));
            viewHolder.userstats.setText(arrayList.get(position).getStats());

        }



    }

}
