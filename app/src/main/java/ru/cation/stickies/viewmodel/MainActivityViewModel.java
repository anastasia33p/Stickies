package ru.cation.stickies.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> count;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        count=new MutableLiveData<>();
        count.postValue(0);
    }


    public MutableLiveData<Integer> getCount() {
        return count;
    }

    public void increase() {
        count.postValue(count.getValue()+1);
    }
}

