package com.nyoobie.petugaslintasbandung.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nyoobie.petugaslintasbandung.R;
import com.nyoobie.petugaslintasbandung.adapter.DashboardAdapter;
import com.nyoobie.petugaslintasbandung.adapter.SemuaDataAdapter;
import com.nyoobie.petugaslintasbandung.data.AppState;
import com.nyoobie.petugaslintasbandung.models.CheckUser;
import com.nyoobie.petugaslintasbandung.network.ApiService;
import com.nyoobie.petugaslintasbandung.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SemuaDataActivity extends AppCompatActivity {

    private LinearLayout ifNull;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<CheckUser> checkUserList;
    private ApiService apiService;
    private AppState appState;
    private Toolbar toolbar;
    private TextView judul;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semua_data);
        swipeRefreshLayout = findViewById(R.id.semua_refresh);

        ifNull = findViewById(R.id.semua_ifNull);

        toolbar = findViewById(R.id.semua_toolbar);
        judul = toolbar.findViewById(R.id.semua_judul);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.semua_recycler);

        apiService = ApiUtils.getApiSerives();
        appState = AppState.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        final String id_user = appState.getUser().getId();
        getData(id_user);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(id_user);
                Log.d("Coba", "Refresh");
            }
        });
    }

    private void getData(String id_user) {
        Call<List<CheckUser>> listCall = apiService.getData(id_user);
        listCall.enqueue(new Callback<List<CheckUser>>() {
            @Override
            public void onResponse(Call<List<CheckUser>> call, Response<List<CheckUser>> response) {
                if (response.isSuccessful()) {
                    Log.d("Coba", "Selesai");
                    swipeRefreshLayout.setRefreshing(false);
                    ifNull.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    checkUserList = response.body();
                    adapter = new SemuaDataAdapter(getApplicationContext(), checkUserList);
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

    private void showToast(String message) {
        Toast.makeText(SemuaDataActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
