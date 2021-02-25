package com.example.myapplication.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.util.network.ImageRequester;
import com.example.myapplication.util.network.ProductEntry;
import com.example.myapplication.R;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter used to show a simple grid of products.
 */
public class ProductCardRecyclerViewAdapter extends RecyclerView.Adapter<ProductCardViewHolder> {

    private List<ProductEntry> productList;
    private ImageRequester imageRequester;

    ProductCardRecyclerViewAdapter(List<ProductEntry> productList) {
        this.productList = productList;
        imageRequester = ImageRequester.getInstance();
    }

    @NonNull
    @Override
    public ProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_card, parent, false);
        return new ProductCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCardViewHolder holder, int position) {
        // TODO: Put ViewHolder binding code here in MDC-102
        if (productList != null && position < productList.size()) {
            ProductEntry product = productList.get(position);
            holder.productTitle.setText(product.title);
            holder.productPrice.setText(product.price);
            imageRequester.setImageFromUrl(holder.productImage, product.url);
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
