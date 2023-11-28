package ru.cation.stickies.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.cation.stickies.R;
import ru.cation.stickies.adapters.MainGridAdapter;
import ru.cation.stickies.databinding.ActivityMainBinding;
import ru.cation.stickies.models.StickiesItem;
import ru.cation.stickies.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    private ActivityMainBinding binding;
    private MainGridAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private String userIP = "noIP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new MainActivityViewModel(getApplication());


        adapter = new MainGridAdapter(itemClick, itemLongClick);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.content.setAdapter(adapter);
        binding.content.setLayoutManager(layoutManager);
        observeContent();

        initClicks();

    }

    //слушаем нажатия на заметки и открываем редактор с передачей id
    public OnItemClickListener itemClick = (item, position) -> {
        EditorFragment fragment = new EditorFragment(getApplication());
        fragment.setId(item);
        fragment.show(getSupportFragmentManager(), "hkh");
    };

    //слушаем долгие нажатия на заметки и показываем диалог удаления
    public OnItemLongClickListener itemLongClick = (item, position) -> {
        showDeleteConfirmationDialog(item);
        return true;
    };

    //слушаем клики на том что кликается
    private void initClicks() {

        //если нажали на кнопочку добавления открываем редактор без передачи id
        binding.add.setOnClickListener(view -> {
            EditorFragment fragment = new EditorFragment(getApplication());
            fragment.show(getSupportFragmentManager(), "hkh");
        });

        //если нажали на кнопочку в тулбаре запускаем синхронизацию
        binding.toolbar.setOnMenuItemClickListener(item -> {
            syncClick();
            return true;
        });

        //если долгое нажатие на тулбар то открываем диалоговое окно со считыванием ip
        binding.toolbar.setOnLongClickListener(view -> {
            showAlertDialog();
            return true;
        });
    }

    //считывание ip и обновление глобальной переменной
    private void showAlertDialog() {
        final EditText input = new EditText(this);
        //input.setText("0.101");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите ip")
                .setView(input)
                .setPositiveButton("Подключение", (dialog, which) -> {
                    String userInput = input.getText().toString();
                    userIP = "192.168." + userInput;
                    viewModel.connect(userIP);
                })
                .setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void observeContent() {
        //следим за обновлениями базы данных
        viewModel.getStickiesItemLiveData().observe(this, stickerItems -> {
            adapter.submitList(null);
            adapter.submitList(stickerItems);
        });

        //следим если идет синхронизация то показываем загрузочку
        viewModel.getLoadingLiveData().observe(this, loading -> {
            if(loading){
                binding.loadingBar.setVisibility(View.VISIBLE);
            }else{
                binding.loadingBar.setVisibility(View.GONE);
            }
        });
    }

    //
    private void showDeleteConfirmationDialog(StickiesItem item) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Удаление");
        alertDialogBuilder.setMessage("Вы уверены, что хотите удалить это?");

        alertDialogBuilder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.removeById(item.getId());
                dialog.dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @SuppressLint("CheckResult")
    public void syncClick() {
        if (userIP != "noIP") {
            viewModel.sync();
        }
        else{
            Toast.makeText(this,"Enter IP",Toast.LENGTH_SHORT).show();
        }
    }

}