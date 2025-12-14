/**
 * Author: WANGQINGBIN
 * A20478885
 * qwang93@hawk.illinoistech.edu
 * ITMD 555
 */
package com.example.coinview.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.coingecko.com/api/v3/";

    private static Retrofit retrofit = null;
    private static CoinGeckoApiService apiService = null;

    /**
     * Get Retrofit instance (singleton)
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    // Create logging interceptor
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    // Create OkHttpClient
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(loggingInterceptor)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .build();

                    // Create Retrofit instance
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    /**
     * Get CoinGecko API service (singleton)
     */
    public static CoinGeckoApiService getApiService() {  // ✅ 添加 static
        if (apiService == null) {
            synchronized (RetrofitClient.class) {
                if (apiService == null) {
                    apiService = getClient().create(CoinGeckoApiService.class);
                }
            }
        }
        return apiService;
    }

    /**
     * Get CoinGecko API service (alias)
     */
    public static CoinGeckoApiService getCoinGeckoApiService() {  // ✅ 添加 static
        return getApiService();
    }

    /**
     * Reset client (for testing purposes)
     */
    public static void resetClient() {
        retrofit = null;
        apiService = null;
    }
}
