package com.nyoobie.petugaslintasbandung.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nyoobie.petugaslintasbandung.R;
import com.nyoobie.petugaslintasbandung.models.CheckUser;

import java.util.List;

public class SemuaDataAdapter extends RecyclerView.Adapter<SemuaDataAdapter.ViewHolder> {

    private Context context;
    private List<CheckUser> checkUserList;

    public SemuaDataAdapter(Context context, List<CheckUser> checkUserList) {
        this.context = context;
        this.checkUserList = checkUserList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_petugas, parent, false);
        ViewHolder listFiveData = new ViewHolder(view);
        return listFiveData;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.nama.setText(checkUserList.get(position).getCustomer().getFirstName() + " " + checkUserList.get(position).getCustomer().getLastName());
        holder.rute.setText(checkUserList.get(position).getRute().getNamaTrayek());
        holder.dari.setText(checkUserList.get(position).getKeberangkatan());
        holder.tujuan.setText(checkUserList.get(position).getTujuan());
    }

    @Override
    public int getItemCount() {
        return checkUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nama, rute, dari, tujuan;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.listPetugas_nama);
            rute = itemView.findViewById(R.id.listPetugas_rute);
            dari = itemView.findViewById(R.id.listPetugas_fromTrayek);
            tujuan = itemView.findViewById(R.id.listPetugas_toTrayek);
        }
    }
}
