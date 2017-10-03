package com.akshay.com.flickrapp.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.akshay.com.flickrapp.models.Photo;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.List;

/**
 * Created by akshaymathur on 10/3/17.
 */

public class FlickrDatabaseHelper {

    private static final String TAG = "FlickrDatabaseHelper";

    public static void saveAllPhotos(final List<Photo> photoList){
        DatabaseDefinition database = FlowManager.getDatabase(FlickrDatabase.class);
        Transaction transaction = database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                for(Photo photo : photoList){
                    photo.save();
                }
            }
        }).success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                Log.d(TAG, "Photos saved successfully");
            }
        }).error(new Transaction.Error() {
            @Override
            public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                Log.d(TAG, "Failed to save photos");
            }
        }).build();

        transaction.execute();
    }

    public static List<Photo> getAllPhotos(){

        List<Photo> photos = SQLite.select().from(Photo.class).queryList();
        return photos;
    }

    public static void deleteAllPhotos(){
        SQLite.delete(Photo.class)
                .async().success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                Log.d(TAG, "All photos deleted");
            }
        }).error(new Transaction.Error() {
            @Override
            public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                Log.d(TAG, "Failed to delete all photos");
            }
        }).execute();
    }
}
