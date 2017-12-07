package com.example.thiscord;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;


public class Main_roomlist_Fragment extends Fragment {
    private FloatingActionButton floatingActionButton;
    private RecyclerView room_recyclerView ;
    private static final int request_code = 0;
    private Main_roomlistAdapter main_roomlistAdapter;

    private String room_id; // 방이름
    private String login_user; // 접속한 유저 아이디
    private String[] gmsg; // 받은 데이터 split으로 잘라서 넣어두는곳
    SharedPreferences sharedPreferences;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
        View view = inflater.inflate(R.layout.fragment_main_roomlist_, container, false);
        room_recyclerView = (RecyclerView)view.findViewById(R.id.room_recycleview);
        return view;
    }


    // 최종적으로 액티비티에 붙여주는곳
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        room_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        main_roomlistAdapter = new Main_roomlistAdapter(getActivity().getApplicationContext());
        room_recyclerView.setAdapter(main_roomlistAdapter);

        sharedPreferences = this.getActivity().getSharedPreferences("user_id", Context.MODE_PRIVATE);
        login_user = sharedPreferences.getString("userid", "01012345678");


        floatingActionButton = (FloatingActionButton)getActivity().findViewById(R.id.room_add_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            // 방 추가하는 버튼
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), " 방생성 버튼이다아아!!!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), Create_Room_Activity.class);
                startActivityForResult(intent, request_code);

            }
        });

        //connectServer();
    }


    // 다른 액티비티에서 넘어온 값을 처리해줌(-> 방생성하기 액티비티)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        room_id = "";
        room_id += data.getStringExtra("Result");
        // 플로팅 버튼에서 취소버튼이 아닌 확인 버튼을 누르게 되면 방목록을 추가함
        if(!room_id.equals("cancle")){
          main_roomlistAdapter.addItem(new RoomContacts("1",room_id));
        }
    }






}




