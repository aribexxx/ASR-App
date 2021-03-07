package com.example.myapplication.views.demo_trail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.views.adapter.RecordAdaptor;
import com.example.myapplication.R;
import com.example.myapplication.models.Record;
import com.example.myapplication.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    RecyclerView myrecyclerView;

    List<Record> recordlist;

    Button go_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        //recycler view
        myrecyclerView = findViewById(R.id.myrecycleview);
        initData();
        initRecyclerView();

        //button go back main
        go_back=(Button)findViewById(R.id.back_main);
         go_back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent back_intent=new Intent(RecyclerViewActivity.this, MainActivity.class);
                 startActivity(back_intent);
             }
         });
    }
    private void initRecyclerView() {
        RecordAdaptor recordAdaptor = new RecordAdaptor(recordlist);
        myrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myrecyclerView.setAdapter(recordAdaptor);
    }

    private void initData() {
        recordlist = new ArrayList<>();
        recordlist.add(new Record("Iron Man", "7.9", "2008", "After being held captive in an Afghan cave, billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil."));
        recordlist.add(new Record("The Incredible Hulk", "6.7", "2008", "Bruce Banner, a scientist on the run from the U.S. Government, must find a cure for the monster he turns into, whenever he loses his temper."));
        recordlist.add(new Record("Iron Man 2", "7.0", "2010", "With the world now aware of his identity as Iron Man, Tony Stark must contend with both his declining health and a vengeful mad man with ties to his father's legacy."));
        recordlist.add(new Record("Thor", "7.0", "2011", "The powerful but arrogant god Thor is cast out of Asgard to live amongst humans in Midgard (Earth), where he soon becomes one of their finest defenders."));

    }

}
