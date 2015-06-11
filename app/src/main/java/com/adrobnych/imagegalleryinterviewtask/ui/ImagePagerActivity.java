package com.adrobnych.imagegalleryinterviewtask.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.adrobnych.imagegalleryinterviewtask.GalleryApp;
import com.adrobnych.imagegalleryinterviewtask.R;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class ImagePagerActivity extends AppCompatActivity {
    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    private int imageID;
    private int page;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);

        pager = (ViewPager) findViewById(R.id.pager);
        page = getIntent().getIntExtra("position", 0);

        resetPagerAdapter();

        this.imageID = getIntent().getIntExtra("image_id", -1);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Cursor cursor = ((GalleryApp)getApplication()).getMainActivity().
                        mySimpleCursorAdapter.getCursor();
                cursor.moveToPosition(position);
                setImageID(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID)));

                page = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void resetPagerAdapter() {
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), getAllFragments()));
        pager.setCurrentItem(page);
    }


    private Map<Integer, Fragment> getAllFragments() {
        Map<Integer, Fragment> fragmentMap = new HashMap<>();
        int count = ((GalleryApp)getApplication()).getMainActivity().mySimpleCursorAdapter.getCursor().getCount();
        for(int i=0; i<count; i++)
            fragmentMap.put(i, GalleryImageFragment.newInstance(i));


        return fragmentMap;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.open) {
            openImageInExternalApp();
            return true;
        }

        if (id == R.id.favorite) {
            favoriteImage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void favoriteImage() {
        try {
            ((GalleryApp)getApplication()).getFavoritesManager().markAsFavorite(imageID);
            resetPagerAdapter();
        } catch (SQLException e) {
            Log.e("ImagePagerActivity", e.toString());
        }
    }

    //TODO: investigate Google+ crash in some apps
    private void openImageInExternalApp() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(imageID)),
                "image/*");
        startActivity(intent);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS;
        private Map<Integer, Fragment> allFragments;

        public MyPagerAdapter(FragmentManager fragmentManager, Map<Integer, Fragment> allFragments) {
            super(fragmentManager);
            NUM_ITEMS = allFragments.size();
            this.allFragments = allFragments;
        }


        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return allFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }
}
