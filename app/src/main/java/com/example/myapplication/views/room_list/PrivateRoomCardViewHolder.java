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
import com.example.myapplication.views.setup_room.SpeakerPrivateRoomActivity;

public class PrivateRoomCardViewHolder extends RecyclerView.ViewHolder {
    public NetworkImageView roomImage;
    public TextView roomTitle;
    public TextView speaker_name;
    private ImageRequester imageRequester;

    public PrivateRoomCardViewHolder(@NonNull View itemView) {
        super(itemView);
        //TODO: Find and store views from itemView
        roomImage = itemView.findViewById(R.id.room_theme_image);
        roomTitle = itemView.findViewById(R.id.room_title);
        speaker_name = itemView.findViewById(R.id.speaker_name);

        imageRequester = ImageRequester.getInstance();

        roomImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// 点击图片，听众打开私密房间
                Context context=v.getContext();
                Intent go_asr_activity=new Intent(context, SpeakerPrivateRoomActivity.class);
                context.startActivity(go_asr_activity);

            }
        });
    }


    public void bindType(RoomCardType item) {
        roomTitle.setText(((RoomEntry)item).getRoom_title());
        speaker_name.setText(((RoomEntry)item).getSpeaker_name());
        imageRequester.setImageFromUrl(roomImage,((RoomEntry)item).getUrl());
    }
}
