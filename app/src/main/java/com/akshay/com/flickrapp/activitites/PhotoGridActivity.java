package com.akshay.com.flickrapp.activitites;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.akshay.com.flickrapp.databinding.ActivityPhotoGridBinding;
import com.akshay.com.flickrapp.fragments.PhotoGridFragment;
import com.akshay.com.flickrapp.R;

public class PhotoGridActivity extends AppCompatActivity {

    private ActivityPhotoGridBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_photo_grid);
        setSupportActionBar(mBinding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(R.string.app_name);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment photoGridFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if(photoGridFragment==null){
            photoGridFragment = new PhotoGridFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container,photoGridFragment,PhotoGridFragment.TAG)
                    .commit();
        }else{
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,photoGridFragment,PhotoGridFragment.TAG)
                    .commit();
        }
    }
}
