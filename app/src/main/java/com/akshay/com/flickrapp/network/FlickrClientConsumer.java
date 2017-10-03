package com.akshay.com.flickrapp.network;

import android.util.Log;

import com.akshay.com.flickrapp.application.FlickrApplication;
import com.akshay.com.flickrapp.models.FlickrResponse;
import com.akshay.com.flickrapp.models.Photo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by akshaymathur on 10/2/17.
 */

public class FlickrClientConsumer {

    private static final String TAG = "FlickrClientConsumer";
    private static final String METHOD_PARAM = "method";
    private static final String METHOD = "flickr.photos.getRecent";
    private static final String PAGE_PARAM = "page";


    public static Call<FlickrResponse> getRecentPhotos(int page){

        Retrofit retrofit = FlickrApplication.getRetrofit();

        FlickrEndPointInterface apiService = retrofit.create(FlickrEndPointInterface.class);
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put(METHOD_PARAM,METHOD);
        queryParams.put(PAGE_PARAM,String.valueOf(page));
        Call<FlickrResponse> responseCall = apiService.getPhotos(queryParams);
        return responseCall;
    }
}
