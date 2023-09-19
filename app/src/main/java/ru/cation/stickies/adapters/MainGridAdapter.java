package ru.cation.stickies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.cation.stickies.R;
import ru.cation.stickies.databinding.StickerItemBinding;

public class MainGridAdapter extends RecyclerView.Adapter {


    private final Context context;
    private List<String> texts;

    public MainGridAdapter(Context context, List<String> texts) {
        this.context = context;
        this.texts = texts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        StickerItemBinding binding = StickerItemBinding.bind(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sticker_item, parent, false));

        return new StickerItemHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String ss ="";
        for (int i = 0; i < position; i++) {
            ss += position+" "+texts.get(position)+"\n";
        }
        ((StickerItemHolder)holder).binding.textView.setText(ss);
    }

    @Override
    public int getItemCount() {
        return texts.size();
    }


    public static class StickerItemHolder extends RecyclerView.ViewHolder {
        StickerItemBinding binding;

        public StickerItemHolder(@NonNull StickerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
