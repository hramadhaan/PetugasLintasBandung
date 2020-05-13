package com.nyoobie.petugaslintasbandung.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nyoobie.petugaslintasbandung.R;
import com.nyoobie.petugaslintasbandung.data.AppState;
import com.nyoobie.petugaslintasbandung.models.DataUser;
import com.nyoobie.petugaslintasbandung.models.Token;
import com.nyoobie.petugaslintasbandung.network.ApiService;
import com.nyoobie.petugaslintasbandung.utils.ApiUtils;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login, hubungi_admin;
    private ApiService apiService;
    private AppState appState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiService = ApiUtils.getApiSerives();
        appState = AppState.getInstance();
        appState = AppState.getInstance();
        if (AppState.getInstance().isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.color));
        }

        email = findViewById(R.id.login_email);
        email.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(email, InputMethodManager.SHOW_IMPLICIT);
        password = findViewById(R.id.login_password);

        login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(email.getText().toString(), password.getText().toString());
            }
        });
        hubungi_admin = findViewById(R.id.login_hubungiAdmin);
        hubungi_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "+6282216402588";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
    }

    private void login(String email, String password) {

        login.setEnabled(false);
        if (email.matches("") && password.matches("")) {
            showToastInfo("Masukkan E-Mail dan Password Anda");
            login.setEnabled(true);
        } else if (email.matches("")) {
            showToastInfo("Masukkan E-Mail Anda");
            login.setEnabled(true);
        } else if (password.matches("")) {
            showToastInfo("Masukkan Password Anda");
            login.setEnabled(true);
        } else {
            closeKeyboard();

            Call<Token> tokenCall = apiService.getToken(email, password);
            tokenCall.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    if (response.isSuccessful()) {
                        String hasilToken = response.body().getToken();
                        if (hasilToken.equals("false")) {
                            showToastInfo("E-Mail atau Password belum terdaftar, silahkan hubungi Admin");
                            login.setEnabled(true);
                        } else {
                            appState.setToken(hasilToken);
                            appState.setIsLoggedIn(true);
                            saveUser();
                            login.setEnabled(true);
                            showToastSuccess("Login berhasil");
                        }
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    showToastError(t.getMessage());
                }
            });
        }
    }

    private void saveUser() {
        apiService.saveUser().enqueue(new Callback<DataUser>() {
            @Override
            public void onResponse(Call<DataUser> call, Response<DataUser> response) {
                if (response.isSuccessful()) {
                    appState.saveUser(response.body());
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showToastInfo("Gagal Membuat Session");
                }
            }

            @Override
            public void onFailure(Call<DataUser> call, Throwable t) {
                showToastError(t.getMessage());
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showToastInfo(String message) {
        Toasty.warning(LoginActivity.this, message, Toast.LENGTH_SHORT, true).show();
    }

    private void showToastError(String message) {
        Toasty.error(LoginActivity.this, message, Toast.LENGTH_SHORT, true).show();
    }

    private void showToastSuccess(String message) {
        Toasty.success(LoginActivity.this, message, Toast.LENGTH_SHORT, true).show();
    }
}
