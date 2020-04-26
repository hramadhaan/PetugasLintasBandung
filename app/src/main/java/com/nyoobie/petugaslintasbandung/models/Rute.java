package com.nyoobie.petugaslintasbandung.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rute {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama_trayek")
    @Expose
    private String namaTrayek;

    public Rute(String id, String namaTrayek) {
        this.id = id;
        this.namaTrayek = namaTrayek;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaTrayek() {
        return namaTrayek;
    }

    public void setNamaTrayek(String namaTrayek) {
        this.namaTrayek = namaTrayek;
    }
}
