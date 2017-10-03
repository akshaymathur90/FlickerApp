package com.akshay.com.flickrapp.network;

import com.akshay.com.flickrapp.models.FlickrResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by akshaymathur on 10/2/17.
 */

public interface FlickrEndPointInterface {

    @GET("rest/")
    Call<FlickrResponse> getPhotos(@QueryMap Map<String,String> queryParams);

}
