package ru.cation.stickies.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.cation.stickies.database.StickiesItemRepo;
import ru.cation.stickies.models.StickiesItem;

public class MainActivityViewModel extends AndroidViewModel {


    private final MutableLiveData<List<StickiesItem>> stickiesItemsLiveData;
    private final StickiesItemRepo repo;

    @SuppressLint("CheckResult")
    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        stickiesItemsLiveData = new MutableLiveData<>();

        repo = new StickiesItemRepo(application);

        repo
                .getAllStickiesItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stickiesItemsLiveData::postValue);

    }

    public MutableLiveData<List<StickiesItem>> getStickiesItemLiveData() {
        return stickiesItemsLiveData;
    }



}

