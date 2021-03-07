package com.example.myapplication.views.room_list;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDestination;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.NavController;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;
import com.example.myapplication.models.RoomEntry;
import com.example.myapplication.views.nav.NavigationHost;
import com.example.myapplication.views.setup_room.StartRoomFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.iot.speech.app.DemoConfig;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//This is a GridView that display all the room cards + start room button

public class RoomGridFragment extends Fragment implements NavController.OnDestinationChangedListener{


ExtendedFloatingActionButton fab;
StartRoomFragment startroom_frag;
SwipeRefreshLayout my_swipe_refresh;
List<RoomEntry> roomlist;
RoomCardRecyclerViewAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment with the ProductGrid theme
        View view = inflater.inflate(R.layout.room_grid_fragment, container, false);



        // Set up the toolbar
        setUpToolbar(view);


        //swipe refresh layout
        init_SwipeRefreshLayout(view);

        //init bottom sheet
        startroom_frag=new StartRoomFragment();

        //init Fab
        initFab(view);


        // Set up the RecyclerView for rooms display
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
       // recyclerView.setLayoutManager(new );
        roomlist=RoomEntry.initRoomEntryList(getResources(),getResources().openRawResource(R.raw.rooms));
         adapter = new RoomCardRecyclerViewAdapter(this.getContext(),
                roomlist);
       // Log.println(Log.DEBUG,"SHOW",RoomEntry.initRoomEntryList(getResources()).toArray().toString());
        recyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new RoomGridItemDecoration(largePadding, smallPadding));


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

    public void initFab(View view){
        //initiate a fab
         fab = (ExtendedFloatingActionButton) view.findViewById(R.id.extended_fab);
        fab.setShowMotionSpecResource(R.animator.fab_show);
        fab.setHideMotionSpecResource(R.animator.fab_hide);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //((NavigationHost) getActivity()).navigateTo(new StartRoomFragment(), false); // Navigate to the next Fragment
                  startroom_frag.show(getFragmentManager(),startroom_frag.getTag());
            }

        });
    }



    public void init_SwipeRefreshLayout(View view){
        my_swipe_refresh=view.findViewById(R.id.swipe_refresh);
        my_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("RF", "onRefresh called from SwipeRefreshLayout");

                // This method performs the actual data-refresh operation.
                // The method calls setRefreshing(false) when it's finished.




               //swap items
                new Handler(getActivity().getMainLooper()).post(new Runnable() {
                    public void run() {

                       //Modify here to
                       // adapter.setRoomList(initRoomEntryList_OnServer());
                        adapter.setRoomList(RoomEntry.initRoomEntryList(getResources(),getResources().openRawResource(R.raw.new_rooms)));
                        adapter.notifyDataSetChanged();
                        Log.i("RF", "finish");
                        my_swipe_refresh.setRefreshing(false);
                    }
                });

            }
        });

    }



    /**
     * Loads server responds and converts it into a list of RoomEntry objects
     */
    public  List<RoomEntry> initRoomEntryList_OnServer(){

        List<RoomEntry> room_list = null;
        new Thread(()->{
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10,TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            // FormBody formBody=new FormBody.Builder().build();
            Request request=new Request.Builder().get().url(DemoConfig.SERVER_PATH +DemoConfig.route_showroom).build();
            Call call= okHttpClient.newCall(request);

            // this makes asynchronous call to server
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    //handling json response, getting a list of existing rooms info
                    final String myresponse_json=response.body().string();
                    Gson gson = new Gson();
                    Properties data=gson.fromJson(myresponse_json,Properties.class);
                    String state=data.getProperty("state");
                    String all_rooms=data.getProperty("meeting");
                    
                    
                    // check response "state"
                    if(state.equals("0")){
                        //need to update UI thread with a new thread,dont block UI thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MaterialAlertDialogBuilder dialog_builder;
                                dialog_builder=new MaterialAlertDialogBuilder(getActivity()).setTitle("Fail to get room list from server");
                                dialog_builder.show();
                            }

                        });
                    }
                    else if(state.equals("1")){
                        //need to update UI thread with a new thread,dont block UI thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                           
                            }
                        });
                    }
                    
                }
            });


        }).start();

      return room_list;

    }


}
