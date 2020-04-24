package com.nyoobie.petugaslintasbandung;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.nyoobie.petugaslintasbandung.activity.LoginActivity;
import com.nyoobie.petugaslintasbandung.activity.MainActivity;
import com.nyoobie.petugaslintasbandung.activity.PilihLoginActivity;

public class SplashScreen extends AppCompatActivity {

    private long ms = 0, splashTime = 1200;
    private boolean splashActive = true, paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        final RelativeLayout relativeLayout = findViewById(R.id.splash_main);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (splashActive && ms < splashTime) {
                        if (!paused) {
                            ms = ms + 100;
                            sleep(100);
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                } finally {
                    if (!isOnline()) {
                        Snackbar snackbar = Snackbar.make(relativeLayout, "No Internet Connection", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recreate();
                            }
                        });
                        snackbar.show();
                    } else {
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        };
        thread.start();
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
