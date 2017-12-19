package com.example.thiscord;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 안탄 on 2017-11-24.
 */

public class Main_roomlistAdapter extends RecyclerView.Adapter<Main_roomlistAdapter.ViewHolder> {
    private int userinfo_position ;  // 현재 버튼눌린 유저목록 위치
    Context context;
    ArrayList<RoomContacts> arrayList;


    public Main_roomlistAdapter(Context context){
        this.context = context;
        arrayList = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView joinImg;
        private TextView roomname;


        // 뷰홀더 -> UI(?)부분에 해당되는 것들을 뷰에 미리 붙여서 고정
        public ViewHolder(View itemView){
            super(itemView);
                //getLayoutPosition(); 현재 리스트 방 현재 위치
            joinImg = (ImageView)itemView.findViewById(R.id.room_join);
            joinImg.setOnClickListener(this);

            roomname = (TextView)itemView.findViewById(R.id.room_name);
            roomname.setText("으앙");



        }

        @Override
        public void onClick(View view){
            int position = getLayoutPosition();

            Intent intent2 = new Intent(context, ChatActivity.class);
            //intent2.putExtra("key", "value");
            intent2.putExtra("roomnum", arrayList.get(position).getRoom_num());
            intent2.putExtra("roomname", arrayList.get(position).getRoom_tile());
            System.out.println("방 이름 : " + arrayList.get(position).getRoom_tile());
            context.startActivity(intent2);


        }

    }

    // 포지션값 가져오는 부분
    @Override
    public int getItemCount(){

        return arrayList.size();
    }

    public void addItem(RoomContacts contacts){
        arrayList.add(contacts);
        notifyDataSetChanged();

    }

    @Override //아이템을 위한 뷰를 만들어서 뷰홀더에 넣어서 리턴
    public Main_roomlistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contatctview = inflater.inflate(R.layout.fragment_main_roomlist_item, parent, false);
        Main_roomlistAdapter.ViewHolder viewHolder = new Main_roomlistAdapter.ViewHolder(contatctview);

        return viewHolder;
    }

    @Override   // 뷰홀더의 뷰에 position에 해당되는 데이터를 넣음 = 데이터 처리하는곳
    public void onBindViewHolder(Main_roomlistAdapter.ViewHolder viewHolder, int position){

        //Glide.with(context.getApplicationContext()).load(arrayList.get(position).getUrl()).into(viewHolder.userImg);
        viewHolder.roomname.setText(arrayList.get(position).getRoom_tile());




    }


    public void removeItem(){
        int size = arrayList.size();
        for(int i=0; i<size; i++) {
            arrayList.remove(0);
            notifyItemRemoved(0);
        }
    }
}
