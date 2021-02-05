package com.example.myapplication.util;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Record;

import java.util.List;

public class RecordAdaptor extends RecyclerView.Adapter<RecordAdaptor.RecordVH> {
    private static final String TAG = "RecordAdaptor";
    private List<Record> recordslist;
    // data is passed into the constructor
    public RecordAdaptor(List<Record> recordslist){
        this.recordslist=recordslist;
    }


    // inflates the row layout from xml when needed
    @Override
    public RecordVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_row, parent, false);
        return new RecordVH(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull RecordVH holder, int position) {

        Record record = recordslist.get(position);
        holder.titleTextView.setText(record.getTitle());
        holder.yearTextView.setText(record.getYear());
        holder.ratingTextView.setText(record.getRating());
        holder.plotTextView.setText(record.getPlot());

        boolean isExpanded = recordslist.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return recordslist.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class RecordVH extends RecyclerView.ViewHolder {
        private static final String TAG = "RecordVH";
        ConstraintLayout expandableLayout;
        TextView titleTextView, yearTextView, ratingTextView, plotTextView;

        public RecordVH(@NonNull final View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            plotTextView = itemView.findViewById(R.id.plotTextView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);


            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Record record = recordslist.get(getAdapterPosition());
                    record.setExpanded(!record.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }

    }

}