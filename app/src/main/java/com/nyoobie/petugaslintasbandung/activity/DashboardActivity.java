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
import android.widget.LinearLayout;
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
import com.nyoobie.petugaslintasbandung.models.Check;
import com.nyoobie.petugaslintasbandung.models.CheckUser;
import com.nyoobie.petugaslintasbandung.models.Ringkasan;
import com.nyoobie.petugaslintasbandung.network.ApiService;
import com.nyoobie.petugaslintasbandung.utils.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
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
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        appState = AppState.getInstance();
        apiService = ApiUtils.getApiSerives();

        recyclerView = findViewById(R.id.dashboard_recyclerView);
        linearLayout = findViewById(R.id.dashboard_ifNull);

        checkTicket = findViewById(R.id.dashboard_checkTicket);
        if (appState.getUser().getRole().equals("angkot")) {
            checkTicket.setVisibility(View.GONE);
        }
        checkTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBarcode = new Intent(DashboardActivity.this, CheckTicketActivity.class);
                startActivity(toBarcode);
            }
        });
        click = findViewById(R.id.dashboard_clickDisini);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBarcode = new Intent(DashboardActivity.this, CheckTicketActivity.class);
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

        Date date = new Date();
        final String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        getData(id_user, modifiedDate);

        swipeRefreshLayout = findViewById(R.id.dashboard_refresh);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(id_user, modifiedDate);
                hasilRingkasan(id_user);
            }
        });
    }

    private void getData(String id_user, String date) {
        Call<Check> getData = apiService.getDataByDate(id_user, date);
        getData.enqueue(new Callback<Check>() {
            @Override
            public void onResponse(Call<Check> call, Response<Check> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("berhasil")) {
                        swipeRefreshLayout.setRefreshing(false);
                        recyclerView.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
                        checkUserList = response.body().getData();
                        adapter = new DashboardAdapter(DashboardActivity.this, checkUserList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else if (response.body().getStatus().equals("not found")) {
                        showToastInfo("Data Tidak Ada");
                        swipeRefreshLayout.setRefreshing(false);
                        recyclerView.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    } else {
//                        showToastInfo("Kesalahan pada status");
                    }
                } else {
                    showToastInfo(response.message());
                }
            }

            @Override
            public void onFailure(Call<Check> call, Throwable t) {
                showToastError(t.getMessage());
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
                } else {
                    showToastInfo(response.message());
                }
            }

            @Override
            public void onFailure(Call<Ringkasan> call, Throwable t) {
                showToastError(t.getMessage());
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

    private void showToastInfo(String message) {
        Toasty.warning(DashboardActivity.this, message, Toast.LENGTH_SHORT, true).show();
    }

    private void showToastSuccess(String message) {
        Toasty.success(DashboardActivity.this, message, Toast.LENGTH_SHORT, true).show();
    }

    private void showToastError(String message) {
        Toasty.error(DashboardActivity.this, message, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private void logout() {
        if (appState.isLoggedIn()) {
            showToastSuccess("Logout Anda Berhasil");
            appState.logout();
            appState.removeCurrentUser();
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }
    }
}
