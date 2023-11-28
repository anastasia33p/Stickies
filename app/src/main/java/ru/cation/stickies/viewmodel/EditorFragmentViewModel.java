
package ru.cation.stickies.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.cation.stickies.database.StickiesItemRepo;
import ru.cation.stickies.models.StickiesItem;

public class EditorFragmentViewModel extends AndroidViewModel {

    private StickiesItemRepo repo;


    public EditorFragmentViewModel(@NonNull Application application) {
        super(application);
        repo = new StickiesItemRepo(application);

    }

    private List<Integer> getIdList(List<StickiesItem> stickiesItem) {
        List<Integer> idList = new ArrayList<>();
        for (StickiesItem item : stickiesItem) {
            String id = item.getId();
            idList.add(Integer.parseInt(id));
        }
        return idList;
    }

    private Integer findId(List<Integer> idList) {

        if (idList.isEmpty()) {
            return 0;
        }

        idList.sort(Integer::compareTo);

        int i = 0;
        for (int id : idList) {
            if (id != i) {
                return i;
            }
            i++;
        }
        return i;

    }

    @SuppressLint("CheckResult")
    public void addSticker(String data, StickiesItem item) {
        if (item.getId() == "new") {
            repo
                    .getAllStickiesItems().take(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(stickiesItems -> {
                        String id = findId(getIdList(stickiesItems)).toString();
                        String name = "test";
                        StickiesItem stickiesItem = new StickiesItem(id, name, data);
                        repo.insertStickiesItem(stickiesItem). observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();
                    }, throwable -> {
                    });
        } else {
            repo
                    .getAllStickiesItems().take(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(stickiesItems -> {
                        String id = item.getId();
                        String name = "test";
                        StickiesItem stickiesItem = new StickiesItem(id, name, data);
                        repo.insertStickiesItem(stickiesItem).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();
                    }, throwable -> {
                    });
        }
    }

}
