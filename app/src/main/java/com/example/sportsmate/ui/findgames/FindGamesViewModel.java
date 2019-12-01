package com.example.sportsmate.ui.findgames;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FindGamesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FindGamesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Find Games");
    }

    public LiveData<String> getText() {
        return mText;
    }
}