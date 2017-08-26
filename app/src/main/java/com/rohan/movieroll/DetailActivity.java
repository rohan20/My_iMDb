package com.rohan.movieroll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rohan.movieroll.Utils.Constants;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DetailFragment fragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(Constants.DETAIL_FRAGMENT_TAG);

        if (fragment == null) {
            fragment = new DetailFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_details, fragment, Constants.DETAIL_FRAGMENT_TAG)
                .commit();

        AdView mAdView = (AdView) findViewById(R.id.banner_ad);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("5BBDB114D900920A2045F20FC8A733CE")  // MyOnePlus2
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
