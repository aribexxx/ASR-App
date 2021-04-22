package com.example.myapplication.views.room_list;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.myapplication.R;
import com.example.myapplication.models.RoomEntry;
import com.example.myapplication.util.network.ImageRequester;
import com.example.myapplication.views.setuproom.ListenerPublicRoomActivity;

public class PublicRoomCardViewHolder extends RecyclerView.ViewHolder {
    public NetworkImageView roomImage;
    public TextView roomTitle;
    public TextView speaker_name;
    private ImageRequester imageRequester;
    public TextView roomId;

    public PublicRoomCardViewHolder(@NonNull View itemView) {
        super(itemView);
        //TODO: Find and store views from itemView
        roomImage = itemView.findViewById(R.id.room_theme_image);
        roomTitle = itemView.findViewById(R.id.room_title);
        speaker_name = itemView.findViewById(R.id.speaker_name);
        imageRequester = ImageRequester.getInstance();
        roomId = itemView.findViewById(R.id.room_id);

        roomImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// 点击图片，听众打开public房间
                Context context=v.getContext();
                String meetingId = roomId.getText().toString();
                Intent go_asr_activity=new Intent(context, ListenerPublicRoomActivity.class);
                go_asr_activity.putExtra("meetingId", meetingId);
                context.startActivity(go_asr_activity);

            }
        });
    }


    public void bindType(RoomCardType item) {
        roomTitle.setText(((RoomEntry)item).getRoomTitle());
        speaker_name.setText(((RoomEntry)item).getSpeakerName());
        imageRequester.setImageFromUrl(roomImage,((RoomEntry)item).getUrl());
        roomId.setText(((RoomEntry)item).getRoomID());
    }


}
