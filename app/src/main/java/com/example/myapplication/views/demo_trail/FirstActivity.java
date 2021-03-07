package com.example.myapplication.views.demo_trail;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.views.MainActivity;

public class FirstActivity extends AppCompatActivity {
    TextView textreceived;
    Button button2;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        text = getIntent().getExtras().getString("Text");
        textreceived = (TextView)findViewById(R.id.textreceived);
        textreceived.setText(text);

        button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
