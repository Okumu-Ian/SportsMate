package com.example.sportsmate.ui.creategame;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateGameViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CreateGameViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Create Game");
    }

    public LiveData<String> getText() {
        return mText;
    }
}