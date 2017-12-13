package com.example.thiscord;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by 안탄 on 2017-11-19.
 */

public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.UnknownViewHolder>{

    private int userinfo_position;  // 현재 버튼눌린 유저목록 위치
    Context context;
    ArrayList<MessageContact> arrayList;


    public Chat_Adapter(Context context){
        this.context = context;
        arrayList = new ArrayList<>();
    }

    public class UnknownViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout chat_layout;
        private LinearLayout my_layout;
        private LinearLayout my_layout2;

        private TextView chat_text;

        private TextView chat_name;
        private TextView chat_time;
        private ImageView chat_img;


        private TextView chat_name2;
        private TextView chat_time2;
        private ImageView chat_img2;


        // 뷰홀더 -> UI(?)부분에 해당되는 것들을 뷰에 미리 붙여서 고정
        public UnknownViewHolder(View itemView){
            super(itemView);

            chat_layout = (LinearLayout)itemView.findViewById(R.id.chat_layout);
            my_layout = (LinearLayout)itemView.findViewById(R.id.my_layout);
            my_layout2 = (LinearLayout)itemView.findViewById(R.id.my_layout2);

            chat_text = (TextView)itemView.findViewById(R.id.message_text);

            chat_img = (ImageView)itemView.findViewById(R.id.chat_img);
            chat_name = (TextView)itemView.findViewById(R.id.chat_name);
            chat_time = (TextView)itemView.findViewById(R.id.chat_time);

            chat_img2 = (ImageView)itemView.findViewById(R.id.chat_img2);
            chat_name2 = (TextView)itemView.findViewById(R.id.chat_name2);
            chat_time2 = (TextView)itemView.findViewById(R.id.chat_time2);
        }


    }

    // 포지션값 가져오는 부분
    @Override
    public int getItemCount(){
        return arrayList.size();
    }

    public void addItem(MessageContact contacts){
        // 나일때는 오른쪽에 띄워야 하므로
        if(contacts.index==0){
            System.out.println("인덱스0");
            arrayList.add(new MessageContact("null", "null", "null", 3, 3));
            arrayList.add(contacts);
        }
        // 상대방 왼쪽에 띄우기
        else if(contacts.index==1){
            System.out.println("인덱스1");
            arrayList.add(contacts);
            arrayList.add(new MessageContact("null", "null", "null", 3, 3));
        }
        notifyItemInserted(arrayList.size()-1);

    }

    @Override //아이템을 위한 뷰를 만들어서 뷰홀더에 넣어서 리턴
    public Chat_Adapter.UnknownViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contatctview = inflater.inflate(R.layout.activity_chat_item, parent, false);
        UnknownViewHolder viewHolder = new UnknownViewHolder(contatctview);

        return viewHolder;
    }

    @Override   // 뷰홀더의 뷰에 position에 해당되는 데이터를 넣음 = 데이터 처리하는곳
    public void onBindViewHolder(final Chat_Adapter.UnknownViewHolder viewHolder,final int position){

        viewHolder.chat_layout.setGravity(Gravity.RIGHT);

        // 내가 보냈을때
        if(arrayList.get(position).getIndex()==0){
            Log.e("포지션>>", "캬캬캬캬" + position);
            Log.e("포지션 인덱스 : ", "뭐지 " + arrayList.get(position).getIndex());
            viewHolder.chat_layout.setVisibility(View.VISIBLE);
            viewHolder.my_layout2.setVisibility(View.VISIBLE);
            viewHolder.chat_text.setText(arrayList.get(position).getMessage());
            viewHolder.chat_name2.setText(arrayList.get(position).getUser_name());
            viewHolder.chat_time2.setText(arrayList.get(position).getTime());
            Glide.with(context).load(arrayList.get(position).getUrl()).into(viewHolder.chat_img2);

        }
        // 타인이 보냈을때
        else if(arrayList.get(position).getIndex()==1) {
            System.out.print("포지션 찍어봐 : " + " >> " + position);
            viewHolder.chat_layout.setVisibility(View.VISIBLE);
            viewHolder.my_layout.setVisibility(View.VISIBLE);
            viewHolder.chat_text.setText(arrayList.get(position).getMessage());
            viewHolder.chat_name.setText(arrayList.get(position).getUser_name());
            viewHolder.chat_time.setText(arrayList.get(position).getTime());
            Glide.with(context).load(arrayList.get(position).getUrl()).into(viewHolder.chat_img);
        }

        System.out.print("포지션 찍어봐 : " + arrayList.get(position).toString() + " >> " + position);
    }

    public void removeItem(){
        int size = arrayList.size();
        for(int i=0; i<size; i++) {
            arrayList.remove(0);
            notifyItemRemoved(0);
        }
    }

}

/*

         user_arrayList.add(new Contacts(R.drawable.user, R.drawable.background, "ahntan", "안형우", "오프라인"));
         user_arrayList.add(new Contacts(R.drawable.user2, R.drawable.background2, "shbaek","백승환", "오프라인"));
         user_arrayList.add(new Contacts(R.drawable.user3, R.drawable.background3, "jsr", "진소린", "오프라인"));
         user_arrayList.add(new Contacts(R.drawable.user4, R.drawable.background2, "sje","송정은", "오프라인"));
         user_arrayList.add(new Contacts(R.drawable.user5, R.drawable.background1, "hkd","한경동", "오프라인"));*/
