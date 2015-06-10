package com.adrobnych.imagegalleryinterviewtask;

import android.app.Application;

import com.adrobnych.imagegalleryinterviewtask.ui.MainActivity;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by adrobnych on 6/10/15.
 */
public class GalleryApp extends Application {

    private static final String TAG = "GalleryApp";

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private MainActivity mainActivity;


}
