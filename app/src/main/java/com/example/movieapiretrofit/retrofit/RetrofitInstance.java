package com.example.movieapiretrofit.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    public static Retrofit retrofit;

    public static Retrofit getUrl(){

        retrofit = new Retrofit.Builder().
                baseUrl(Constant.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        return retrofit;
    }
}
