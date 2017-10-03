package com.akshay.com.flickrapp.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by akshaymathur on 10/3/17.
 */

@Database(name = FlickrDatabase.NAME, version = FlickrDatabase.VERSION)
public class FlickrDatabase {

    public static final String NAME = "TweetsDatabaseUpdatedVersion";

    public static final int VERSION = 1;
}
