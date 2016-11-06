package com.audreytroutt.androidbeginners.firstapp;

import android.app.Application;
import android.widget.Toast;

public class MyFirstApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // You can delete this Toast if you want :)
        Toast.makeText(this, "MyFirstApplication has been created!", Toast.LENGTH_LONG).show();
    }
}
