package com.example.myapplication.views.room_list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.myapplication.R;
import com.example.myapplication.models.RoomEntry;
import com.example.myapplication.control.network.ImageRequester;
import com.example.myapplication.views.setuproom.ListenerPrivateRoomActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.example.myapplication.utils.DemoConfig;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PrivateRoomCardViewHolder extends RecyclerView.ViewHolder {
    public NetworkImageView roomImage;
    public TextView roomTitle;
    public TextView speaker_name;
    public TextView roomStatus;
    private ImageRequester imageRequester;
    public TextView roomId;
    private EditText input_password;

    protected ProgressDialog progressDialog;

    public  static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public PrivateRoomCardViewHolder(@NonNull View itemView) {
        super(itemView);
        // Find and store views from itemView
        roomImage = itemView.findViewById(R.id.room_theme_image);
        roomTitle = itemView.findViewById(R.id.room_title);
        speaker_name = itemView.findViewById(R.id.speaker_name);
        roomId = itemView.findViewById(R.id.room_id);
        roomStatus = itemView.findViewById(R.id.room_status);

        imageRequester = ImageRequester.getInstance();

        roomImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// 点击图片，听众打开私密房间
                // todo: all call join meeting api
                Context context=v.getContext();
                promptPasswordInput(context);

            }
        });
    }




    public void bindType(RoomCardType item) {
        roomTitle.setText(((RoomEntry)item).getRoomTitle());
        speaker_name.setText(((RoomEntry)item).getSpeakerName());
        imageRequester.setImageFromUrl(roomImage,((RoomEntry)item).getUrl());
        roomId.setText(((RoomEntry)item).getRoomID());
        roomStatus.setText(((RoomEntry)item).getStatus());
    }

    private void callJoinMeeting(Context context) {
            new Thread(() -> {

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .build();


                String meetingId = roomId.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("meetingId", meetingId);
                    jsonObject.put("pwd", input_password.getText().toString());

                } catch (Exception e) {
                    System.out.println(e.toString());
                }

                RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
                Request request = new Request.Builder().post(requestBody).url(DemoConfig.SERVER_PATH + DemoConfig.ROUTE_JOINROOM).build();
                Call call = okHttpClient.newCall(request);

                // this makes asynchronous call to server
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        //startMeetingProgressDialog.dismiss();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        //dismiss loading bar
                        //startMeetingProgressDialog.dismiss();
                        //handling json response, getting fields
                        final String myresponse_json = response.body().string();
                        //Log.println(Log.DEBUG, "OUT_JSON", myresponse_json);
                        Gson gson = new Gson();
                        Properties extractData = gson.fromJson(myresponse_json, Properties.class);
                        String state = extractData.getProperty("state");
                        //Log.println(Log.DEBUG, "LOG", state);

                        // check response "state"/ todo: if 400 bad request state is null that cause app to crash
                        if (state.equals("1")) {
                            //need to update UI thread with a new thread,dont block UI thread
                            String errorMessage = extractData.getProperty("errorMessage");

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    MaterialAlertDialogBuilder dialogBuilder;
                                    dialogBuilder = new MaterialAlertDialogBuilder(context).setTitle(errorMessage);

                                    dialogBuilder.setPositiveButton("Ok", null).show();
                                }
                            });
                        } else if (state.equals("0")) { //get UID here
                            String roomTitle = extractData.getProperty("roomTitle");
                            String userName = extractData.getProperty("userName");
                            String userId = extractData.getProperty("userId");

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    // password and meeting correct redirect to meeting room
                                    String meetingId = roomId.getText().toString();
                                    String meetingStatus = roomStatus.getText().toString();
                                    Intent go_asr_activity=new Intent(context, ListenerPrivateRoomActivity.class);
                                    go_asr_activity.putExtra("meetingId", meetingId);
                                    go_asr_activity.putExtra("meetingStatus", meetingStatus);
                                    context.startActivity(go_asr_activity);
                                }
                            });

                        }

                    }
                });
            }).start();
        }

    public void promptPasswordInput(Context context) {
        MaterialAlertDialogBuilder dialog_builder;
        dialog_builder = new MaterialAlertDialogBuilder(context).setTitle("Input Your Password for the private Room");

        //SET UP input textview
        input_password = new EditText(context);
        input_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog_builder.setView(input_password);

        // Set up the buttons
        dialog_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callJoinMeeting(context);
            }
        });
        dialog_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog_builder.show();
    }


}
