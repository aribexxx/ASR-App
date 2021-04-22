package com.example.myapplication.views.setuproom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.util.UserLocalStore;
import com.example.myapplication.views.nav.NavigationHost;
import com.example.myapplication.views.room_list.RoomGridFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;
import com.tencent.iot.speech.app.DemoConfig;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


//This fragment user input room name and other info to start a room
// functions:
// 1. language setting
// 2.pub/ priv
// 3. input room info
public class StartRoomFragment extends BottomSheetDialogFragment {
    ImageButton public_room;
    ImageButton private_room;
    MaterialButton letgo;
    TextView roomTitleInput;
    TextView roomDescriptionInput;
    SpeakerPublicRoomActivity a_pub_room;
    SpeakerPrivateRoomActivity a_private_room;
    CarouselView carouselView;

    List<String> imageUrls = new ArrayList<String>();


    UserLocalStore userLocalStore;
    ProgressDialog startMeetingProgressDialog;

    EditText input_password;
    Spinner spinner;


    enum RoomType {
        PUBLIC_ROOM,
        PRIVATE_ROOM
    }

    RoomType selectedRoom;

    public  static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        userLocalStore = new UserLocalStore(this);

        // Inflate the layout for this fragment with the ProductGrid theme
        View view = inflater.inflate(R.layout.mh_startroom_page, container, false);
        public_room = view.findViewById(R.id.public_room);
        private_room = view.findViewById(R.id.private_room);
        letgo = view.findViewById(R.id.startroom_button);
        // first set letgo to invisible before public room/ private room is clicked
        letgo.setVisibility(View.INVISIBLE);
        roomDescriptionInput = view.findViewById(R.id.room_description_setting);
        roomTitleInput = view.findViewById(R.id.room_title_setting);

        // startMeetingProgressDialog
        startMeetingProgressDialog = new ProgressDialog(getContext());
        startMeetingProgressDialog.setTitle("Processing...");
        startMeetingProgressDialog.setMessage("Please wait...");
        startMeetingProgressDialog.setCancelable(false);
        startMeetingProgressDialog.setIndeterminate(true);

        for (int i=0; i<10; i++) {
            Random random = new Random();
            int ranit = random.nextInt(1100 - 1000) + 1000;
            imageUrls.add("https://picsum.photos/id/"+String.valueOf(ranit)+"/200/300");
        }


        carouselView = view.findViewById(R.id.carouselView);
        carouselView.setPageCount(imageUrls.size());
        carouselView.setImageListener(imageListener);

