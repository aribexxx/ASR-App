package com.example.myapplication.views.parentclass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.control.network.ResponseHandler;
import com.example.myapplication.models.RoomEntry;
import com.example.myapplication.models.TranslationResponse;
import com.example.myapplication.models.User;
import com.example.myapplication.control.UserLocalStore;
import com.example.myapplication.control.network.WebSocket;
import com.example.myapplication.utils.StringBuild;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.tencent.iot.speech.app.DemoConfig;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// parent class for public and private listener room
// get meetinglist from server and display all existing rooms

//  公开私密房间区别 : customize UI
//  公共模块：socket获取字幕+ 双语字幕展示

public class ListenerRoomActivity extends AppCompatActivity {
    private static final Logger logger = LoggerFactory.getLogger(ListenerRoomActivity.class);
    private static final String TAG = "ListenerRoomActivity";
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
    RoomEntry room;
    private String meetingId;
    private String meetingStatus;
    private String translationModelType;
    UserLocalStore userLocalStore;
    Context context;
    WebSocket.SocketListener serverListner;
    protected LinkedHashMap<String, String> resMap = new LinkedHashMap<>();
    public  static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public ListenerRoomActivity() {
        this.room=new RoomEntry.Builder().build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initView();
        // get logged in userId
        userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();
        String userId = user.getUserId();
       // AAILogger.info(logger, "Current user Id is " + userId +"username "+user.getUserName());
        Log.d(TAG,"Current user Id is " + userId +"username "+user.getUserName());
        // get meetingId from intent
        Intent intent = getIntent();
        meetingId = intent.getStringExtra("meetingId" );
        meetingStatus = intent.getStringExtra("meetingStatus" );
       // AAILogger.info(logger, "meetingStatus is " + meetingStatus);
        Log.d(TAG,"meetingStatus is " + meetingStatus+"meetingID: "+meetingId);
        if (meetingStatus.equals("0")){ // meeting already ended call get meetingRecord instead of ws
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Processing...");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            callHistoryMeetingRecord();
        }
        else { // meeting still in progress, init ws and create ws connection

            websocket = new WebSocket(new WebSocket.SocketListener() {
                @Override
                public void onMessage(String response) {
                    Log.d(TAG,response);
                    TranslationResponse resObj= ResponseHandler.decodeJsonResponse(response);
                    Log.d(TAG,"statues:"+resObj.getStatus());
                     switch (resObj.getStatus()) {
                         case "0":
                             //Log.d(TAG,"success");
                             //decoding response packets
                             //Log.d(TAG,String.format("Response: seq = %s ,src = %s , transRes = %s ",resObj.getSeq(),resObj.getSrc(),resObj.getTranRes()));
                             resMap.put(String.valueOf(resObj.getSeq()), resObj.getTranRes());
                             final String totalMsg = StringBuild.buildMessage(resMap);
                             handler.post(() -> recognizeResult.setText(totalMsg));
                             handler.post(() -> recognizeResult.setSelection(recognizeResult.getText().length(), recognizeResult.getText().length()));
                             break;
                         case "1001":
                             Log.d(TAG,"翻译模型不可用");
                             break;
                         case "1002":
                             Log.d(TAG,"翻译失败");
                             break;
                         case "1000":
                             Log.d(TAG,"翻译方向不可用");
                             break;
                         case "1":
                             Log.d(TAG,"001SYSTEM MESSAGE: SPEAKER HAS LEFT");
                             break;
                         case "2":
                             Log.d(TAG,"userId+\"加入会议\"+meetingId");
                             break;
                         default:
                             break;
                     }
                    }
                });
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
    public void initView(){}
    private void callHistoryMeetingRecord() {
        //History Record
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