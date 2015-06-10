package com.adrobnych.imagegalleryinterviewtask;

import android.app.Application;

import com.adrobnych.imagegalleryinterviewtask.model.GalleryImageHTTPHelper;
import com.adrobnych.imagegalleryinterviewtask.model.GalleryImageManager;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by adrobnych on 6/10/15.
 */
public class GalleryApp extends Application {

    private static final String TAG = "GalleryApp";
    private volatile GalleryImageManager gManager = null;

    public GalleryImageManager getGalleryManager(){
        if (null == gManager)
            synchronized(this){
                if (null == gManager) {

                    gManager = new GalleryImageManager();
                    gManager.setGalleryHTTPHelper(new GalleryImageHTTPHelper(gManager));
                    gManager.setGalleryItems(new TreeMap<Integer, Map<String, String>>());

                }

            }
        return gManager;
    }
}
