package com.nyoobie.petugaslintasbandung.utils;

import com.nyoobie.petugaslintasbandung.network.ApiService;
import com.nyoobie.petugaslintasbandung.services.RetrofitClient;

public class ApiUtils {
    private ApiUtils() {
    }

    public static final String API_URL = "https://lintasbandung.nyoobie.com/API/";

    public static ApiService getApiSerives() {
        return RetrofitClient.getClient(API_URL).create(ApiService.class);
    }
}
