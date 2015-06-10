package com.adrobnych.imagegalleryinterviewtask.ui;

import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.adrobnych.imagegalleryinterviewtask.GalleryApp;
import com.adrobnych.imagegalleryinterviewtask.R;


public class GalleryImageFragment extends Fragment {

    private int page;
    ImageView im;
    View view;

    public static GalleryImageFragment newInstance(int page) {
        GalleryImageFragment fragment = new GalleryImageFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("page", 0);
;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gallery_image, container, false);
        im = (ImageView) view.findViewById(R.id.imageView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Cursor cursor =
            ((GalleryApp)(getActivity().getApplication())).getMainActivity().mySimpleCursorAdapter.getCursor();
        cursor.moveToPosition(page);

        int int_ID = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));


        im.setImageBitmap(getThumbnail(int_ID));
    }

    //TODO: optimize quality of thumbs. research MINI vs MICRO thumbs
    private Bitmap getThumbnail(int id){

        String[] thumbColumns = {MainActivity.thumb_DATA, MainActivity.thumb_IMAGE_ID};

        CursorLoader thumbCursorLoader = new CursorLoader(
                getActivity(),
                MainActivity.thumbUri,
                thumbColumns,
                MainActivity.thumb_IMAGE_ID + "=" + id,
                null,
                null);

        Cursor thumbCursor = thumbCursorLoader.loadInBackground();

        Bitmap thumbBitmap = null;
        if(thumbCursor.moveToFirst()){
            int thCulumnIndex = thumbCursor.getColumnIndex(MainActivity.thumb_DATA);

            String thumbPath = thumbCursor.getString(thCulumnIndex);

            thumbBitmap = BitmapFactory.decodeFile(thumbPath);

        }else{
            Toast.makeText(getActivity().getApplicationContext(),
                    "NO Thumbnail!",
                    Toast.LENGTH_LONG).show();
        }

        return thumbBitmap;
    }



}
