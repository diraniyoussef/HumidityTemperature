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

    private String panel_index = "charcoal_humidity_panel";
    private String message;
    private String message_header;

    private SocketConnection localSocketConnection, internetSocketConnection;
    Boolean isLocal, isInternet;

    private MainActivity activity;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);//this is deprecated so next line is better
        //TempHumViewModel viewModel = new ViewModelProvider(this).get(TempHumViewModel.class);
        root = inflater.inflate(R.layout.fragment_temphum, container, false);
        activity = (MainActivity) getActivity();
        /*
        final TextView textView_humidity = root.findViewById(R.id.text_humidity);
        final TextView textView_temperature = root.findViewById(R.id.text_temperature);
        */
/*
        viewModel.getText().observe( getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
  */


        return root;
    }

    private void messageServerWithWiFiCheck( SocketConnection socketConnection, boolean silent_wifi ) {
        if( WiFiConnection.wiFiValid( activity.getApplicationContext(), silent_wifi, activity.toasting, isLocal ) ) {
            try {
                Log.i("Youssef info..electr", "message is " + message);
                socketConnection.socketConnectionSetup(message);
            } catch (Exception e) {
                Log.i("Youssef info..electr", "Error in messageTheServer");
                e.printStackTrace();
            }
        }
    }

    private void messageServerThroughInternet( SocketConnection socketConnection ) {
        try {
            Log.i("Youssef info..electr", "message is " + message);
            socketConnection.socketConnectionSetup(message);
        } catch (Exception e) {
            Log.i("Youssef info..electr", "Error in messageTheServer");
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        final MainActivity activity = (MainActivity) getActivity();
        //activity.toasting.toast("Value has changed", Toast.LENGTH_LONG);

        //activity.toasting.toast("Value has changed", Toast.LENGTH_LONG);
        final String panel_name = "رطوبة - حرارة";
        final String panel_type = "informing"; //either obeying or informing or empty. I do have a protection mechanism though, so it's ok if you forget it.. This variable is probably only used in ConfigPanel class.

        if (activity != null) {
            activity.panel_index = panel_index;
            activity.panel_name = panel_name;
            activity.panel_type = panel_type;
            /* //This used to be in cooperation with MutableLiveData<String> localNotInternet in MainActivity.java
              activity.localNotInternet.observe(activity, new Observer<String>() {
                @Override
                public void onChanged(String changedValue) {
                //This is called even at the beginning, so the code to communicate will be here.
                    String localNotInternet_str = activity.localNotInternet.getValue();
                }
              });
             */

            message_header = activity.mob_Id + panel_index + ":";
            message = message_header + "R?\0";

            ServerConfig localServerConfig = new ServerConfig( panel_index, panel_name, 11359, 11360 );
            ServerConfig internetServerConfig = new ServerConfig( panel_index, panel_name, 11359, 11360 );

            final SharedPreferences network_prefs = activity.getSharedPreferences(panel_index + "_networkConfig", 0);

            isLocal = network_prefs.getBoolean("local", true); //this info is written in ConfigPanel where we determine whether the panel supports local communication
            isInternet = network_prefs.getBoolean("internet", true); //same here.

            if( isLocal ) {
                localServerConfig.staticIP = network_prefs.getString("staticIP", "192.168.1.215"); //"192.168.1.215" is a dummy value
                localSocketConnection = new SocketConnection(activity.toasting, false, activity,
                        activity.getApplicationContext(), localServerConfig, 2,
                        true, activity.owner_part, activity.mob_part, activity.mob_Id);
                messageServerWithWiFiCheck( localSocketConnection,true);
            }
            if( isInternet ) {
                Log.i("TempHumFrag...", "Youssef/ setting Internet Dictionary");
                internetServerConfig.setInternetDictionary();
                internetSocketConnection = new SocketConnection(activity.toasting, false, activity,
                        activity.getApplicationContext(), internetServerConfig, 2,
                        false, activity.owner_part, activity.mob_part, activity.mob_Id);
                messageServerThroughInternet( internetSocketConnection );
            }

            //Button refreshButton = activity.findViewById(R.id.buttonHumTemperature_refresh); //doesn't work when you go back to this fragment. And this is logical since the refresh button belongs to the fragment and not the activity
            Button refreshButton = root.findViewById(R.id.buttonHumTemperature_refresh);
            refreshButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    message = message_header + "R?\0";
                    if( isLocal ) {
                        messageServerWithWiFiCheck( localSocketConnection,false);
                    }
                    if( isInternet ) {
                        messageServerThroughInternet( internetSocketConnection );
                    }
                }
            });

        }



    }

}