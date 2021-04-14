package com.example.myapplication.asr.parentclass;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.myapplication.R;
import com.example.myapplication.util.network.WebSocket;
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

    // var used by ws and socketListImpl
    protected EditText recognizeResult;
    protected WebSocket websocket;
    protected Handler handler;

    public class SocketListImpl implements WebSocket.SocketListener {

        public void  onMessage(String s){
            System.out.println("this is implemented listener: "+s);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    recognizeResult.setText(s);
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initView();
        // create ws connection
        websocket = new WebSocket(new SocketListImpl());
        websocket.createWebSocketClient("2", "4");
    }

    public void passHandlerResult(EditText passedRecognizeResult, Handler passedHandler){
        handler = passedHandler;
        recognizeResult = passedRecognizeResult;
    }

    public void initView(){
    }

}