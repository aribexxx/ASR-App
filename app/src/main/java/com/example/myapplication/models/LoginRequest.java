package com.example.myapplication.models;

public class LoginRequest {
    final String userName;
    final String userPwd;

    public LoginRequest(String userName, String userPwd) {
        this.userName = userName;
        this.userPwd = userPwd;
    }
}
