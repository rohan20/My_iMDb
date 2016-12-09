package com.rohan.myimdb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rohan.myimdb.Utils.Constants;
import com.rohan.myimdb.Utils.IOnMovieSelected;

// TODO: 09-Dec-16 Remove hardcoding

public class MainActivity extends AppCompatActivity implements IOnMovieSelected {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.container_details) != null) {
            mTwoPane = true;

            DetailFragment fragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(Constants.DETAIL_FRAGMENT_TAG);
            if (fragment == null) {
                fragment = new DetailFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_details, fragment, Constants.DETAIL_FRAGMENT_TAG)
                    .commit();

        } else {
            mTwoPane = false;
        }

    }

    @Override
    public void movieSelected() {

        //portrait mode
        if (!mTwoPane) {
            Intent launchDetailActivityIntent = new Intent(MainActivity.this, DetailActivity.class);
            launchDetailActivityIntent.putExtra(Constants.MOVIE_ID, getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE).getString(Constants.SELECTED_ID, null));
            startActivity(launchDetailActivityIntent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }

        //landscape mode or tablet mode
        else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_details, new DetailFragment(), Constants.DETAIL_FRAGMENT_TAG)
                    .commit();
        }
    }

    public void favouritesUpdated() {
        if (mTwoPane) {
            GridFragment gridFragment = (GridFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_grid);
            gridFragment.updateFavouritesGrid();
        }
    }

}
