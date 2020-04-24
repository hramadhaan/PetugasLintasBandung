package com.nyoobie.petugaslintasbandung.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ringkasan {
    @SerializedName("penumpang")
    @Expose
    private String penumpang;
    @SerializedName("Income")
    @Expose
    private String income;

    public Ringkasan(String penumpang, String income) {
        this.penumpang = penumpang;
        this.income = income;
    }

    public String getPenumpang() {
        return penumpang;
    }

    public void setPenumpang(String penumpang) {
        this.penumpang = penumpang;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }
}
