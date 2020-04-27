package com.nyoobie.petugaslintasbandung.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nyoobie.petugaslintasbandung.R;
import com.nyoobie.petugaslintasbandung.adapter.DashboardAdapter;
import com.nyoobie.petugaslintasbandung.data.AppState;
import com.nyoobie.petugaslintasbandung.models.CheckUser;
import com.nyoobie.petugaslintasbandung.models.Ringkasan;
import com.nyoobie.petugaslintasbandung.network.ApiService;
import com.nyoobie.petugaslintasbandung.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private AppState appState;
    private ApiService apiService;
    private RelativeLayout checkTicket;
    private Toolbar toolbar;
    private TextView judul;
    private TextView nama, role, jumlahPenumpang, pendapatan, seeAll;
    private Button click;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<CheckUser> checkUserList;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        appState = AppState.getInstance();
        apiService = ApiUtils.getApiSerives();

        recyclerView = findViewById(R.id.dashboard_recyclerView);
        progressBar = findViewById(R.id.dashboard_progressRecycler);

        checkTicket = findViewById(R.id.dashboard_checkTicket);
        if (appState.getUser().getRole().equals("angkot")) {
            checkTicket.setVisibility(View.GONE);
        }
        checkTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBarcode = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(toBarcode);
            }
        });
        click = findViewById(R.id.dashboard_clickDisini);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBarcode = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(toBarcode);
            }
        });

        toolbar = findViewById(R.id.dashboard_toolbar);
        judul = toolbar.findViewById(R.id.dashboard_judul);
        setSupportActionBar(toolbar);


        nama = findViewById(R.id.dashboard_nama);
        nama.setText(appState.getUser().getFirstName() + " " + appState.getUser().getLastName() + ",");
        role = findViewById(R.id.dashboard_role);
        role.setText(appState.getUser().getRole());
        jumlahPenumpang = findViewById(R.id.dashboard_jumlahPenumpang);
        pendapatan = findViewById(R.id.dashboard_pendapatan);
        seeAll = findViewById(R.id.dashboard_seeAll);
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, SemuaDataActivity.class));
            }
        });

        hasilRingkasan(appState.getUser().getId());


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.color));
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        final String id_user = appState.getUser().getId();

        getData(id_user);

        swipeRefreshLayout = findViewById(R.id.dashboard_refresh);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(id_user);
                hasilRingkasan(id_user);
            }
        });
    }

    private void getData(String id_user) {
        Call<List<CheckUser>> listCall = apiService.getData(id_user);
        listCall.enqueue(new Callback<List<CheckUser>>() {
            @Override
            public void onResponse(Call<List<CheckUser>> call, Response<List<CheckUser>> response) {
                if (response.isSuccessful()) {
                    swipeRefreshLayout.setRefreshing(false);
                    checkUserList = response.body();
                    adapter = new DashboardAdapter(getApplicationContext(), checkUserList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (adapter.getItemCount() == 0) {
                        showToast("Belum Ada Transaksi");
                    }
                } else {
                    showToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<CheckUser>> call, Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    private void hasilRingkasan(String id) {
        Call<Ringkasan> ringkasanCall = apiService.getRingkasan(id);
        ringkasanCall.enqueue(new Callback<Ringkasan>() {
            @Override
            public void onResponse(Call<Ringkasan> call, Response<Ringkasan> response) {
                if (response.isSuccessful()) {
                    swipeRefreshLayout.setRefreshing(false);
                    jumlahPenumpang.setText(response.body().getPenumpang());
                    pendapatan.setText(response.body().getIncome());
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    showToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<Ringkasan> call, Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.moreVertical:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showToast(String message) {
        Toast.makeText(DashboardActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private void logout() {
        if (appState.isLoggedIn()) {
            appState.logout();
            appState.removeCurrentUser();
            startActivity(new Intent(DashboardActivity.this, PilihLoginActivity.class));
            finish();
        }
    }
}
