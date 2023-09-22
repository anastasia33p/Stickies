package ru.cation.stickies.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.cation.stickies.models.StickiesItem;

@Dao
public interface StickiesItemDAO {

    @Query("SELECT * FROM stickiesBase")
    Flowable<List<StickiesItem>> getAllWallpaperItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertStickiesItem(StickiesItem stickiesItem);

    @Query("DELETE FROM stickiesBase WHERE id = :id")
    Completable removeStickiesItemById(String id);

    @Query("SELECT * FROM stickiesBase WHERE id = :id")
    Single<StickiesItem> getStickiesItemById(String id);
}
