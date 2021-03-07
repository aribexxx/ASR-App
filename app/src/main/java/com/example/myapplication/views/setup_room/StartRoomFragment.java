 package com.example.myapplication.views.setup_room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;
import com.example.myapplication.models.RoomEntry;
import com.example.myapplication.views.nav.NavigationHost;
import com.example.myapplication.views.room_list.RoomGridFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.iot.speech.app.DemoConfig;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//This fragment user input room name and other info to start a room

public class StartRoomFragment  extends BottomSheetDialogFragment  {
    ImageButton public_room;
    ImageButton private_room;
    MaterialButton letgo;




  //  SpeakerPublicRoomActivity a_pub_room;
    SpeakerPrivateRoomActivity a_private_room;


    enum RoomType{
        PUBLIC_ROOM,
        PRIVATE_ROOM
    };
    RoomType selectedRoom;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment with the ProductGrid theme
        View view = inflater.inflate(R.layout.mh_startroom_page, container, false);
        public_room=view.findViewById(R.id.public_room);
        private_room=view.findViewById(R.id.private_room);
        letgo=view.findViewById(R.id.startroom_button);


        public_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView room_selected_description=view.findViewById(R.id.roomType_description);
                room_selected_description.setText("Start a room open to everyone");
                letgo.setText("Open to everybody");
                selectedRoom=RoomType.PUBLIC_ROOM;
            }
        });

        private_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView room_selected_description=view.findViewById(R.id.roomType_description);
                room_selected_description.setText("Start a private room");
                letgo.setText("Start Private Room");
                selectedRoom=RoomType.PRIVATE_ROOM;

                //first create fragment, which initiate a RoomEntry
                a_private_room=new SpeakerPrivateRoomActivity();
                //then prompt password, which set password field in RoomEntry
                a_private_room.initRoomEtry();
                promptPasswordSetting();
            }
        });

        letgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             switch(selectedRoom){
                 case PUBLIC_ROOM:
                     Log.println(Log.DEBUG,"ROOM","PUBLICCCC");
                     //start one room here
//                     a_pub_room=new PublicRoomFragment();
//                     a_pub_room.show(getFragmentManager(),a_pub_room.getTag());
                     //go to asr room
                     Intent to_asr_public=new Intent(getActivity(), SpeakerPublicRoomActivity.class);
                     startActivity(to_asr_public);
                     //((NavigationHost) getActivity()).navigateTo(a_pub_room, true); // Navigate to the next Fragment
                     break;

                 case PRIVATE_ROOM:
                     Log.println(Log.DEBUG,"ROOM","PRIVATEEEE");
                    if(a_private_room.getRoomEtry().getPassword()!=null){
                        Intent to_asr_private=new Intent(getActivity(), SpeakerPrivateRoomActivity.class);
                        startActivity(to_asr_private);
                    }
                     Log.println(Log.DEBUG,"ROOM","wooorks"+a_private_room.getRoomEtry().getPassword());

                  //   ((NavigationHost) getActivity()).navigateTo(a_private_room, true); // Navigate to the next Fragment
                     break;
             }
             // click lets go button , also remove current fragment
                getFragmentManager().beginTransaction().remove(StartRoomFragment.this).commit();
            }
        });

        return view;
    }

     public void promptPasswordSetting(){
         MaterialAlertDialogBuilder dialog_builder;
          dialog_builder=new MaterialAlertDialogBuilder(getActivity()).setTitle("Input Your Password for the private Room");

          //SET UP input textview
         final EditText input_password=new EditText(getActivity());
         input_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
         dialog_builder.setView(input_password);

// Set up the buttons
         dialog_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 a_private_room.setPasswordPrivateRoom( input_password.getText().toString());
                 Log.println(Log.DEBUG,"ROOM","wooorks"+a_private_room.getRoomEtry().getPassword());

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
