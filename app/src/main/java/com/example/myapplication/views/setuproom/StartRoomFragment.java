package com.example.myapplication.views.setuproom;

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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.myapplication.R;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


//This fragment user input room name and other info to start a room
// functions:
// 1. language setting
// 2.pub/ priv
// 3. input room info
public class StartRoomFragment extends BottomSheetDialogFragment {
    ImageButton publicRoom;
    ImageButton privateRoom;
    MaterialButton letgo;
    TextView roomTitleInput;
    TextView roomDescriptionInput;
    SpeakerPublicRoomActivity aPubRoom;
    SpeakerPrivateRoomActivity aPrivateRoom;

    enum RoomType {
        PUBLIC_ROOM,
        PRIVATE_ROOM
    }

    RoomType selectedRoom;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment with the ProductGrid theme
        View view = inflater.inflate(R.layout.mh_startroom_page, container,false);

        publicRoom = view.findViewById(R.id.public_room);
        privateRoom = view.findViewById(R.id.private_room);
        letgo = view.findViewById(R.id.startroom_button);
        roomDescriptionInput = view.findViewById(R.id.room_description_setting);
        roomTitleInput = view.findViewById(R.id.room_title_setting);

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
        Spinner spinner = (Spinner) view.findViewById(R.id.language_setting_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.language_direction, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // public room button--------------
        publicRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView roomSelectedDescription = view.findViewById(R.id.roomType_description);
                roomSelectedDescription.setText("Start a room open to everyone");
                letgo.setText("Open to everybody");
                selectedRoom = RoomType.PUBLIC_ROOM;
            }
        });

        privateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView roomSelectedDescription = view.findViewById(R.id.roomType_description);
                roomSelectedDescription.setText("Start a private room");
                letgo.setText("Start Private Room");
                selectedRoom = RoomType.PRIVATE_ROOM;
                Log.println(Log.DEBUG, "ROOM", "PRIVATEEEE");
                promptPasswordSetting();
            }
        });

        letgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //click letgo button ,save the room info to RoomEntry
                switch (selectedRoom) {
                    case PUBLIC_ROOM:
                        Log.println(Log.DEBUG, "ROOM", "PUBLICCCC");
                        //start one room here
                        aPubRoom = new SpeakerPublicRoomActivity();
                        aPubRoom.initRoomEtry(roomTitleInput.getText().toString(), roomDescriptionInput.getText().toString());
                        //go to asr room
                        Intent to_asr_public = new Intent(getActivity(), SpeakerPublicRoomActivity.class);
                        startActivity(to_asr_public);
                        //((NavigationHost) getActivity()).navigateTo(a_pub_room, true); // Navigate to the next Fragment
                        break;

                    case PRIVATE_ROOM:
                        if (aPrivateRoom.getRoomEtry().getPwd() != null) {
                            Intent to_asr_private = new Intent(getActivity(), SpeakerPrivateRoomActivity.class);
                            startActivity(to_asr_private);
                        }
                        Log.println(Log.DEBUG, "ROOM", "wooorks" + aPrivateRoom.getRoomEtry().getPwd());
                        //   ((NavigationHost) getActivity()).navigateTo(a_private_room, true); // Navigate to the next Fragment
                        break;

                    default:
                        Log.println(Log.DEBUG, "WRONG", "MUST BE EITHER PUBLIC OR PRIVATE");
                }
                // click lets go button , also remove current fragment
                getFragmentManager().beginTransaction().remove(StartRoomFragment.this).commit();
                //clear text in inputbox
                roomDescriptionInput.setText("");
                roomTitleInput.setText("");
            }
        });
        return view;
    }

    public void promptPasswordSetting() {
        MaterialAlertDialogBuilder dialog_builder;
        dialog_builder = new MaterialAlertDialogBuilder(getActivity()).setTitle("Input Your Password for the private Room");

        //SET UP input textview
        final EditText input_password = new EditText(getActivity());
        input_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog_builder.setView(input_password);

// Set up the buttons
        dialog_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //输完密码后才能创建房间OBJEC。 新建的RoomEntry绑定在一个PrivateAcitivty上。
                aPrivateRoom = new SpeakerPrivateRoomActivity();
                aPrivateRoom.initRoomEtry(roomTitleInput.getText().toString(), roomDescriptionInput.getText().toString());
                aPrivateRoom.setPasswordPrivateRoom(input_password.getText().toString());
                Log.println(Log.DEBUG, "ROOM", "wooorks" + aPrivateRoom.getRoomEtry().getPwd());

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

}
