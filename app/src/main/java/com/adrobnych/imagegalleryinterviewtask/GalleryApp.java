package com.adrobnych.imagegalleryinterviewtask;

import android.app.Application;
import android.util.Log;

import com.adrobnych.imagegalleryinterviewtask.model.db.DBHelper;
import com.adrobnych.imagegalleryinterviewtask.model.entities.Favorites;
import com.adrobnych.imagegalleryinterviewtask.model.managers.FavoritesManager;
import com.adrobnych.imagegalleryinterviewtask.ui.MainActivity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by adrobnych on 6/10/15.
 */
public class GalleryApp extends Application {

    private static final String TAG = "GalleryApp";
    private DBHelper dbHelper;
    private FavoritesManager favoritesManager;

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private MainActivity mainActivity;

    public GalleryApp() {
        super();
        try {
            dbHelper = new DBHelper(this);
        } catch (SQLException e) {
            Log.e(TAG, String.valueOf(e));
        }

    }

    //TODO: implement double-checked singleton
    public FavoritesManager getFavoritesManager(){
        if (null == favoritesManager) {

            favoritesManager = new FavoritesManager();
            Dao<Favorites, Integer> favoritesDao;
            try {
                favoritesDao = DaoManager.createDao(dbHelper.getConnectionSource(), Favorites.class);
                favoritesManager.setRegistrationDao(favoritesDao);
            } catch (SQLException e) {
                Log.e(TAG, "getFavoritesManager", e);
            }

        }
        return favoritesManager;
    }

}
