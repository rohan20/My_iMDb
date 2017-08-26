package com.rohan.movieroll;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rohan.movieroll.Utils.Constants;
import com.rohan.movieroll.Utils.IOnMovieSelected;

// TODO: 09-Dec-16 Remove hardcoding

public class MainActivity extends AppCompatActivity implements IOnMovieSelected {

    private boolean mTwoPane;
    private AdView mAdView;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mPrefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

        mAdView = (AdView) findViewById(R.id.banner_ad);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("5BBDB114D900920A2045F20FC8A733CE")  // MyOnePlus2
                .build();
        mAdView.loadAd(adRequest);

        if (findViewById(R.id.container_details) != null) {
            mPrefs.edit().putBoolean(Constants.SHOW_RECYCLER_VIEW_ADS, false).apply();
            mTwoPane = true;

            DetailFragment fragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(Constants.DETAIL_FRAGMENT_TAG);
            if (fragment == null) {
                fragment = new DetailFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_details, fragment, Constants.DETAIL_FRAGMENT_TAG)
                    .commit();

        } else {
            mPrefs.edit().putBoolean(Constants.SHOW_RECYCLER_VIEW_ADS, true).apply();
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
