package ru.cation.stickies.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="stickiesBase")
public class StickiesItem {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String text;

    public StickiesItem(@NonNull String id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StickiesItem)) return false;
        StickiesItem that = (StickiesItem) o;
        return id == that.id;
    }
}
