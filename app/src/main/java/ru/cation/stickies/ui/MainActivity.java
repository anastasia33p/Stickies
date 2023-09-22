package ru.cation.stickies.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import ru.cation.stickies.adapters.MainGridAdapter;
import ru.cation.stickies.databinding.ActivityMainBinding;
import ru.cation.stickies.models.StickiesItem;
import ru.cation.stickies.viewmodel.MainActivityViewModel;


public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    private ActivityMainBinding binding;
    private MainGridAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private List<String> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new MainActivityViewModel(getApplication());


        adapter = new MainGridAdapter(itemClick);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.content.setAdapter(adapter);
        binding.content.setLayoutManager(layoutManager);
        observeContent();


        initClicks();


    }
    public OnItemClickListener itemClick = (item, position) -> {
        EditorFragment fragment=new EditorFragment(getApplication());
        fragment.show(getSupportFragmentManager(), "hkh");
    };
    private void initClicks() {
        binding.add.setOnClickListener(view -> {
            EditorFragment fragment=new EditorFragment(getApplication());
            fragment.show(getSupportFragmentManager(), "hkh");
        });
    }

    private void observeContent(){
        viewModel.getStickiesItemLiveData().observe(this, stickerItems -> {
            adapter.submitList(null);
            adapter.submitList(stickerItems);

        });
    }
}