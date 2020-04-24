package com.nyoobie.petugaslintasbandung.network;

import com.nyoobie.petugaslintasbandung.models.DataUser;
import com.nyoobie.petugaslintasbandung.models.Ringkasan;
import com.nyoobie.petugaslintasbandung.models.Status;
import com.nyoobie.petugaslintasbandung.models.Token;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("edit/{id_order}/{status}")
    Call<Status> changeStatus(
            @Path("id_order") String id_order,
            @Path("status") String status
    );

    @FormUrlEncoded
    @POST("loginAPI")
    Call<Token> getToken(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("decode")
    Call<DataUser> saveUser();

    @GET("todayOrder/{id}")
    Call<Ringkasan> getRingkasan(
            @Path("id") String id
    );
}
