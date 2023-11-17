package com.metanoiasystem.go4lunchxoc.data.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // Singleton instance of Retrofit.
    private static Retrofit retrofit;

    // Provides a single instance of Retrofit. If it doesn't exist, it's created with specified configurations.
    public static Retrofit getRetrofit() {
        if (retrofit != null) {
            return retrofit;
        }

        // Interceptor for logging network requests and responses.
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // OkHttpClient with logging interceptor.
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        // Retrofit builder with base URL, Gson converter, RxJava2 adapter, and OkHttpClient.
        retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;
    }
}

