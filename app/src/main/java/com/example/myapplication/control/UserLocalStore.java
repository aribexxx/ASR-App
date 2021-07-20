package com.example.myapplication.control;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

import com.example.myapplication.models.User;

public class UserLocalStore {
    public static final String SP_NAME = "userDetails";

    SharedPreferences userLocalDatabase;

    public UserLocalStore(Fragment fragment){
        userLocalDatabase = fragment.getActivity().getSharedPreferences(SP_NAME, 0);
    }

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name", user.getUserName());
        spEditor.putString("id", user.getUserId());
        spEditor.commit();
    }

    public User getLoggedInUser(){
        String name = userLocalDatabase.getString("name", "");
        String id = userLocalDatabase.getString("id", "");

        return new User(id, name);
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public void cleanUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
