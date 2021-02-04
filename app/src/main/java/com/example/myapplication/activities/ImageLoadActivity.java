package com.example.myapplication.activities;
import com.example.myapplication.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoadActivity extends AppCompatActivity  {

    String apiUrl = "https://protocoderspoint.com/jsondata/superheros.json";
    String title, image, category;
    TextView titleTextView, categoryTextView;
    ProgressDialog progressDialog;
    Button displayData;
    ImageView imageView;
    int index_no;
    Spinner spinner;
    String[] HerosNameList = { "Flash", "SuperMan", "Thor", "Hulk", "Venom"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);
        // get the reference of View's
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        categoryTextView = (TextView) findViewById(R.id.categoryTextView);
        displayData = (Button) findViewById(R.id.displayData);
        imageView = (ImageView) findViewById(R.id.imageView);
        spinner=(Spinner)findViewById(R.id.selectHero);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,HerosNameList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                index_no=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // implement setOnClickListener event on displayData button
        displayData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create object of MyAsyncTasks class and execute it
                MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                myAsyncTasks.execute();
            }
        });
    }
    public class MyAsyncTasks extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(ImageLoadActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            // implement API in background and store the response in current variable
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                        System.out.print(current);
                    }
                    Log.d("datalength",""+current.length());
                    // return the data to onPostExecute method
                    return current;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }
        @Override
        protected void onPostExecute(String s) {
            Log.d("data", s.toString());
            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray1 = jsonObject.getJSONArray("superheros");
                JSONObject jsonObject1 =jsonArray1.getJSONObject(index_no);
                title = jsonObject1.getString("name");
                category = jsonObject1.getString("power");
                image=jsonObject1.getString("url");
                //make all the view Visible when data is ready to show
                titleTextView.setVisibility(View.VISIBLE);
                categoryTextView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                titleTextView.setText("Hero Name : "+title);
                categoryTextView.setText("His Ability : "+category);
                Picasso.get().load(image).into(imageView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


