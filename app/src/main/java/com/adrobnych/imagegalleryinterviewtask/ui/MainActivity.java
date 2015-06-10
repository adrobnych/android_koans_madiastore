package com.adrobnych.imagegalleryinterviewtask.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.adrobnych.imagegalleryinterviewtask.GalleryApp;
import com.adrobnych.imagegalleryinterviewtask.R;
import com.adrobnych.imagegalleryinterviewtask.adapters.ImageAdapter;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    //TODO: load images not only from external storage
    private final Uri sourceUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    //TODO: research situations when image has no thumbnails
    public final static Uri thumbUri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
    public final static String thumb_DATA = MediaStore.Images.Thumbnails.DATA;
    public final static String thumb_IMAGE_ID = MediaStore.Images.Thumbnails.IMAGE_ID;

    public ImageAdapter mySimpleCursorAdapter;
    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = (GridView) findViewById(R.id.gridview);

        ((GalleryApp)getApplication()).setMainActivity(this);

        updateAdapter();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();

                Cursor cursor = mySimpleCursorAdapter.getCursor();
                cursor.moveToPosition(position);

                Intent i = new Intent(MainActivity.this, ImagePagerActivity.class);
                i.putExtra("position", position);
                i.putExtra("image_id", cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                startActivity(i);
            }
        });
    }


    private void updateAdapter() {
        String[] getIdsOnly = { MediaStore.Images.Media._ID };

        CursorLoader cursorLoader = new CursorLoader(
                this,
                sourceUri,
                getIdsOnly,
                null,
                null,
                null);

        //TODO: close existing cursor
        Cursor cursor = cursorLoader.loadInBackground();

        mySimpleCursorAdapter = new ImageAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{},
                new int[]{},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        gridview.setAdapter(mySimpleCursorAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_take_picture) {
            callToExternalCamApp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private Uri imageUri;
    private void callToExternalCamApp() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                + "/DCIM/Camera","picture_name_" +
                String.valueOf(System.currentTimeMillis()) + ".jpg"));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {


                Log.d("mainactivity: new photo", imageUri.toString());

                //Before android 4.4

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

                //TODO: apply this another strategy for refresh with MSCOnnection After android 4.4:
//                MediaScannerConnection.scanFile(this,
//                        new String[] { imageUri.toString() }, null,
//                        new MediaScannerConnection.OnScanCompletedListener() {
//                            public void onScanCompleted(String path, Uri uri) {
//                                Log.i("ExternalStorage", "Scanned " + path + ":");
//                                Log.i("ExternalStorage", "-> uri=" + uri);
//                            }
//                        });


                updateAdapter();

            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
            }
        }


    }



}
