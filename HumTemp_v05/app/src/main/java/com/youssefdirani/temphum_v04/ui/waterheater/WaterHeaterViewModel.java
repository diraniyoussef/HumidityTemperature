package com.youssefdirani.temphum_v04.ui.waterheater;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WaterHeaterViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WaterHeaterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}