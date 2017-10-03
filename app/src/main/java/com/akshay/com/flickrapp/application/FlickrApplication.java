package com.akshay.com.flickrapp.application;

import android.app.Application;
import android.content.Context;

import com.akshay.com.flickrapp.network.FlickrClient;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import retrofit2.Retrofit;

/**
 * Created by akshaymathur on 10/2/17.
 */

public class FlickrApplication extends Application {

    private static Retrofit sRetrofit;
    private static Context mAppContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        sRetrofit = new FlickrClient().getRetrofit();

        //Set up db flow
        FlowManager.init(new FlowConfig.Builder(this).build());

    }

    public static Retrofit getRetrofit(){
        return sRetrofit;
    }

    public static Context getAppContext(){
        return mAppContext;
    }
}