        //this part add listner to detect user input, if finish remove keyboard.
        roomDescriptionInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(roomDescriptionInput.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        //set language direction spinner-------------
        spinner = (Spinner) view.findViewById(R.id.language_setting_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.language_direction, android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // public room button--------------
        public_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView room_selected_description = view.findViewById(R.id.roomType_description);
                room_selected_description.setText("Start a room open to everyone");
                letgo.setText("Open to everybody");
                letgo.setVisibility(View.VISIBLE); // set letgo to visible
                selectedRoom = RoomType.PUBLIC_ROOM;
            }
        });

        private_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView room_selected_description = view.findViewById(R.id.roomType_description);
                room_selected_description.setText("Start a private room");
                letgo.setText("Start Private Room");
                letgo.setVisibility(View.VISIBLE); // set letgo to visible
                selectedRoom = RoomType.PRIVATE_ROOM;
                Log.println(Log.DEBUG, "ROOM", "PRIVATEEEE");
                promptPasswordSetting();
        }
        });

        letgo.setOnClickListener(v -> {
            if (selectedRoom == null){
                return;
            }

            //click letgo button ,save the room info to RoomEntry
            switch (selectedRoom) {
                case PUBLIC_ROOM:

                    // add input field checking logic
                    startMeetingProgressDialog.show();

                    Log.println(Log.DEBUG, "ROOM", "PUBLICCCC");
                    //start one room here
                    a_pub_room = new SpeakerPublicRoomActivity();
                    a_pub_room.initRoomEtry(roomTitleInput.getText().toString(), roomDescriptionInput.getText().toString());

                    callStartMeeting();

                    //go to asr room
                    //Intent to_asr_public = new Intent(getActivity(), SpeakerPublicRoomActivity.class);
                    //startActivity(to_asr_public);

                    //((NavigationHost) getActivity()).navigateTo(a_pub_room, true); // Navigate to the next Fragment
                    break;

                case PRIVATE_ROOM:
                    if (input_password.getText().toString().equals("")){
                        promptPasswordSetting();
                        break;
                    }
                    if (a_private_room.getRoomEtry().getPwd() != null) {
                        callStartMeeting();
                        //Intent to_asr_private = new Intent(getActivity(), SpeakerPrivateRoomActivity.class);
                        //startActivity(to_asr_private);
                    }
                    Log.println(Log.DEBUG, "ROOM", "wooorks" + a_private_room.getRoomEtry().getPwd());
                    //   ((NavigationHost) getActivity()).navigateTo(a_private_room, true); // Navigate to the next Fragment
                    break;
            }
            // click lets go button , also remove current fragment
            //getFragmentManager().beginTransaction().remove(StartRoomFragment.this).commit();
        });
        return view;
    }

    public void promptPasswordSetting() {
        MaterialAlertDialogBuilder dialog_builder;
        dialog_builder = new MaterialAlertDialogBuilder(getActivity()).setTitle("Input Your Password for the private Room");

        //SET UP input textview
        input_password = new EditText(getActivity());
        input_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog_builder.setView(input_password);

        // Set up the buttons
        dialog_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //输完密码后才能创建房间OBJEC。 新建的RoomEntry绑定在一个PrivateAcitivty上。
                a_private_room = new SpeakerPrivateRoomActivity();
                a_private_room.initRoomEtry(roomTitleInput.getText().toString(), roomDescriptionInput.getText().toString());
                a_private_room.setPasswordPrivateRoom(input_password.getText().toString());
                Log.println(Log.DEBUG, "ROOM", "wooorks" + a_private_room.getRoomEtry().getPwd());
            }
        });
        dialog_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog_builder.show();
    }

    public void callStartMeeting() {
        new Thread(() -> {

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            String roomTitle = roomTitleInput.getText().toString();
            String roomDescription = roomDescriptionInput.getText().toString();
            User user = userLocalStore.getLoggedInUser();
            String userName = user.getUserName();
            String direct = spinner.getSelectedItem().toString();
            int currentItem = carouselView.getCurrentItem();
            String url = imageUrls.get(currentItem);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("roomTitle", roomTitle);
                jsonObject.put("roomDescription", roomDescription);
                jsonObject.put("userName", userName);
                jsonObject.put("imageUrl", url);
                if (direct.equals("zh")){
                    jsonObject.put("direct", 0);
                } else {
                    jsonObject.put("direct", 1);
                }
                if (selectedRoom == RoomType.PRIVATE_ROOM ) {
                        String pwd = a_private_room.getRoomEtry().getPwd();
                        jsonObject.put("pwd", pwd);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder().post(requestBody).url(DemoConfig.SERVER_PATH + DemoConfig.ROUTE_STARTROOM).build();
            Call call = okHttpClient.newCall(request);

            // this makes asynchronous call to server
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    startMeetingProgressDialog.dismiss();
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    //dismiss login loading bar
                    startMeetingProgressDialog.dismiss();
                    //handling json response, getting fields
                    final String myresponse_json = response.body().string();
                    Log.println(Log.DEBUG, "OUT_JSON", myresponse_json);
                    Gson gson = new Gson();
                    Properties extractData = gson.fromJson(myresponse_json, Properties.class);
                    String state = extractData.getProperty("state");
                    Log.println(Log.DEBUG, "LOG", state);

                    // check response "state"
                    if (state.equals("1")) {
                        //need to update UI thread with a new thread,dont block UI thread
                        String errorMessage = extractData.getProperty("errorMessage");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MaterialAlertDialogBuilder dialogBuilder;
                                dialogBuilder = new MaterialAlertDialogBuilder(getActivity()).setTitle(errorMessage);
                                dialogBuilder.show();
                            }
                        });
                    } else if (state.equals("0")) { //get UID here
                        //need to update UI thread with a new thread,dont block UI thread
                        String meetingId = extractData.getProperty("meetingId");
                        Objects.requireNonNull(getActivity()).runOnUiThread(
                                () -> {
                                    switch (selectedRoom) {
                                        case PUBLIC_ROOM:
                                            Intent to_asr_public = new Intent(getActivity(), SpeakerPublicRoomActivity.class);
                                            to_asr_public.putExtra("meetingId", meetingId);
                                            to_asr_public.putExtra("direct", direct);
                                            startActivity(to_asr_public);
                                            break;
                                        case PRIVATE_ROOM:
                                            Intent to_asr_private = new Intent(getActivity(), SpeakerPrivateRoomActivity.class);
                                            to_asr_private.putExtra("meetingId", meetingId);
                                            to_asr_private.putExtra("direct", direct);
                                            startActivity(to_asr_private);
                                            break;
                                    }
                                }
                        );
                    }

                }
            });
        }).start();
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
             Picasso.get().load(imageUrls.get(position)).into(imageView);
        }
    };


}
