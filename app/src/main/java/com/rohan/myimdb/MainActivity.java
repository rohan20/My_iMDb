package com.rohan.myimdb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rohan.myimdb.Utils.Constants;

// TODO: 17-Jul-16 Handle Android M internet permission case
public class MainActivity extends AppCompatActivity implements MoviesGridFragment.OnMovieSelected {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String DETAIL_FRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.container_details) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_details, new MovieDetailsFragment(), DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }

    @Override
    public void movieSelected(String id) {

        //portrait mode
        if (!mTwoPane) {
            Intent launchDetailActivityIntent = new Intent(MainActivity.this, DetailActivity.class);
            launchDetailActivityIntent.putExtra(Constants.MOVIE_ID, id);
            startActivity(launchDetailActivityIntent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        }
        //landscape mode or tablet mode
        else {
            MovieDetailsFragment fragment = new MovieDetailsFragment();
            Bundle b = new Bundle();
            b.putString(Constants.MOVIE_ID, id);
            fragment.setArguments(b);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_details, fragment)
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
