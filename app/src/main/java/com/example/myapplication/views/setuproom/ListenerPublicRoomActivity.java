package com.example.myapplication.views.setuproom;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.asr.parentclass.ListenerRoomActivity;
import com.example.myapplication.models.RoomEntry;

public class ListenerPublicRoomActivity extends ListenerRoomActivity {
    RoomEntry room;
    EditText recognizeResult;

    public ListenerPublicRoomActivity() {
        this.room=new RoomEntry.Builder().build();
   }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView(){
        setContentView(R.layout.mh_listener_publicroom_activity);

        // 初始化相应的控件
        //set UI components view
        leaveRoom = findViewById(R.id.leaveroom_button);
        //set leave room button click
        leaveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        cancel = (Button)findViewById(R.id.cancel);
        recognizeState = (TextView) findViewById(R.id.recognize_state);
        volume = (TextView) findViewById(R.id.volume);
        recognizeResult = (EditText) findViewById(R.id.recognize_result);
        recognizeResult.setText("This is aaaa listener public room");
    }


}
