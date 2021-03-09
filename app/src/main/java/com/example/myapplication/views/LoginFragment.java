package com.example.myapplication.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.views.nav.NavigationHost;
import com.example.myapplication.views.room_list.RoomGridFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.tencent.iot.speech.app.DemoConfig;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Fragment representing the login screen for MH.
 */
public class LoginFragment extends Fragment  {



    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        final TextInputEditText usernameEditText=view.findViewById(R.id.username_edit_text);
        final TextInputLayout passwordtextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);

        MaterialButton cancelButton=view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEditText.setText("");
                passwordEditText.setText("");
            }
        });
        ProgressDialog login_prog=new ProgressDialog(getContext());

        login_prog.setTitle("Processing...");
        login_prog.setMessage("Please wait...");
        login_prog.setCancelable(false);
        login_prog.setIndeterminate(true);

        MaterialButton nextButton = view.findViewById(R.id.next_button);
        // Set an error if the password is less than 8 characters.
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPasswordValid(passwordEditText.getText())) {
                    passwordtextInput.setError(getString(R.string.shr_error_password));
                } else {
                    passwordtextInput.setError(null); // Clear the error
                    login_prog.show();
                     new Thread(()->{
                         OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                 .connectTimeout(10, TimeUnit.SECONDS)
                                 .readTimeout(10,TimeUnit.SECONDS)
                                 .writeTimeout(10, TimeUnit.SECONDS)
                                 .build();
                         String username=usernameEditText.getText().toString();
                         String password=passwordEditText.getText().toString();

                         FormBody formBody=new FormBody.Builder().add("userName",username).add("userPwd",password).build();
                         Request request=new Request.Builder().post(formBody).url(DemoConfig.SERVER_PATH +DemoConfig.ROUTE_LOGIN).build();
                         Call call= okHttpClient.newCall(request);

                        // this makes asynchronous call to server
                         call.enqueue(new Callback() {
                             @Override
                             public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                 login_prog.dismiss();
                                e.printStackTrace();
                             }

                             @Override
                             public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                 //dismiss login loading bar
                                 login_prog.dismiss();
                                 //handling json response, getting fields
                                 final String myresponse_json=response.body().string();
                                 Log.println(Log.DEBUG,"OUT_JSON",myresponse_json);
                                 Gson gson=new Gson();
                                 Properties extract_data=gson.fromJson(myresponse_json,Properties.class);
                                 String state=extract_data.getProperty("state");
                                 Log.println(Log.DEBUG,"LOG",state);
                                 String message=extract_data.getProperty("message");

                               // check response "state"
                                 if(state.equals("0")){
                                     //need to update UI thread with a new thread,dont block UI thread
                                     getActivity().runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {
                                             passwordEditText.setText(myresponse_json);

                                             MaterialAlertDialogBuilder dialog_builder;
                                             dialog_builder=new MaterialAlertDialogBuilder(getActivity()).setTitle(message);
                                             dialog_builder.show();
                                         }

                                     });
                                 }
                                 else if(state.equals("1")){
                                     //need to update UI thread with a new thread,dont block UI thread
                                     getActivity().runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {
                                             passwordEditText.setText(myresponse_json);
                                         }
                                     });
                                 }

                                 ((NavigationHost) getActivity()).navigateTo(new RoomGridFragment(), true);
                             }
                         });


                     }).start();

                 //  ((NavigationHost) getActivity()).navigateTo(new RoomGridFragment(), true); // Navigate to the next Fragment :addtoStack set false , need to navigate back to RoomGrid
                }
            }
        });

        // Clear the error once more than 8 characters are typed.
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (isPasswordValid(passwordEditText.getText())) {
                    passwordtextInput.setError(null); //Clear the error
                }
                return false;
            }

        });

        return view;
    }

    /*
       In reality, this will have more complex logic including, but not limited to, actual
       authentication of the username and password.
    */
    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }


}