package com.example.myapplication.views.setup_room;

import android.os.Bundle;
import android.widget.EditText;
import androidx.annotation.Nullable;
import com.example.myapplication.asr.realtime.ASRRoomActivity;
import com.example.myapplication.models.RoomEntry;

public class SpeakerPublicRoomActivity extends ASRRoomActivity {
    RoomEntry room;
    EditText recognizeResult;

    public SpeakerPublicRoomActivity() {
        this.room = new RoomEntry.Builder().build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



}
