package com.example.myapplication.views.test_ws;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

import com.example.myapplication.R;
import com.example.myapplication.util.network.WebSocket;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


public class TestWebSocketFragment extends Fragment implements NavController.OnDestinationChangedListener{

    protected Handler handler;

    protected MaterialButton connectButton;
    protected MaterialButton sendButton;
    protected MaterialButton closeButton;
    protected TextView textView;
    protected TextInputEditText textInputEditText;
    protected WebSocket websocket;

    public class SocketListImpl implements WebSocket.SocketListener {
        public void  onMessage(String s){
            System.out.println("this is implemented:"+s);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(s);
                }
            });
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment with the ProductGrid theme
        View view = inflater.inflate(R.layout.test_web_socket, container, false);

        connectButton = view.findViewById(R.id.connect_button);
        sendButton = view.findViewById(R.id.send_button);
        closeButton = view.findViewById(R.id.close_button);
        textView = view.findViewById(R.id.received_msg);
        textInputEditText = view.findViewById(R.id.send_msg_text);

        sendButton.setVisibility(View.INVISIBLE);
        closeButton.setVisibility(View.INVISIBLE);
        textInputEditText.setVisibility(View.INVISIBLE);

        handler = new Handler();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                websocket.webSocketClient.send(textInputEditText.getText().toString());
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                websocket.webSocketClient.close();
                connectButton.setVisibility(View.VISIBLE);
                sendButton.setVisibility(View.INVISIBLE);
                textInputEditText.setVisibility(View.INVISIBLE);
                closeButton.setVisibility(View.INVISIBLE);
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                websocket = new WebSocket(new SocketListImpl());
                websocket.createWebSocketClient("1", "4");
                sendButton.setVisibility(View.VISIBLE);
                textInputEditText.setVisibility(View.VISIBLE);
                closeButton.setVisibility(View.VISIBLE);

                connectButton.setVisibility(View.INVISIBLE);
            }
        });

        // Set up the toolbar
        setUpToolbar(view);
        return view;
    }



    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.shr_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

    }

}
