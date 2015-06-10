package com.adrobnych.imagegalleryinterviewtask.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


import com.adrobnych.imagegalleryinterviewtask.GalleryApp;
import com.adrobnych.imagegalleryinterviewtask.model.GalleryImageHTTPHelper;
import com.adrobnych.imagegalleryinterviewtask.model.GalleryImageManager;

import org.json.JSONException;

import java.io.IOException;


public class GalleryDataLoaderService extends IntentService {
    private String result = null;
    public final static String RESULT = "result";
    public static final String NOTIFICATION = "com.adrobnych.imagegalleryinterviewtask.services.loadgallerydata.receiver";

    public GalleryDataLoaderService() {
        super("GalleryDataLoaderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("galleryservice--->", "received intent");
        GalleryImageManager gm = ((GalleryApp) getApplication()).getGalleryManager();
        GalleryImageHTTPHelper gh = gm.getGalleryHTTPHelper();
        result = "errorFetchingJson";

        try {
            result = gh.fetchRemoteJsonString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
        e.printStackTrace();
        }

        publishResults(result);
    }

    private void publishResults(String result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}