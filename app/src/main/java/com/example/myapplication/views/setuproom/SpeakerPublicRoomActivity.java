package com.example.myapplication.views.setuproom;

import android.os.Bundle;
import android.widget.EditText;
import androidx.annotation.Nullable;
import com.example.myapplication.asr.parentclass.SpeakerASRRoomActivity;
import com.example.myapplication.models.RoomEntry;

public class SpeakerPublicRoomActivity extends SpeakerASRRoomActivity {
    RoomEntry room;
    EditText recognizeResult;

    public SpeakerPublicRoomActivity() {
        this.room = new RoomEntry.Builder().build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void initRoomEtry(String roomTitle,String roomDescription) {
        this.room = new RoomEntry.Builder().roomTitle(roomTitle).roomDescription(roomDescription).build();
    }


}
