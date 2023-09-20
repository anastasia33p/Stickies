package ru.cation.stickies.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.cation.stickies.R;
import ru.cation.stickies.adapters.MainGridAdapter;
import ru.cation.stickies.databinding.ActivityMainBinding;
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


        list.add("sdfdfs");
        list.add("sdfdfs");
        list.add("sdfdfs");
        list.add("sdfdfs");

        viewModel = new MainActivityViewModel(getApplication());

        adapter = new MainGridAdapter(this, list);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.content.setAdapter(adapter);
        binding.content.setLayoutManager(layoutManager);



        initClicks();

        viewModel.getCount().observe(this, integer -> {
            Toast.makeText(this, integer.toString(), Toast.LENGTH_SHORT).show();

        });
    }

    private void initClicks() {
        binding.add.setOnClickListener(view -> {
            viewModel.increase();
            list.add("fdsfdsjlksdh");
            adapter.notifyItemInserted(list.size()-1);
            EditorFragment fragment=new EditorFragment();

            fragment.show(getSupportFragmentManager(), "hkh");


        });
    }
}