package ru.cation.stickies.database;

import static androidx.lifecycle.Transformations.map;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.cation.stickies.models.StickiesItem;

public class StickiesItemRepo {
    private final StickiesItemDAO stickiesItemDAO;

    public StickiesItemRepo(Context context) {
        StickiesItemDatabase database = StickiesItemDatabase.getInstance(context);
        stickiesItemDAO = database.stickiesItemDAO();
    }

    public Flowable<List<StickiesItem>> getAllStickiesItems() {
        return stickiesItemDAO.getAllWallpaperItems();
    }

    public Completable insertStickiesItem(StickiesItem stickiesItem ) {
        return stickiesItemDAO.insertStickiesItem(stickiesItem);
    }

    public Completable removeStickiesItemById(String id) {
        return stickiesItemDAO.removeStickiesItemById(id);
    }

    public Single<StickiesItem> getStickiesItemById(String id){
        return stickiesItemDAO.getStickiesItemById(id);
    }
}


