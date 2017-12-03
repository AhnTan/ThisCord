package com.example.thiscord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Create_Room_Activity extends Activity {

    private Button createbtn;
    private Button canclebtn;
    private EditText create_room_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__room_);

        create_room_name = (EditText)findViewById(R.id.create_room_edit);


        createbtn = (Button)findViewById(R.id.room_create_ok);
        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("Result", create_room_name.getText().toString());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


        canclebtn = (Button)findViewById(R.id.room_create_cancle);
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("Result", "cancle");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


    }
}
