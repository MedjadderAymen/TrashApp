package com.medjay.trashapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MyStetho extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
