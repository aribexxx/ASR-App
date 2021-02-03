package com.example.myapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MainActivity  extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener  {
    // Array of strings...

    String animalList[] = {"Lion","Tiger","Monkey","Elephant","Dog","Cat","Camel"};


    //button
    EditText text;
    Button clickme ;
    String Texthere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.animalNamesSpinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the animal name's list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, animalList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);



        //click button 1
        clickme = (Button)findViewById(R.id.button);
        text = (EditText)findViewById(R.id.textView1);

        clickme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Texthere = text.getText().toString();
                Intent intent = new Intent(MainActivity.this,
                        SecondActivity.class);
                intent.putExtra("Text",Texthere);
                startActivity(intent);
            }
        });



    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), animalList[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


}