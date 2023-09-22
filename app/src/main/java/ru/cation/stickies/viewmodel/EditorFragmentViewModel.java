
package ru.cation.stickies.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.cation.stickies.database.StickiesItemRepo;
import ru.cation.stickies.databinding.ActivityMainBinding;
import ru.cation.stickies.models.StickiesItem;
import ru.cation.stickies.ui.EditorFragment;

public class EditorFragmentViewModel extends AndroidViewModel {

    private StickiesItemRepo repo;

    public EditorFragmentViewModel(@NonNull Application application) {
        super(application);
        repo= new StickiesItemRepo(application);

    }



    public void addSticker(String data){
        String id="meow";
        String name="test";
        StickiesItem stickiesItem= new StickiesItem(id,name,data);
        repo.insertStickiesItem(stickiesItem).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();

    }

}
