package com.youssefdirani.HumTemp_v04.ui.home;

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
//import androidx.lifecycle.ViewModelProvider; //fine
//import androidx.lifecycle.ViewModelProviders; //deprecated so replaced by the fine one

import com.youssefdirani.HumTemp_v04.MainActivity;
import com.youssefdirani.HumTemp_v04.R;
import com.youssefdirani.HumTemp_v04.ServerConfig;
import com.youssefdirani.HumTemp_v04.SocketConnection;
import com.youssefdirani.HumTemp_v04.WiFiConnection;

public class HomeFragment extends Fragment { //in principle, this fragment represents the charcoal humidity and temperature panel.

    //private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);//this is deprecated so next line is better
        //homeViewModel =new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        /*
        final TextView textView_humidity = root.findViewById(R.id.text_humidity);
        final TextView textView_temperature = root.findViewById(R.id.text_temperature);
        /*
        homeViewModel.getText().observe( getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */

        final MainActivity mainActivity = (MainActivity) getActivity();
        //mainActivity.toasting.toast("Value has changed", Toast.LENGTH_LONG);
        final String panel_name = "charcoal humidity panel";
        /*final String panel_index = "charcoal_humidity_panel";
          //unfortunately I have to access this variable from the mainActivity, though in principle it should be here declared.
            */
        if( mainActivity != null ) {
            mainActivity.localNotInternet.observe(mainActivity, new Observer<String>() {
                private String message;
                private String message_header;
                SocketConnection socketConnection;
                boolean localNotInternet = false;

                @Override
                public void onChanged(String changedValue) {
                    //This is called even at the beginning, so the code to communicate will be here.
                    ServerConfig selectedServerConfig = new ServerConfig(mainActivity.panel_index, panel_name);
                    String localNotInternet_str = mainActivity.localNotInternet.getValue();
                    if (localNotInternet_str != null && localNotInternet_str.equals("local")) {
                        localNotInternet = true;
                        String dummy_default_IP = "192.168.1.215";
                        final SharedPreferences network_prefs = mainActivity.getSharedPreferences("network_config", 0);
                        selectedServerConfig.staticIP = network_prefs.getString(mainActivity.panel_index, dummy_default_IP);
                    } else {
                        localNotInternet = false;
                        Log.i("Youssef Infor..Elec", "setting Internet Dictionary");
                        selectedServerConfig.setInternetDictionary();
                    }
                    Button refreshButton = mainActivity.findViewById(R.id.buttonHumTemperature_refresh);
                    //TextView textView_humidity = mainActivity.findViewById( R.id.text_humidity );
                    message_header = mainActivity.mob_Id + selectedServerConfig.panel_index + ":";
                    socketConnection = new SocketConnection(mainActivity.toasting, false, mainActivity,
                            mainActivity.getApplicationContext(), selectedServerConfig, 2,
                            localNotInternet, mainActivity.owner_part, mainActivity.mob_part, mainActivity.mob_Id);
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
                    if (WiFiConnection.wiFiValid(mainActivity.getApplicationContext(), silent_wifi, mainActivity.toasting, localNotInternet)) {
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