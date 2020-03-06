package com.youssefdirani.temphum_v04.ui.temphum;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TempHumViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TempHumViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}