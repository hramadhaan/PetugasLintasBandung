package com.nyoobie.petugaslintasbandung.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.nyoobie.petugaslintasbandung.R;
import com.nyoobie.petugaslintasbandung.data.AppState;

public class PilihLoginActivity extends AppCompatActivity {

    private Button buttonAngkot, buttonDamri;
    private AppState appState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_login);

        appState = AppState.getInstance();
        if (AppState.getInstance().isLoggedIn()) {
            startActivity(new Intent(PilihLoginActivity.this, DashboardActivity.class));
            finish();
        }

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.color));
        }

        buttonAngkot = findViewById(R.id.pilih_angkot);
        buttonDamri = findViewById(R.id.pilih_damri);

        buttonAngkot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent angkot = new Intent(PilihLoginActivity.this, LoginActivity.class);
                angkot.putExtra("jenis", "angkot");
                startActivity(angkot);
            }
        });

        buttonDamri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent damri = new Intent(PilihLoginActivity.this, LoginActivity.class);
                damri.putExtra("jenis", "damri");
                startActivity(damri);
            }
        });
    }
}
