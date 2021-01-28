package com.example.myapplication.activities;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.util.MyRecyclerViewAdapter;
import com.example.myapplication.R;

public class RecyclerViewActivity extends AppCompatActivity {


    String musicList[] = {"Song a","Song b","Song c"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // link to xml
        setContentView(R.layout.activity_third);

        //ListView
        RecyclerView my_recycview = (RecyclerView) findViewById(R.id.myrecycleview);
        my_recycview.setLayoutManager(new LinearLayoutManager(this));
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, musicList);
        adapter.setClickListener(this);
        my_recycview.setAdapter(adapter);
    }
}
