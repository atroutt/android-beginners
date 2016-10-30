package com.audreytroutt.androidbeginners.firstapp;

import android.app.Application;
import android.widget.Toast;

public class MyFirstApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // My Application was just created!
        showToast("MyFirstApplication has been created!");
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
