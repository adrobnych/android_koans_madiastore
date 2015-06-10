package com.adrobnych.imagegalleryinterviewtask.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.adrobnych.imagegalleryinterviewtask.GalleryApp;
import com.adrobnych.imagegalleryinterviewtask.R;
import com.adrobnych.imagegalleryinterviewtask.model.GalleryImageManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class GalleryImageFragment extends Fragment {

    private int page;
    Bitmap bmp;
    ImageView im;
    URL url;
    View view;
    GalleryImageManager gm;

    public static GalleryImageFragment newInstance(int page) {
        GalleryImageFragment f = new GalleryImageFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gallery_image, container, false);
        im = (ImageView) view.findViewById(R.id.imageView);

        gm = ((GalleryApp)getActivity().getApplication()).getGalleryManager();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //TextView messageTV = (TextView) view.findViewById(R.id.tvMessageTitle);
        //messageTV.setText(gm.getGalleryItemById(page).get(GalleryImageManager.MESSAGE));

        //TextView pageTV = (TextView) view.findViewById(R.id.tvPageCounter);
        //pageTV.setText("" + page + "/" + gm.getGallerySize());

        url = null;
        try {
            url = new URL(gm.getGalleryItemById(page).get(GalleryImageManager.IMAGE_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new LongOperation().execute("");

    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            bmp = null;
            try {
                URLConnection connection = url.openConnection();
                connection.setUseCaches(true);
                bmp = BitmapFactory.decodeStream((InputStream) connection.getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            im.setImageBitmap(bmp);
        }

    }



}
