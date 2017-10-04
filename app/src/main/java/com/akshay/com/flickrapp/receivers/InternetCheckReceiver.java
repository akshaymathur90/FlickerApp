package com.akshay.com.flickrapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.akshay.com.flickrapp.R;
import com.akshay.com.flickrapp.utils.AppUtils;


/**
 * Created by akshaymathur
 */

public class InternetCheckReceiver extends BroadcastReceiver{

    private final String TAG = "InternetCheckReceiver";
    private boolean internet = true;
    public InternetCheckReceiver(){}
    private ConnectivityChangeListener mListener;
    public interface ConnectivityChangeListener{
        void onNetworkChange(boolean isConnected);
    }

    public void setListener(ConnectivityChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"caught the change");

        boolean isConnected = AppUtils.isInternetAvailable(context);
        mListener.onNetworkChange(isConnected);

    }

}
