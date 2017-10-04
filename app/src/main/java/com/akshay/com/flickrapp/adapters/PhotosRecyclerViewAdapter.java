package com.akshay.com.flickrapp.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akshay.com.flickrapp.R;
import com.akshay.com.flickrapp.databinding.PhotoRowItemBinding;
import com.akshay.com.flickrapp.models.Photo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by akshaymathur on 10/2/17.
 */

public class PhotosRecyclerViewAdapter extends RecyclerView.Adapter<PhotosRecyclerViewAdapter.PhotoViewHolder> {

    private Context mContext;
    private List<Photo> mPhotoList;
    private final static String TAG = "PhotosViewAdapter";

    public PhotosRecyclerViewAdapter(Context context, List<Photo> photoList){
        mContext = context;
        mPhotoList = photoList;
    }

    public void addMorePhotos(List<Photo> photoList){
        int initialSize = getItemCount();
        mPhotoList.addAll(photoList);
        notifyItemRangeInserted(initialSize,photoList.size());
    }

    @Override
    public PhotosRecyclerViewAdapter.PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new PhotoViewHolder(inflater.inflate(R.layout.photo_row_item,parent,false));
    }

    @Override
    public void onBindViewHolder(PhotosRecyclerViewAdapter.PhotoViewHolder holder, int position) {
        Photo photo = mPhotoList.get(position);
        holder.bind(photo);
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder{

        private PhotoRowItemBinding mBinding;
        public PhotoViewHolder(View itemView){
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        void bind(Photo photo){

            //https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
            String url = "https://farm%s.staticflickr.com/%s/%s_%s_n.jpg";
            final String photoUrl = String.format(url,photo.getFarm(),
                    photo.getServer(),photo.getId(),photo.getSecret());
            Log.d(TAG, "Fetching url " + photoUrl);
            mBinding.progressBar.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(photoUrl)
                    .resize(320,0)
                    .transform(new RoundedCornersTransformation(5, 5))
                    .into(mBinding.ivPhotoThumbnail, new Callback() {
                        @Override
                        public void onSuccess() {
                            mBinding.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            mBinding.progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "Loading failed for "+photoUrl);
                        }
                    });
        }
    }
}
