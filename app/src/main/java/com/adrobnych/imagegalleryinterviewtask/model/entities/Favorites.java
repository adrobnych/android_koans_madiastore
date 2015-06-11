package com.adrobnych.imagegalleryinterviewtask.model.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by adrobnych on 6/11/15.
 */
@DatabaseTable(tableName = "favorites")
public class Favorites {
    @DatabaseField(generatedId = true)
    private Integer id;

    public Integer getImage_id() {
        return image_id;
    }

    public void setImage_id(Integer image_id) {
        this.image_id = image_id;
    }

    @DatabaseField(columnName = "image_id", canBeNull = false)
    private Integer image_id;
}
