package ru.cation.stickies.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import ru.cation.stickies.databinding.StickerItemBinding;
import ru.cation.stickies.models.StickiesItem;
import ru.cation.stickies.ui.OnItemClickListener;
import ru.cation.stickies.ui.OnItemLongClickListener;

public class MainGridAdapter extends ListAdapter<StickiesItem, MainGridAdapter.StickerItemHolder> {
    private final OnItemClickListener itemClickListener;
    private final OnItemLongClickListener itemLongClickListener;

    public MainGridAdapter(OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener) {
        super(new StickiesItemDiffCallback());
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
    }


    @NonNull
    @Override
    public StickerItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StickerItemBinding binding = StickerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StickerItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerItemHolder holder, int position) {
        StickiesItem item = getItem(position);
        holder.binding.textView.setText(item.getText());
        holder.binding.stickerItem.setOnClickListener(view -> itemClickListener.onItemClick(getItem(position), position));
        holder.binding.stickerItem.setOnLongClickListener(view -> itemLongClickListener.onItemLongClick(getItem(position), position));
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


    public static class StickerItemHolder extends RecyclerView.ViewHolder {
        StickerItemBinding binding;

        public StickerItemHolder(@NonNull StickerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class StickiesItemDiffCallback extends DiffUtil.ItemCallback<StickiesItem> {
        @Override
        public boolean areItemsTheSame(@NonNull StickiesItem oldItem, @NonNull StickiesItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull StickiesItem oldItem, @NonNull StickiesItem newItem) {
            return oldItem.equals(newItem);
        }
    }

}
