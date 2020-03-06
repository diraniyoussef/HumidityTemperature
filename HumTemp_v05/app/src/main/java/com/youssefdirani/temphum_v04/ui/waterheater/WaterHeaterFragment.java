package com.youssefdirani.temphum_v04.ui.waterheater;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.youssefdirani.temphum_v04.R;

public class WaterHeaterFragment extends Fragment {

    private WaterHeaterViewModel waterHeaterViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        waterHeaterViewModel =
                ViewModelProviders.of(this).get(WaterHeaterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_waterheater, container, false);
        final TextView textView = root.findViewById(R.id.text_waterheater);
        waterHeaterViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}