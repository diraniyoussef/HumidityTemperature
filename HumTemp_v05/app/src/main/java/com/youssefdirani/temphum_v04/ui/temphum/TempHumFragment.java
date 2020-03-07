package com.youssefdirani.temphum_v04.ui.temphum;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider; //fine
//import androidx.lifecycle.ViewModelProviders; //deprecated so replaced by the fine one

import com.youssefdirani.temphum_v04.MainActivity;
import com.youssefdirani.temphum_v04.R;
import com.youssefdirani.temphum_v04.ServerConfig;
import com.youssefdirani.temphum_v04.SocketConnection;
import com.youssefdirani.temphum_v04.WiFiConnection;

public class TempHumFragment extends Fragment { //in principle, this fragment represents the charcoal humidity and temperature panel.

    private TempHumViewModel tempHumViewModel;
    private String panel_index = "charcoal_humidity_panel";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);//this is deprecated so next line is better
        tempHumViewModel = new ViewModelProvider(this).get(TempHumViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_temphum, container, false);
        /*
        final TextView textView_humidity = root.findViewById(R.id.text_humidity);
        final TextView textView_temperature = root.findViewById(R.id.text_temperature);
        */
/*
        homeViewModel.getText().observe( getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
  */

        final MainActivity activity = (MainActivity) getActivity();
        //activity.toasting.toast("Value has changed", Toast.LENGTH_LONG);
        final String panel_name = "charcoal humidity panel";


        if( activity != null ) {
            activity.panel_index = panel_index;
            activity.localNotInternet.observe(activity, new Observer<String>() {
                private String message;
                private String message_header;
                SocketConnection socketConnection;
                boolean localNotInternet = false;

                
                
                @Override
                public void onChanged(String changedValue) {
                    //This is called even at the beginning, so the code to communicate will be here.
                    ServerConfig selectedServerConfig = new ServerConfig(panel_index, panel_name);
                    
                    String localNotInternet_str = activity.localNotInternet.getValue();
                    if (localNotInternet_str != null && localNotInternet_str.equals("local")) {
                        localNotInternet = true;
                        String dummy_default_IP = "192.168.1.215";
                        final SharedPreferences network_prefs = activity.getSharedPreferences("network_config", 0);
                        selectedServerConfig.staticIP = network_prefs.getString(activity.panel_index, dummy_default_IP);
                    } else {
                        localNotInternet = false;
                        Log.i("Youssef Infor..Elec", "setting Internet Dictionary");
                        selectedServerConfig.setInternetDictionary();
                    }
                    //Button refreshButton = activity.findViewById(R.id.buttonHumTemperature_refresh); //doesn't work when you go back to this fragment. And this is logical since the refresh button belongs to the fragment and not the activity
                    Button refreshButton = root.findViewById(R.id.buttonHumTemperature_refresh);
                    //TextView textView_humidity = activity.findViewById( R.id.text_humidity );
                    message_header = activity.mob_Id + selectedServerConfig.panel_index + ":";
                    socketConnection = new SocketConnection(activity.toasting, false, activity,
                            activity.getApplicationContext(), selectedServerConfig, 2,
                            localNotInternet, activity.owner_part, activity.mob_part, activity.mob_Id);
                    message = message_header + "R?\0";
                    messageTheServer(true);
                    refreshButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            message = message_header + "R?\0";
                            messageTheServer(false);
                        }
                    });
                }

                private void messageTheServer(boolean silent_wifi) {
                    if (WiFiConnection.wiFiValid(activity.getApplicationContext(), silent_wifi, activity.toasting, localNotInternet)) {
                        try {
                            Log.i("Youssef info..electr", "message is " + message);
                            socketConnection.socketConnectionSetup(message);
                        } catch (Exception e) {
                            Log.i("Youssef info..electr", "Error in messageTheServer");
                            e.printStackTrace();
                        }
                    }
                }

            });
        }
        return root;
    }
}