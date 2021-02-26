package com.example.myapplication.views.room_view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.util.network.ImageRequester;
import com.example.myapplication.models.RoomEntry;
import com.example.myapplication.R;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter used to show a simple grid of products.
 */
public class RoomCardRecyclerViewAdapter extends RecyclerView.Adapter<RoomCardViewHolder> {

    private List<RoomEntry> productList;
    private ImageRequester imageRequester;

    RoomCardRecyclerViewAdapter(List<RoomEntry> productList) {
        this.productList = productList;
        imageRequester = ImageRequester.getInstance();
    }

    @NonNull
    @Override
    public RoomCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_card, parent, false);
        return new RoomCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomCardViewHolder holder, int position) {
        // TODO: Put ViewHolder binding code here in MDC-102
        if (productList != null && position < productList.size()) {
            RoomEntry product = productList.get(position);
            holder.productTitle.setText(product.title);
            holder.productPrice.setText(product.speaker_name);
            imageRequester.setImageFromUrl(holder.productImage, product.url);
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
