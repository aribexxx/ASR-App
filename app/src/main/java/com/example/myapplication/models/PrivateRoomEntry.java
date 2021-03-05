 package com.example.myapplication.models;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.example.myapplication.R;
/**
 * A product entry in the list of products.
 */
public class PublicRoomEntry {
    private static final String TAG = PublicRoomEntry.class.getSimpleName();

    private String room_title;
    private Uri dynamicUrl;
    private String url;
    private String speaker_name;
    private String room_description;
    private String password;// private roon require, public can set to null

    public PublicRoomEntry(){
        room_title="Default Titile";
        dynamicUrl=null;
        url=null;
        speaker_name="Speaker name unknown";
        room_description="Need description";

    }
    public PublicRoomEntry(String password) {
        this.password=password;
    }


    public String getSpeaker_name() {
        return speaker_name;
    }

    public void setSpeaker_name(String speaker_name) {
        this.speaker_name = speaker_name;
    }

    public String getRoom_description() {
        return room_description;
    }

    public void setRoom_description(String room_description) {
        this.room_description = room_description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getRoom_title() {
        return room_title;
    }

    public void setRoom_title(String room_title) {
        this.room_title = room_title;
    }

    public Uri getDynamicUrl() {
        return dynamicUrl;
    }

    public void setDynamicUrl(Uri dynamicUrl) {
        this.dynamicUrl = dynamicUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    

    /**
     * Loads a raw JSON at R.raw.products and converts it into a list of RoomEntry objects
     */
    public static List<PublicRoomEntry> initRoomEntryList(Resources resources) {
        InputStream inputStream = resources.openRawResource(R.raw.rooms);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int pointer;
            while ((pointer = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, pointer);
            }
        } catch (IOException exception) {
            Log.e(TAG, "Error writing/reading from the JSON file.", exception);
        } finally {
            try {
                inputStream.close();
            } catch (IOException exception) {
                Log.e(TAG, "Error closing the input stream.", exception);
            }
        }
        String jsonRoomsString = writer.toString();
        Gson gson = new Gson();
        Type roomListType = new TypeToken<ArrayList<PublicRoomEntry>>() {
        }.getType();

        return gson.fromJson(jsonRoomsString, roomListType);
    }
}