package com.rohan.myimdb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.rohan.myimdb.Utils.Constants;

import static android.R.attr.fragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        String id = i.getStringExtra(Constants.MOVIE_ID);

        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle b = new Bundle();
        b.putString(Constants.MOVIE_ID, id);
        fragment.setArguments(b);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_details, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
