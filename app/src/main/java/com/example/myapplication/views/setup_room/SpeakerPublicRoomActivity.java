package com.example.myapplication.views.setup_room;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.myapplication.asr.realtime.ASRRoomActivity;
import com.example.myapplication.models.RoomEntry;

public class PublicRoomActivity extends ASRRoomActivity {



    RoomEntry room;
    EditText recognizeResult;

    public PublicRoomActivity(){
       this.room=new RoomEntry();
   }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



}
