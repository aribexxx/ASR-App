package com.tencent.iot.speech.app;


import com.example.myapplication.BuildConfig;

public class DemoConfig {
    //BuildConfig.Secretkey
    public static final String apppId = CommonConst.appid;
    public static final String secretKey = CommonConst.secretKey;
    public static final String secretId = CommonConst.secretId;

    // For 按设备授权需求
    public static final String appIdForDeviceAuth = BuildConfig.AppIdForDeviceAuth;
    public static final String secretKeyForDeviceAuth = BuildConfig.SecretkeyForDeviceAuth;
    public static final String secretIdForDeviceAuth = BuildConfig.SecretIdForDeviceAuth;
    public static final String serialNumForDeviceAuth = BuildConfig.SerialNumForDeviceAuth;
    public static final String deviceNumForDeviceAuth = BuildConfig.DeviceNumForDeviceAuth;


    //google cloud server config
    public final static String SERVER_PATH = "https://af914efa-f908-4eac-9a23-b84e6115908a.mock.pstmn.io";
    public final static String ROUTE_LOGIN ="/logIn";
    public final static String ROUTE_SIGNUP ="/signup";
    public final static String ROUTE_STARTROOM ="/startMeeting";
    public final static String ROUTE_SHOWROOM ="/showMeeting";
}
