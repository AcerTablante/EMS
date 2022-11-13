package com.example.emg.utils;


import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.example.emg.R;

public class LoadingDialog {
    Activity activity;
    AlertDialog dialog;
    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_loading_dialog,null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog = builder.show();

    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
