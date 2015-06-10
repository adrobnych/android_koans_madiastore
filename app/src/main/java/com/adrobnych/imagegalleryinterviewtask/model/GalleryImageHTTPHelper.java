package com.adrobnych.imagegalleryinterviewtask.model;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adrobnych on 3/27/15.
 */
public class GalleryImageHTTPHelper {
    private static final String POST_URL =
        "http://ec2-176-34-216-72.eu-west-1.compute.amazonaws.com/rest/map/discovery?access_token=C3AE7&installation_uuid=6fy&device=x86_64";
    private static final String POST_DATA =
        "{\"mapId\": 1, \"userLang\": \"en\"}";
    private static HttpClient httpClient = new DefaultHttpClient();
    private GalleryImageManager gm;

    public GalleryImageHTTPHelper(GalleryImageManager gm){
        this.gm = gm;
    }



    public String fetchRemoteJsonString() throws IOException, JSONException {

        HttpPost httppost = new HttpPost(POST_URL);

        httppost.setEntity(new ByteArrayEntity(POST_DATA.getBytes(Charset.forName("UTF-8"))));

        HttpResponse response = httpClient.execute(httppost);
        HttpEntity entity = response.getEntity();

        String result = IOUtils.toString(entity.getContent(), "UTF-8");

        JSONObject jObject = new JSONObject(result);
        JSONObject jReturnObject = jObject.getJSONObject("return");

        //feed Image Map
        Map<String, String> imageMap = new HashMap<>();
        JSONArray jImagesArray = jReturnObject.getJSONArray("images");

        for (int i=0; i < jImagesArray.length(); i++){
            imageMap.put(jImagesArray.getJSONObject(i).getString("id"),
                         jImagesArray.getJSONObject(i).getString("urlPreview"));
        }



        //feed galleryData
        JSONArray jPoisArray = jReturnObject.getJSONArray("pois");

        gm.clearData();

        for (int i=0; i < jPoisArray.length(); i++)
        {
            String message = jPoisArray.getJSONObject(i).getString("recMessage");
            String imageId = jPoisArray.getJSONObject(i).getString("imageId");
            gm.addGalleryItem(message, imageMap.get(imageId));

        }


        return "data succesifully loaded";//gm.getGalleryItems().toString();

    }
}
