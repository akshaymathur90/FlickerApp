package com.akshay.com.flickrapp.fragments;


import android.content.BroadcastReceiver;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akshay.com.flickrapp.R;
import com.akshay.com.flickrapp.adapters.PhotosRecyclerViewAdapter;
import com.akshay.com.flickrapp.database.FlickrDatabase;
import com.akshay.com.flickrapp.database.FlickrDatabaseHelper;
import com.akshay.com.flickrapp.databinding.FragmentPhotoGridBinding;
import com.akshay.com.flickrapp.fragments.abs.VisibleFragment;
import com.akshay.com.flickrapp.models.FlickrResponse;
import com.akshay.com.flickrapp.models.Photo;
import com.akshay.com.flickrapp.network.FlickrClientConsumer;
import com.akshay.com.flickrapp.receivers.InternetCheckReceiver;
import com.akshay.com.flickrapp.utils.AppUtils;
import com.akshay.com.flickrapp.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoGridFragment extends VisibleFragment {

    public static final String TAG = "PhotoGridFragment";
    private FragmentPhotoGridBinding mFragmentPhotoGridBinding;
    private StaggeredGridLayoutManager mLayoutManager;
    private List<Photo> mPhotoList;
    private PhotosRecyclerViewAdapter mAdapter;
    private EndlessRecyclerViewScrollListener mScrollListener;

    private int totalPages=1;
    private int currentPage = 1;
    private final int TOTAL_RETRY_COUNT = 5;
    private int retryRemaining = TOTAL_RETRY_COUNT;
    private boolean hasInternet = true;

    public PhotoGridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentPhotoGridBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_photo_grid,container,false);
        mPhotoList = new ArrayList<>();
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new PhotosRecyclerViewAdapter(getActivity(),mPhotoList);
        return mFragmentPhotoGridBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFragmentPhotoGridBinding.rvPhotoGridView.setLayoutManager(mLayoutManager);
        mFragmentPhotoGridBinding.rvPhotoGridView.setAdapter(mAdapter);
        mScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(TAG, "on Load More Called");
                if(currentPage<totalPages && hasInternet){
                    mFragmentPhotoGridBinding.btnLoadMore.setVisibility(View.VISIBLE);
                }else if(!hasInternet){
                    showInternetUnavailableSnackbar();
                }

            }
        };
        mFragmentPhotoGridBinding.rvPhotoGridView.addOnScrollListener(mScrollListener);
        mFragmentPhotoGridBinding.swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mFragmentPhotoGridBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(hasInternet){
                    mPhotoList.clear();
                    mAdapter.notifyDataSetChanged();
                    mScrollListener.resetState();
                    currentPage=1;
                    fetchPhotos(currentPage);
                }else{
                    showInternetUnavailableSnackbar();
                }

            }
        });
        mFragmentPhotoGridBinding.btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Load more button clicked");
                mFragmentPhotoGridBinding.btnLoadMore.setVisibility(View.GONE);
                currentPage++;
                fetchPhotos(currentPage);
            }
        });
        hasInternet = AppUtils.isInternetAvailable(getActivity());
        if(hasInternet){
            FlickrDatabaseHelper.deleteAllPhotos();
            fetchPhotos(currentPage);
        }else{
            showInternetUnavailableSnackbar();
            Log.d(TAG,"Internet Unavailable fetching data from db...");
            List<Photo> photos = FlickrDatabaseHelper.getAllPhotos();
            Log.d(TAG,"Fetched " + photos.size() + " photos from db...");
            mAdapter.addMorePhotos(photos);
        }

    }

    private void fetchPhotos(final int page){
        Log.d(TAG,"Loading Page = "+ page);
        mFragmentPhotoGridBinding.swipeRefreshLayout.setRefreshing(true);
        FlickrClientConsumer.getRecentPhotos(page).enqueue(new Callback<FlickrResponse>() {
            @Override
            public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                Log.d(TAG,"Response Code = "+ response.code());
                Log.d(TAG,"Response Status = "+ response.body().getStat());
                Log.d(TAG,"Total Pages = "+ response.body().getPhotos().getPages());
                Log.d(TAG,"Returned Page # = "+ response.body().getPhotos().getPage());
                mFragmentPhotoGridBinding.swipeRefreshLayout.setRefreshing(false);
                totalPages = response.body().getPhotos().getPages();
                List<Photo> photos = response.body().getPhotos().getPhoto();
                mAdapter.addMorePhotos(photos);
                FlickrDatabaseHelper.saveAllPhotos(photos);
                retryRemaining = TOTAL_RETRY_COUNT;
            }

            @Override
            public void onFailure(Call<FlickrResponse> call, Throwable t) {
                mFragmentPhotoGridBinding.swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG,t.getMessage());
                Log.d(TAG,"Retrying request for page--> "+page);
                Log.d(TAG,"Retrying count--> "+retryRemaining);
                if(retryRemaining>0) {
                    retryRemaining--;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fetchPhotos(page);

                        }
                    }, 5000);
                }
            }
        });
    }

    @Override
    public BroadcastReceiver createInternetCheckReceiver() {
        InternetCheckReceiver internetCheckReceiver = new InternetCheckReceiver();
        internetCheckReceiver.setListener(new InternetCheckReceiver.ConnectivityChangeListener() {
            @Override
            public void onNetworkChange(boolean isConnected) {
                if(hasInternet==isConnected){
                    return;
                }
                if(isConnected){
                    hasInternet = true;
                    Snackbar.make(mFragmentPhotoGridBinding.getRoot(),
                            getActivity().getString(R.string.snackbar_text_internet_available),
                            Snackbar.LENGTH_LONG).show();

                }
                else{
                    hasInternet = false;
                    showInternetUnavailableSnackbar();
                }
            }
        });
        return internetCheckReceiver;
    }

    private void showInternetUnavailableSnackbar(){
        Snackbar.make(mFragmentPhotoGridBinding.getRoot(),
                getActivity().getString(R.string.snackbar_text_internet_lost),
                Snackbar.LENGTH_LONG).show();
    }
}
