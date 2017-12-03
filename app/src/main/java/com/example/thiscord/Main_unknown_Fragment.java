package com.example.thiscord;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Main_unknown_Fragment extends Fragment {
    private RecyclerView recyclerView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
        View view = inflater.inflate(R.layout.fragment_main_unknown_, container, false);
        TextView tv = (TextView)view.findViewById(R.id.tt);
        tv.setTextColor(Color.WHITE);
        //recyclerView = (RecyclerView)view.findViewById(R.id.main_unknown_recycleview);
        return view;
    }


    // 최종적으로 액티비티에 붙여주는곳
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }
}
