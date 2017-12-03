package com.example.thiscord;

import android.content.Intent;
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

       /* main_roomlistAdapter.addItem(new Contacts(R.drawable.user, R.drawable.background1, "안형우", "온라인")); // 이미지 url, 이름 name
        main_roomlistAdapter.addItem(new Contacts(R.drawable.user2, R.drawable.background2, "백승환", "오프라인")); // 이미지 url, 이름 name
        main_roomlistAdapter.addItem(new Contacts(R.drawable.user3, R.drawable.background3, "차연경", "자리비움")); // 이미지 url, 이름 name
*/
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String room_id = "";
        room_id += data.getStringExtra("Result");
        // 플로팅 버튼에서 취소버튼이 아닌 확인 버튼을 누르게 되면 방목록을 추가함
        if(!room_id.equals("cancle")){
            main_roomlistAdapter.addItem(new RoomContacts("1", room_id)); // 이미지 url, 이름 name
        }
    }

}




