package com.example.myapplication.views;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.myapplication.R;

public class ProductCardViewHolder extends RecyclerView.ViewHolder {
    public NetworkImageView productImage;
    public TextView productTitle;
    public TextView productPrice;

    public ProductCardViewHolder(@NonNull View itemView) {
        super(itemView);
        //TODO: Find and store views from itemView
        productImage = itemView.findViewById(R.id.product_image);
        productTitle = itemView.findViewById(R.id.product_title);
        productPrice = itemView.findViewById(R.id.product_price);
    }
}
