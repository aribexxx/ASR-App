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
import com.example.myapplication.asr.parentclass.SpeakerASRRoomActivity;
import com.example.myapplication.models.RoomEntry;
import com.tencent.iot.speech.app.CommonConst;

public class SpeakerPrivateRoomActivity extends SpeakerASRRoomActivity {
    RoomEntry room;

    public SpeakerPrivateRoomActivity(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.mh_speaker_privateroom_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(CommonConst.config);

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
        recognizeResult.setText("This is aaaa speaker private room");
    }

    public void initRoomEtry(String roomTitle,String roomDescription) {
        this.room = new RoomEntry.Builder().roomTitle(roomTitle).roomDescription(roomDescription).build();
    }

    public RoomEntry getRoomEtry() {
        return  room;
    }

    public void setPasswordPrivateRoom(String password) {
       room.setPwd(password);
    }
}
