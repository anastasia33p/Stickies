package ru.cation.stickies.ui;

import ru.cation.stickies.models.StickiesItem;

public interface OnItemClickListener {
    void onItemClick(StickiesItem item, int position);
}
