package com.nyoobie.petugaslintasbandung.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Check {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<CheckUser> data;

    public Check(String status, List<CheckUser> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CheckUser> getData() {
        return data;
    }

    public void setData(List<CheckUser> data) {
        this.data = data;
    }
}
