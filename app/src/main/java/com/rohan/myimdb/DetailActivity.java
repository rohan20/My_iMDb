package com.rohan.myimdb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rohan.myimdb.Utils.Constants;

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
