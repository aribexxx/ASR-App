package com.example.myapplication.views.setuproom;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.views.parentclass.SpeakerASRRoomActivity;
import com.example.myapplication.models.RoomEntry;

public class SpeakerPrivateRoomActivity extends SpeakerASRRoomActivity {
    private Toolbar returnBar;
    //need to set this attribute private in case privacy leak.

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        super.initView();
        returnBar =(Toolbar) findViewById(R.id.return_bar);
        returnBar.setTitle("Speaker Private Room");
        recognizeResult.setText("This is aaaa speaker private room");
    }

    public void setModelType(String modelType) {
        if(room!=null){
            room.setModelType(modelType);}
        else{
            Log.d("SPrivateRoomActivity","room is not initiated");
        }
    }
    public String getPassword() {
            return room.getPwd();
    }
    public void setPassword(String pwd) {
            room.setPwd(pwd);}
}
