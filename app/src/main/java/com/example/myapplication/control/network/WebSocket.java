package com.example.myapplication.control.network;
import android.util.Log;
import com.tencent.iot.speech.app.DemoConfig;
import java.net.URI;
import java.net.URISyntaxException;
import tech.gusavila92.websocketclient.WebSocketClient;

public class WebSocket {
    private static final String TAG = "WebSocket";

    public interface SocketListener {
        void onMessage(String s);
    }
    SocketListener listener;

    public WebSocket(SocketListener l){
        listener = l;
    }

    public WebSocketClient webSocketClient;

    public void createWebSocketClient(String userId, String meetingId) {

        URI uri;
        try {
            uri = new URI(DemoConfig.WS_PATH+"userId="+userId+"&meetingId="+meetingId); // change to remote
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        Log.d(TAG,"Socket url is"+uri.toString());
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.d(TAG,"Session is starting");
                //webSocketClient.send(userName+" entered room "+meetingId);
            }
            @Override
            public void onTextReceived(String response) {
                // todo: not sure why sometimes not invoked after the listener first join the meeting
                Log.d(TAG,"onTextReceived"+response);
                listener.onMessage(response);

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
                Log.d(TAG,"onException: "+e.getMessage());
            }
            @Override
            public void onCloseReceived() {
                Log.d(TAG,"onCloseReceived: ");
            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    public void closeWebSocket(){
        if (webSocketClient!=null) {
            webSocketClient.close();
        }
    }
}
