package com.nyoobie.petugaslintasbandung.network;

import com.nyoobie.petugaslintasbandung.models.Status;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("edit/{id_order}/{status}")
    Call<Status> changeStatus(
            @Path("id_order") String id_order,
            @Path("status") String status
    );
}
