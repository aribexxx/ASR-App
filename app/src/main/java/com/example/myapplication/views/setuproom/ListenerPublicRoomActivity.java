package com.example.myapplication.views.setuproom;

import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Scroller;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.asr.parentclass.ListenerRoomActivity;
import com.example.myapplication.models.RoomEntry;

public class ListenerPublicRoomActivity extends ListenerRoomActivity {
    RoomEntry room;
    EditText recognizeResult;
    protected Handler handler;

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
        setContentView(R.layout.mh_listener_publicroom_activity);

        // 初始化相应的控件
        //set UI components view
        leaveRoom = findViewById(R.id.leaveroom_button);
        //set leave room button click
        leaveRoom.setOnClickListener(v -> {
            super.closeWebSocket();
            finish();
        });

        recognizeResult = (EditText) findViewById(R.id.recognize_result);
        recognizeResult.setFocusable(false);
        recognizeResult.setScroller(new Scroller(getApplicationContext()));
        recognizeResult.setVerticalScrollBarEnabled(true);
        recognizeResult.setMovementMethod(new ScrollingMovementMethod());
        //recognizeResult.setText("This is aaaa listener public room");
        handler = new Handler(getMainLooper()); // used by parent listenerRoomActivity

        // pass the instance to parent listenerRoomActivity
        super.passHandlerResult(recognizeResult, handler);
    }


}
