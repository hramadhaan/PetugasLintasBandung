package com.nyoobie.petugaslintasbandung.network;

import com.nyoobie.petugaslintasbandung.models.CheckUser;
import com.nyoobie.petugaslintasbandung.models.DataUser;
import com.nyoobie.petugaslintasbandung.models.Ringkasan;
import com.nyoobie.petugaslintasbandung.models.Status;
import com.nyoobie.petugaslintasbandung.models.Token;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("checkOrder/{id_order}/{id_user}")
    Call<Status> changeStatus(
            @Path("id_order") String id_order,
            @Path("id_user") String id_user
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

    @GET("showSpecificOrder/{id_order}")
    Call<CheckUser> getCheckUser(
            @Path("id_order") String id_order
    );

    @GET("checkedByUser/{id_user}")
    Call<List<CheckUser>> getData(
            @Path("id_user") String id_user
    );
}
