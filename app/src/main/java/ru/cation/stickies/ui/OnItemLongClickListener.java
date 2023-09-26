package ru.cation.stickies.ui;

import ru.cation.stickies.models.StickiesItem;

public interface OnItemLongClickListener {
    void onItemLongClick(StickiesItem item, int position);
}
