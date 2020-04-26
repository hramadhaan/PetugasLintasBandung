package com.nyoobie.petugaslintasbandung.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckUser {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("rute")
    @Expose
    private Rute rute;
    @SerializedName("jumlah_tiket")
    @Expose
    private String jumlahTiket;
    @SerializedName("harga")
    @Expose
    private String harga;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("keberangkatan")
    @Expose
    private String keberangkatan;
    @SerializedName("tujuan")
    @Expose
    private String tujuan;

    public CheckUser(String id, Rute rute, String jumlahTiket, String harga, Customer customer, String keberangkatan, String tujuan) {
        this.id = id;
        this.rute = rute;
        this.jumlahTiket = jumlahTiket;
        this.harga = harga;
        this.customer = customer;
        this.keberangkatan = keberangkatan;
        this.tujuan = tujuan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Rute getRute() {
        return rute;
    }

    public void setRute(Rute rute) {
        this.rute = rute;
    }

    public String getJumlahTiket() {
        return jumlahTiket;
    }

    public void setJumlahTiket(String jumlahTiket) {
        this.jumlahTiket = jumlahTiket;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getKeberangkatan() {
        return keberangkatan;
    }

    public void setKeberangkatan(String keberangkatan) {
        this.keberangkatan = keberangkatan;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }
}
