package com.example.myapplication.util.network;

import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class WebSocket {
    public interface SocketListener {
        void onMessage(String s);
    }
    SocketListener listener;

    public WebSocket(SocketListener l){
        listener = l;
    }

    public WebSocketClient webSocketClient;

    public void createWebSocketClient(String userName, String meetingId) {

        URI uri;
        try {
            // Connect to local host
            //uri = new URI("ws://192.168.1.124:8080/ws?userName="+userName+"&meetingId="+meetingId); // change to remote
            uri = new URI("ws://35.215.150.91/ws?userName="+userName+"&meetingId="+meetingId); // change to remote
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                webSocketClient.send("Hello World!");
            }
            @Override
            public void onTextReceived(String s) {
                //Log.i("WebSocket", "Message received");
                //final String message = s;
                Log.i("WebSocket", "Message received" + s);
                listener.onMessage(s);
            }
            @Override
            public void onBinaryReceived(byte[] data) {
            }
            @Override
            public void onPingReceived(byte[] data) {
            }
            @Override
            public void onPongReceived(byte[] data) {
            }
            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }
            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        //webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();

    }
}
