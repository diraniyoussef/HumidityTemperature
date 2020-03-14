package com.youssefdirani.temphum_v04.ui.waterheater;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.youssefdirani.temphum_v04.MainActivity;
import com.youssefdirani.temphum_v04.R;

public class WaterHeaterFragment extends Fragment {

    private final String panel_index = "water_heater_panel";
    private final String panel_name = "سخّان المياه";


    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_waterheater, container, false);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        final String panel_type = "obeying"; //either obeying or informing or empty. I do have a protection mechanism though, so it's ok if you forget it.. This variable is probably only used in ConfigPanel class.
        final MainActivity activity = (MainActivity) getActivity();
        //activity.toasting.toast("Value has changed", Toast.LENGTH_LONG);


        if( activity != null ) {
            activity.panel_index = panel_index;
            activity.panel_name = panel_name;
            activity.panel_type = panel_type;
        }

        //Among the shared preferences attributes, we usually care about the static IP string, the local boolean, and the internet boolean.
        Log.i( "WaterHeaterFragment", "id is " + this.getId() );

    }
}