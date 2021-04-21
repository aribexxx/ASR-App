package com.example.myapplication.views.setuproom;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.asr.parentclass.ListenerRoomActivity;
import com.example.myapplication.models.RoomEntry;
import com.example.myapplication.views.customview.subtitle.SubtitleView;

import anylife.scrolltextview.ScrollTextView;

public class ListenerPublicRoomActivity extends ListenerRoomActivity {
    RoomEntry room;
    //SubtitleView recognizeResult;
    protected Handler handler;
    ScrollTextView scrollText;

    public ListenerPublicRoomActivity() {
        this.room=new RoomEntry.Builder().build();
   }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public void initView(){
        super.initView();
        setContentView(R.layout.mh_listener_publicroom_activity);
        // 初始化相应的控件
        //set UI components view
        leaveRoom = findViewById(R.id.leaveroom_button);
        //recognizeResult = (SubtitleView) findViewById(R.id.recognize_result);
        //set leave room button click
        leaveRoom.setOnClickListener(v -> {
            super.closeWebSocket();
            finish();
        });
         //set scrolltext
        scrollText = findViewById(R.id.scrolltxt);
        scrollText.setSpeed(4);
        scrollText.setTextColor(0xffad43ae);
        //recognizeResult.setSubtitle();
        //recognizeResult.init();
        //recognizeResult.setText("This is aaaa listener public room");
        handler = new Handler(getMainLooper()); // used by parent listenerRoomActivity
        // pass the instance to parent listenerRoomActivity
        super.passHandlerResult(scrollText, handler);
    }


}
