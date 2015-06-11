package com.adrobnych.imagegalleryinterviewtask.model.managers;

import com.adrobnych.imagegalleryinterviewtask.model.entities.Favorites;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by adrobnych on 6/11/15.
 */
public class FavoritesManager {
    private Dao<Favorites, Integer> favoritesDao = null;

    public void setRegistrationDao(Dao<Favorites, Integer> favDao) {
        this.favoritesDao = favDao;
    }

    public Favorites markAsFavorite(int imageId) throws SQLException {

        Favorites fav = new Favorites();
        fav.setImage_id(imageId);

        favoritesDao.create(fav);

        return fav;

    }

    public boolean isFavorite(Integer imageId) throws SQLException {
        if(favoritesDao.queryBuilder().where().eq("image_id", imageId).query().size() != 0)
            return  true;
        else
            return false;
    }

}
