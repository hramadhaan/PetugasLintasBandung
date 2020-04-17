package com.nyoobie.petugaslintasbandung;

import android.app.Application;

import com.nyoobie.petugaslintasbandung.data.AppState;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppState.getInstance().initSharedPrefs(this);
    }
}
