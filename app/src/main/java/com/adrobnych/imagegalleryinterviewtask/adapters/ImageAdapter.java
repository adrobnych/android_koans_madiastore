package com.adrobnych.imagegalleryinterviewtask.adapters;

import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.content.Context;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


import com.adrobnych.imagegalleryinterviewtask.R;
import com.adrobnych.imagegalleryinterviewtask.ui.MainActivity;

/**
 * Created by adrobnych on 6/6/15.
 */
public class ImageAdapter extends SimpleCursorAdapter {
    private Context mContext;
    private Cursor mCursor;

    public ImageAdapter(Context context, int layout, Cursor c, String[] from,
                        int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mCursor = c;
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        mCursor.moveToPosition(position);

        int imageID = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media._ID));

        String[] imageProjection = {MainActivity.thumb_DATA, MainActivity.thumb_IMAGE_ID};
        CursorLoader thumbCursorLoader = new CursorLoader(
                mContext,
                MainActivity.thumbUri,
                imageProjection,
                MainActivity.thumb_IMAGE_ID + "=" + imageID,
                null,
                null);
        Cursor thumbCursor = thumbCursorLoader.loadInBackground();

        Bitmap myBitmap = null;
        if(thumbCursor.moveToFirst()){
            int thCulumnIndex = thumbCursor.getColumnIndex(MainActivity.thumb_DATA);
            String thumbPath = thumbCursor.getString(thCulumnIndex);
            myBitmap = BitmapFactory.decodeFile(thumbPath);
            imageView.setImageBitmap(myBitmap);
        }

        thumbCursor.close();
        return imageView;
    }


}
