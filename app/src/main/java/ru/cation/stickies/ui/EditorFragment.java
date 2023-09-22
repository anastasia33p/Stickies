package ru.cation.stickies.ui;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ru.cation.stickies.R;
import ru.cation.stickies.databinding.ActivityMainBinding;
import ru.cation.stickies.databinding.EditorBinding;
import ru.cation.stickies.models.StickiesItem;
import ru.cation.stickies.viewmodel.EditorFragmentViewModel;

public class EditorFragment extends BottomSheetDialogFragment {

    private EditorFragmentViewModel viewModel;
    private EditorBinding binding;


    public EditorFragment(Application application) {
        viewModel=new EditorFragmentViewModel(application);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = EditorBinding.inflate(inflater);
        initClicks();
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setupFullHeight(bottomSheetDialog);
        });
        return dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        LinearLayout bottomSheet = (LinearLayout) bottomSheetDialog.findViewById(R.id.root);

        BottomSheetBehavior behavior = bottomSheetDialog.getBehavior();
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setPeekHeight(windowHeight);
    }

    private int getWindowHeight() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void initClicks() {
        binding.saveButton.setOnClickListener(view -> {
            String input=binding.editText.getText().toString();
            viewModel.addSticker(input);
            dismiss();
        });
    }
}
