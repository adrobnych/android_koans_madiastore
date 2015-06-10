package com.adrobnych.imagegalleryinterviewtask.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.adrobnych.imagegalleryinterviewtask.model.entities.Favorites;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by adrobnych on 6/11/15.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = "DBHelper";
    private static final String DB_NAME = "gallery_app.db";
    private static final int DB_VERSION = 1;
    private Context context;

    public DBHelper(Context context) throws SQLException {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            TableUtils.createTableIfNotExists(connectionSource, Favorites.class);

        } catch (java.sql.SQLException e) {
            Log.e(TAG, "onCreate", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Context getContext() {
        return context;
    }
}