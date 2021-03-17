package com.example.myapplication.asr.parentclass;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

// parent class for public and private listener room
// get meetinglist from server and display all existing rooms

//  公开私密房间区别 : customize UI
//  公共模块：socket获取字幕+ 双语字幕展示

public class ListenerRoomActivity extends AppCompatActivity {

    protected Toolbar toolBar;
    protected MaterialButton leaveRoom;
    protected Button start;
    protected Button stop;
    protected Button cancel;

    protected TextView recognizeState;
    protected TextView volume;

    protected EditText recognizeResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView(){

    }

}