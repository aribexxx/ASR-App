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
import com.example.myapplication.models.User;
import com.example.myapplication.control.UserLocalStore;
import com.example.myapplication.views.nav.NavigationHost;
import com.example.myapplication.views.room_list.RoomGridFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.example.myapplication.utils.DemoConfig;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Fragment representing the login screen for MH.
 *
 * 调 LOGIN /SIGN UP REQUEST
 */
public class LoginFragment extends Fragment  {
    public  static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    UserLocalStore userLocalStore;
    private static  final String PASSWORDMSG="Password is wrong,Try again Plz";
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // set the userLocalStore
        userLocalStore = new UserLocalStore(this);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        final TextInputEditText usernameEditText = view.findViewById(R.id.username_edit_text);
        final TextInputLayout passwordtextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);

        MaterialButton cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEditText.setText("");
                passwordEditText.setText("");
            }
        });

        ProgressDialog loginProgressDialog = new ProgressDialog(getContext());
        loginProgressDialog.setTitle("Processing...");
        loginProgressDialog.setMessage("Please wait...");
        loginProgressDialog.setCancelable(false);
        loginProgressDialog.setIndeterminate(true);
        MaterialButton loginButton = view.findViewById(R.id.next_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPasswordValid(passwordEditText.getText())) {
                    passwordtextInput.setError(getString(R.string.shr_error_password));
                } else {
                    passwordtextInput.setError(null); // Clear the error
                    loginProgressDialog.show();
                    new Thread(() -> {
                        //GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                        //retrofit2.Call<User> call  = service.getUser(new LoginRequest(username, password));
                        //String username = usernameEditText.getText().toString();
                        //String password = passwordEditText.getText().toString();

                        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                 .connectTimeout(10, TimeUnit.SECONDS)
                                 .readTimeout(10,TimeUnit.SECONDS)
                                 .writeTimeout(10, TimeUnit.SECONDS)
                                 .build();
                        String username = usernameEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        //FormBody formBody = new FormBody.Builder().add("userName",username).add("userPwd",password).build();

                        // use json body instead of form body
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("userName", username);
                            jsonObject.put("userPwd", password);
                        } catch (Exception e){
                            System.out.println(e);
                        }
                        RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
                        //HttpUrl localUrl = HttpUrl.parse(DemoConfig.SERVER_PATH + DemoConfig.ROUTE_LOGIN);
                        //Request request = new Request.Builder().post(formBody).url(localUrl).build();
                        Request request = new Request.Builder().post(requestBody).url(DemoConfig.SERVER_PATH + DemoConfig.ROUTE_LOGIN).build();
                        Call call = okHttpClient.newCall(request);

                        // this makes asynchronous call to server
                        call.enqueue(new Callback() {
                             @Override
                             public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                 loginProgressDialog.dismiss();
                                 e.printStackTrace();
                             }

                             @Override
                             public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                 //dismiss login loading bar
                                 loginProgressDialog.dismiss();
                                 //handling json response, getting fields
                                 final String myresponse_json = response.body().string();
                                 Log.println(Log.DEBUG,"OUT_JSON",myresponse_json);
                                 Gson gson = new Gson();
                                 Properties extractData = gson.fromJson(myresponse_json,Properties.class);
                                 String state = extractData.getProperty("state");
                               // check response "state"
                              if (state!=null&& state.equals("0")) { //get UID here
                                     //need to update UI thread with a new thread,dont block UI thread
                                        String userId = extractData.getProperty("userId");
                                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                User newUser=new User(userId, username);
                                                userLocalStore.setUserLoggedIn(true);
                                                userLocalStore.storeUserData(newUser);
                                                //newUser.setUserId(userId);
                                                //passwordEditText.setText("UserId got "+ newUser.userId);
                                                ((NavigationHost) Objects.requireNonNull(getActivity())).navigateTo(new RoomGridFragment(), true);
                                                //((NavigationHost) Objects.requireNonNull(getActivity())).navigateTo(new TestWebSocketFragment(), true); // nov to test ws page
                                            }
                                     });
                                  }
                                 else if (state==null||!state.equals("1")){
                                     //need to update UI thread with a new thread,dont block UI thread
                                     String errorMessage = extractData.getProperty("error");
                                     getActivity().runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {
                                             passwordEditText.setText("");
                                             MaterialAlertDialogBuilder dialogBuilder;
                                             dialogBuilder = new MaterialAlertDialogBuilder(getActivity()).setTitle(PASSWORDMSG);
                                             dialogBuilder.show();
                                         }
                                     });
                                 }
                             }
                        });
                    }).start();
                 //  ((NavigationHost) getActivity()).navigateTo(new RoomGridFragment(), true); // Navigate to the next Fragment :addtoStack set false , need to navigate back to RoomGrid
                }
            }
        });

        MaterialButton signupButton = view.findViewById(R.id.sign_up_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPasswordValid(passwordEditText.getText())) {
                    passwordtextInput.setError(getString(R.string.shr_error_password));
                } else {
                    passwordtextInput.setError(null); // Clear the error
                    loginProgressDialog.show();
                    new Thread(() -> {
                        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10,TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .build();
                        String username = usernameEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        //FormBody formBody = new FormBody.Builder().add("userName",username).add("userPwd",password).build();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("userName", username);
                            jsonObject.put("userPwd", password);
                        } catch (Exception e){
                            System.out.println(e);
                        }
                        RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
                        Request request = new Request.Builder().post(requestBody).url(DemoConfig.SERVER_PATH + DemoConfig.ROUTE_SIGNUP).build();
                        Call call = okHttpClient.newCall(request);

                        // this makes asynchronous call to server
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                loginProgressDialog.dismiss();
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                //dismiss login loading bar
                                loginProgressDialog.dismiss();
                                //handling json response, getting fields
                                final String myresponse_json = response.body().string();
                                Log.println(Log.DEBUG,"OUT_JSON",myresponse_json);
                                Gson gson = new Gson();
                                Log.d("LOG",myresponse_json);
                                Properties extractData = gson.fromJson(myresponse_json,Properties.class);
                                String state = extractData.getProperty("state");
                                Log.println(Log.DEBUG,"LOG",state);

                                // check response "state"
                                if (state.equals("1")) {
                                    //need to update UI thread with a new thread,dont block UI thread
                                    String errorMessage = extractData.getProperty("error");
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            passwordEditText.setText("");
                                            MaterialAlertDialogBuilder dialogBuilder;
                                            dialogBuilder = new MaterialAlertDialogBuilder(getActivity()).setTitle(PASSWORDMSG);
                                            dialogBuilder.show();
                                        }
                                    });
                                } else if (state.equals("0")) { //get UID here
                                    //need to update UI thread with a new thread,dont block UI thread
                                    String userId = extractData.getProperty("userId");
                                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            User newUser=new User(userId, username);
                                            userLocalStore.setUserLoggedIn(true);
                                            userLocalStore.storeUserData(newUser);
                                            //newUser.setUserId(userId);
                                            //passwordEditText.setText("UserId got "+ newUser.getUserId());
                                            ((NavigationHost) Objects.requireNonNull(getActivity())).navigateTo(new RoomGridFragment(), true);
                                        }
                                    }
                                    );
                                }

                            }
                        });
                    }). start();
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
        return (text != null && !text.toString().equals("")&&text.length()>=3);
    }
}