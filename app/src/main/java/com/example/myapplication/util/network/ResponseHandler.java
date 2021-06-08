package com.example.myapplication.util.network;

import com.example.myapplication.models.TranslationResponse;
import com.google.gson.Gson;

public final class ResponseHandler {

    public static TranslationResponse decodeJsonResponse(String jsonString){
        Gson gson=new Gson();
        return gson.fromJson(jsonString,TranslationResponse.class);
    }
}
