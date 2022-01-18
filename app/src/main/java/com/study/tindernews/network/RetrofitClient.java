package com.study.tindernews.network;

import android.content.Context;

import com.ashokvarma.gander.GanderInterceptor;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.study.tindernews.Credentials;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String API_KEY = Credentials.API_KEY;
    private static final String BASE_URL = "https://newsapi.org/v2/";

    public static Retrofit newInstance(Context context) {
        // Write the okHttpClient explicitly to add API_KEY into my header
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new GanderInterceptor(context).showNotification(true)) // add the Gander Interceptor in RetrofitClient
                .addInterceptor(new HeaderInterceptor()) // add my API_KEY in header.
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private static class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original
                    .newBuilder()
                    .header("X-Api-Key", API_KEY)
                    .build();
            return chain.proceed(request);
        }
    }
}
