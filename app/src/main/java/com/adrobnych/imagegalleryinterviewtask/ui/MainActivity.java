package com.adrobnych.imagegalleryinterviewtask.ui;

import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.adrobnych.imagegalleryinterviewtask.R;
import com.adrobnych.imagegalleryinterviewtask.adapters.ImageAdapter;


public class MainActivity extends AppCompatActivity {

    private final Uri sourceUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public final static Uri thumbUri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
    public final static String thumb_DATA = MediaStore.Images.Thumbnails.DATA;
    public final static String thumb_IMAGE_ID = MediaStore.Images.Thumbnails.IMAGE_ID;

    ImageAdapter mySimpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.gridview);


        CursorLoader cursorLoader = new CursorLoader(
                this,
                sourceUri,
                null,
                null,
                null,
                MediaStore.Audio.Media.TITLE);

        Cursor cursor = cursorLoader.loadInBackground();

        mySimpleCursorAdapter = new ImageAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{},
                new int[]{},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        gridview.setAdapter(mySimpleCursorAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
