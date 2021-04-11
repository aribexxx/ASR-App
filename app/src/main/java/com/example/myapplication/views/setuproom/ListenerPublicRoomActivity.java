package com.example.myapplication.views.setuproom;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.asr.parentclass.ListenerRoomActivity;
import com.example.myapplication.models.RoomEntry;
import com.example.myapplication.views.customview.subtitle.SubtitleView;

public class ListenerPublicRoomActivity extends ListenerRoomActivity {
    RoomEntry room;
    EditText recognizeResult;
    SubtitleView subtitleView;

    public ListenerPublicRoomActivity() {
        this.room = new RoomEntry.Builder().build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.mh_listener_publicroom_activity);

        // 初始化相应的控件
        //set UI components view
        leaveRoom = findViewById(R.id.leaveroom_button);
        //set leave room button click
        leaveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        cancel = (Button) findViewById(R.id.cancel);
        recognizeState = (TextView) findViewById(R.id.recognize_state);
        volume = (TextView) findViewById(R.id.volume);
        recognizeResult = (EditText) findViewById(R.id.recognize_result);
        subtitleView = (SubtitleView) findViewById(R.id.subtitle_view);
        recognizeResult.setText("This is aaaa listener public room");

        subtitleView.setSubtitle("[00:16.01]Loving can hurt, loving can hurt sometimes\n" +
            "[00:24.79]But it's the only thing that I know\n" +
            "[00:33.52]When it gets hard, you know it can get hard sometimes\n" +
            "[00:42.85]It is the only thing makes us feel alive\n" +
            "[00:52.76]We keep this love in a photograph\n" +
            "[00:57.57]We made these memories for ourselves\n" +
            "[01:01.26]Where our eyes are never closing\n" +
            "[01:03.80]Hearts are never broken\n" +
            "[01:06.06]And time's forever frozen still\n" +
            "[01:09.75]So you can keep me\n" +
            "[01:12.58]Inside the pocket of your ripped jeans\n" +
            "[01:17.11]Holding me closer 'til our eyes meet\n" +
            "[01:21.61]You won't ever be alone, wait for me to come home\n" +
            "[01:32.36]Loving can heal, loving can mend your soul\n" +
            "[01:40.26]And it's the only thing that I know, know\n" +
            "[01:48.46]I swear it will get easier\n" +
            "[01:52.42]Remember that with every piece of you\n" +
            "[01:57.48]Hm, and it's the only thing we take with us when we die\n" +
            "[02:07.39]Hm, we keep this love in this photograph\n" +
            "[02:13.06]We made these memories for ourselves\n" +
            "[02:16.74]Where our eyes are never closing\n" +
            "[02:19.29]Hearts were never broken\n" +
            "[02:21.55]And time's forever frozen still\n" +
            "[02:25.23]So you can keep me\n" +
            "[02:28.34]Inside the pocket of your ripped jeans\n" +
            "[02:32.59]Holding me closer 'til our eyes meet\n" +
            "[02:37.12]You won't ever be alone\n" +
            "[02:43.07]And if you hurt me\n" +
            "[02:45.90]That's okay baby, only words bleed\n" +
            "[02:48.73]Inside these pages you just hold me\n" +
            "[02:54.95]And I won’t ever let you go\n" +
            "[02:59.19]Wait for me to come home\n" +
            "[03:03.72]Wait for me to come home\n" +
            "[03:07.96]Wait for me to come home\n" +
            "[03:12.78]Wait for me to come home\n" +
            "[03:18.44]Oh, you can fit me\n" +
            "[03:21.55]Inside the necklace you got when you were sixteen\n" +
            "[03:25.80]Next to your heartbeat where I should be\n" +
            "[03:30.32]Keep it deep within your soul\n" +
            "[03:36.55]And if you hurt me\n" +
            "[03:39.67]Well, that's okay baby, only words bleed\n" +
            "[03:43.92]Inside these pages you just hold me\n" +
            "[03:48.16]And I won’t ever let you go\n" +
            "[03:54.11]When I'm away, I will remember how you kissed me\n" +
            "[03:56.94]Under the lamppost back on Sixth street\n" +
            "[04:06.28]Hearing you whisper through the phone\n" +
            "[04:10.24]\"Wait for me to come home\"");
        subtitleView.init();
        super.initView();
    }


}
