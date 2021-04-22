package com.example.myapplication.asr.parentclass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.util.UserLocalStore;
import com.example.myapplication.util.network.WebSocket;
import com.example.myapplication.views.setuproom.SpeakerPrivateRoomActivity;
import com.example.myapplication.views.setuproom.SpeakerPublicRoomActivity;
import com.example.myapplication.views.setuproom.StartRoomFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.tencent.iot.speech.app.DemoConfig;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private ProgressDialog progressDialog;

    private String meetingId;
    private String meetingStatus;
    UserLocalStore userLocalStore;
    Context c;

    public  static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public class SocketListImpl implements WebSocket.SocketListener {

        public void  onMessage(String s){

            System.out.println("this is implemented listener: "+s);
            if (s.equals("001SYSTEM MESSAGE: SPEAKER HAS LEFT")){
                handler.post(() -> Toast.makeText(c, "speaker has left", Toast.LENGTH_SHORT).show());
            } else {
                handler.post(() -> recognizeResult.setText(s));
                handler.post(() -> recognizeResult.setSelection(recognizeResult.getText().length(), recognizeResult.getText().length()));
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initView();

        // get meetingId from intent
        Intent intent = getIntent();
        meetingId = intent.getStringExtra("meetingId" );
        meetingStatus = intent.getStringExtra("meetingStatus" );
        System.out.println("meetingStatus: "+meetingStatus);

        if (meetingStatus.equals("0")){ // meeting already ended call get meetingRecord instead of ws
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Processing...");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            callMeetingRecord();
        } else { // meeting still in progress, init ws

            c = this;
            // get logged in userId
            userLocalStore = new UserLocalStore(this);
            User user = userLocalStore.getLoggedInUser();
            String userId = user.getUserId();

            // create ws connection
            websocket = new WebSocket(new SocketListImpl());
            websocket.createWebSocketClient(userId, meetingId); // enter the meeting room
        }
    }
    public void closeWebSocket(){
        // close web socket is called when onLeave is triggered, prevent meeting with status 0 trigger this close socket
        if (websocket !=null) {
            websocket.closeWebSocket();
        }
    }

    public void passHandlerResult(EditText passedRecognizeResult, Handler passedHandler){
        handler = passedHandler;
        recognizeResult = passedRecognizeResult;

    }

    public void initView(){
    }

    private void callMeetingRecord() {
            new Thread(() -> {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .build();

                Request request =new Request.Builder().get().url(DemoConfig.SERVER_PATH +DemoConfig.ROUTE_SHOWROOMRECORD+meetingId).build();
                Call call = okHttpClient.newCall(request);
                Context context = this;

                // this makes asynchronous call to server
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        //dismiss login loading bar
                        progressDialog.dismiss();
                        //handling json response, getting fields
                        final String myresponse_json = response.body().string();
                        //Log.println(Log.DEBUG, "OUT_JSON", myresponse_json);
                        Gson gson = new Gson();
                        Properties extractData = gson.fromJson(myresponse_json, Properties.class);
                        String state = extractData.getProperty("state");
                        //Log.println(Log.DEBUG, "LOG", state);

                        // check response "state"
                        if (state.equals("1")) {
                            //need to update UI thread with a new thread,dont block UI thread
                            String errorMessage = extractData.getProperty("errorMessage");
                            runOnUiThread(() -> {
                                MaterialAlertDialogBuilder dialogBuilder;
                                dialogBuilder = new MaterialAlertDialogBuilder(context).setTitle(errorMessage);
                                dialogBuilder.show();
                            });
                        } else if (state.equals("0")) { //get UID here
                            //need to update UI thread with a new thread,dont block UI thread
                            String meetingRecord = extractData.getProperty("record");
                            runOnUiThread(
                                    () -> {
                                        handler.post(() -> recognizeResult.setText(meetingRecord));
                                    }
                            );
                        }

                    }
                });
            }).start();
        }

}