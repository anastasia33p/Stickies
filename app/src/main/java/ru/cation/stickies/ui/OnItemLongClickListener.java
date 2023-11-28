package ru.cation.stickies.ui;

import ru.cation.stickies.models.StickiesItem;

public interface OnItemLongClickListener {
    boolean onItemLongClick(StickiesItem item, int position);
}
