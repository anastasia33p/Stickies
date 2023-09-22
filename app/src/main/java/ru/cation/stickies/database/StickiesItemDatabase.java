package ru.cation.stickies.database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.cation.stickies.models.StickiesItem;


@Database(entities = {StickiesItem.class}, version = 1)
public abstract class StickiesItemDatabase extends RoomDatabase {

    private static StickiesItemDatabase INSTANCE;

    public abstract StickiesItemDAO stickiesItemDAO();

    public static synchronized StickiesItemDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            StickiesItemDatabase.class, "stickiesBase")
                    .build();
        }
        return INSTANCE;
    }
}