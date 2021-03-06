package com.tinuade.africaknow.Api;

import com.tinuade.africaknow.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://quizzies.herokuapp.com/";
    private static ApiInterface service;

    public static ApiInterface getService() {

        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(ApiInterface.class);
        }
        return service;
    }

    private static OkHttpClient okHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        //add a logging interceptor to be able to log network request response
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }

        okHttpClient.addInterceptor(httpLoggingInterceptor);
        return okHttpClient.build();
    }
}
