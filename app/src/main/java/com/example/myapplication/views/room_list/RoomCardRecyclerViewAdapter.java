package com.example.myapplication.views.room_list;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.myapplication.R;
import com.example.myapplication.models.RoomEntry;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter used to show a list of rooms
 */
public class RoomCardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int TYPE_PUBLIC = 1;
    private static int TYPE_PRIVATE = 2;
    private final Context context;
    private  List<RoomEntry> roomList;


    RoomCardRecyclerViewAdapter(Context context,List<RoomEntry> roomList) {
        this.context = context;
        this.roomList = roomList;

    }

    @Override
    public int getItemViewType(int position){
       if(TextUtils.isEmpty(roomList.get(position).getPassword())){
           return TYPE_PUBLIC;
       }
       else{
           return TYPE_PRIVATE;
       }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_PUBLIC) { // for call layout
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.public_room_card, parent, false);
            return new PublicRoomCardViewHolder(view);}
            else {
                view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.private_room_card, parent, false);
                return new PrivateRoomCardViewHolder(view);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (roomList != null && position < roomList.size()) {
            if (getItemViewType(position) == TYPE_PUBLIC) {
                ((PublicRoomCardViewHolder) viewHolder).bindType(roomList.get(position));
            } else {
                ((PrivateRoomCardViewHolder) viewHolder).bindType(roomList.get(position));
            }
        }
    }

    public List<RoomEntry> getRoomList(){
        return roomList;
    }
    public void setRoomList(List<RoomEntry> rooms){
         roomList=rooms;
    }
    @Override
    public int getItemCount() {
        return roomList.size();
    }
}
