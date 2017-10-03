package com.akshay.com.flickrapp.network;

import com.akshay.com.flickrapp.application.FlickrApplication;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by akshaymathur on 10/2/17.
 */

public class FlickrClient {

    private final String API_KEY = "7b85e389607020e3b5a12c5a40e260db";
    private final String API_KEY_PARAM = "api_key";
    private final String FORMAT = "json";
    private final String FORMAT_PARAM = "format";
    private final String NO_JSON_CALLBACK = "1";
    private final String NO_JSON_CALLBACK_PARAM = "nojsoncallback";
    private final String BASE_URL = "https://api.flickr.com/services/";
    private Retrofit mRetrofit;

    public FlickrClient(){

        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(FlickrApplication.getAppContext().getCacheDir(), cacheSize);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                HttpUrl originalUrl = originalRequest.url();
                HttpUrl url = originalUrl.newBuilder()
                        .addQueryParameter(API_KEY_PARAM, API_KEY)
                        .addQueryParameter(FORMAT_PARAM,FORMAT)
                        .addQueryParameter(NO_JSON_CALLBACK_PARAM,NO_JSON_CALLBACK)
                        .build();

                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });


        httpClient.cache(cache);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callFactory(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit(){
        return mRetrofit;
    }


}
