package com.example.myapplication.views.setup_room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.asr.realtime.ASRRoomActivity;
import com.example.myapplication.models.RoomEntry;
import com.tencent.iot.speech.app.CommonConst;

public class ListenerPrivateRoomActivity extends ASRRoomActivity {
    RoomEntry room;

    public ListenerPrivateRoomActivity(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void initView(){
        setContentView(R.layout.mh_privateroom_activity);

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
        handler = new Handler(getMainLooper());
        recognizeResult.setText("This is aaaa private room");
    }

    public void initRoomEtry(){
        this.room=new RoomEntry.Builder().build();
    }

    public RoomEntry getRoomEtry(){
           return  room;
    }

    public void setPasswordPrivateRoom(String password){
        this.room.setPassword(password);
    }
}
