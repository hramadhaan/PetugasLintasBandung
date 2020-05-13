package com.nyoobie.petugaslintasbandung.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nyoobie.petugaslintasbandung.R;
import com.nyoobie.petugaslintasbandung.data.AppState;
import com.nyoobie.petugaslintasbandung.models.CheckUser;
import com.nyoobie.petugaslintasbandung.models.Status;
import com.nyoobie.petugaslintasbandung.network.ApiService;
import com.nyoobie.petugaslintasbandung.utils.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckTicketActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private LinearLayout showData, showNull;
    private Toolbar toolbar;
    private AppState appState;
    private Button button;
    private String id_order;

    private TextView rute, dari, tujuan, namaPemesan, jumlahPesanan, totalHarga;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ticket);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.color));
        }

        //        NETWORK
        apiService = ApiUtils.getApiSerives();
        appState = AppState.getInstance();

        final String id_user = appState.getUser().getId();

//        LINEARLAYOUT
        showData = findViewById(R.id.main_notNull);
        showNull = findViewById(R.id.main_ifNull);

//        TEXTVIEW
        rute = findViewById(R.id.main_rute);
        dari = findViewById(R.id.main_fromTrayek);
        tujuan = findViewById(R.id.main_toTrayek);
        namaPemesan = findViewById(R.id.main_namaPemesan);
        jumlahPesanan = findViewById(R.id.main_jumlahPesanan);
        totalHarga = findViewById(R.id.main_totalHarga);

//        BUTTON
        button = findViewById(R.id.main_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(id_order, id_user);
            }
        });

//      TOOLBAR
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


//FLOATINGACTIONBUTTON
        floatingActionButton = findViewById(R.id.main_camera);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(CheckTicketActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setPrompt("Letakkan secara horizontal dan pastikan barcode sejajar dengan garis merah");
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                showToastInfo("Data Tidak Ada");
            } else {
                showData.setVisibility(View.VISIBLE);
                showNull.setVisibility(View.GONE);
                id_order = intentResult.getContents();
                Date date = new Date();
                final String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                showUser(id_order, modifiedDate);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showToastInfo(String message) {
        Toasty.warning(CheckTicketActivity.this, message, Toast.LENGTH_SHORT, true).show();
    }

    private void showToastError(String message) {
        Toasty.error(CheckTicketActivity.this, message, Toast.LENGTH_SHORT, true).show();
    }

    private void showToastSuccess(String message) {
        Toasty.success(CheckTicketActivity.this, message, Toast.LENGTH_SHORT, true).show();
    }

    private void showUser(String id_order, final String date) {
        Call<CheckUser> checkUserCall = apiService.getCheckUser(id_order);
        checkUserCall.enqueue(new Callback<CheckUser>() {
            @Override
            public void onResponse(Call<CheckUser> call, Response<CheckUser> response) {
                if (response.isSuccessful()) {
                    if (response.body().getTanggal_pemesanan().equals(date)) {
                        rute.setText(response.body().getRute().getNamaTrayek());
                        dari.setText(response.body().getKeberangkatan());
                        tujuan.setText(response.body().getTujuan());
                        namaPemesan.setText(response.body().getCustomer().getFirstName() + " " + response.body().getCustomer().getLastName());
                        jumlahPesanan.setText(response.body().getJumlahTiket() + " Orang");
                        totalHarga.setText("Rp. " + response.body().getHarga());
                    } else {
                        rute.setText(response.body().getRute().getNamaTrayek());
                        dari.setText(response.body().getKeberangkatan());
                        tujuan.setText(response.body().getTujuan());
                        namaPemesan.setText(response.body().getCustomer().getFirstName() + " " + response.body().getCustomer().getLastName());
                        jumlahPesanan.setText(response.body().getJumlahTiket() + " Orang");
                        totalHarga.setText("Rp. " + response.body().getHarga());
                        button.setEnabled(false);
                        button.setText("Tiket Tidak Valid");
                        showToastInfo("Keberangkatan pada tiket bukan pada hari ini");
                    }
                } else {
                    showToastInfo(response.message());
                }
            }

            @Override
            public void onFailure(Call<CheckUser> call, Throwable t) {
                showToastError(t.getMessage());
            }
        });
    }

    private void send(String id_order, String id_user) {
        Call<Status> statusCall = apiService.changeStatus(id_order, id_user);
        statusCall.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        showToastSuccess("Check Berhasil");
                        finish();
                    } else {
                        showToastInfo("Coba Ulangi Lagi");
                    }
                } else {
                    showToastInfo(response.message());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                showToastError(t.getMessage());
            }
        });
    }
}
