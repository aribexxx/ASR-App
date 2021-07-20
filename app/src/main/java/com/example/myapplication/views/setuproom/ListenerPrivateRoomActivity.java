package com.example.myapplication.views.setuproom;

import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.views.parentclass.ListenerRoomActivity;
import com.example.myapplication.models.RoomEntry;

public class ListenerPrivateRoomActivity extends ListenerRoomActivity {
    RoomEntry room;
    EditText recognizeResult;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initView();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(){
        setContentView(R.layout.mh_listener_privateroom_activity);

        // 初始化相应的控件
        //set UI components view
        leaveRoom = findViewById(R.id.leaveroom_button);
        //set leave room button click
        leaveRoom.setOnClickListener(v -> {
            super.closeWebSocket();
            finish();
        });

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        cancel = (Button)findViewById(R.id.cancel);
        recognizeState = (TextView) findViewById(R.id.recognize_state);
        volume = (TextView) findViewById(R.id.volume);
        recognizeResult = (EditText) findViewById(R.id.recognize_result);
        //recognizeResult.setText("This is aaaa listener 私密 room");
        recognizeResult.setFocusable(false);
        recognizeResult.setScroller(new Scroller(getApplicationContext()));
        recognizeResult.setVerticalScrollBarEnabled(true);
        recognizeResult.setMovementMethod(new ScrollingMovementMethod());

        handler = new Handler(getMainLooper()); // used by parent listenerRoomActivity

        super.passHandlerResult(recognizeResult, handler);
    }


    public void setModelType(String modelType) {
        if(room!=null){
            room.setModelType(modelType);}
        else{
            Log.d("LPrivateRoomActivity","room is not initiated");
        }
    }

}
