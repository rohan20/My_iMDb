package com.rohan.myimdb;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

// TODO: 17-Jul-16 Retrofit
// TODO: 17-Jul-16 Use Butterknife
// TODO: 17-Jul-16 Create Movie, Review, Trailer model class
// TODO: 17-Jul-16 SugarORM for favourites
// TODO: 17-Jul-16 Display API last updated text somewhere
// TODO: 17-Jul-16 Handle Android M internet permission case
public class MainActivity extends AppCompatActivity implements MoviesGridFragment.OnMovieSelected, View.OnClickListener {

    @Nullable
    @BindView(R.id.back_button)
    ImageView mBackButton;
    @Nullable
    @BindView(R.id.toolbar_text_view)
    TextView mToolbarTextView;
    @Nullable
    @BindView(R.id.toolbar_main_activity)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (mBackButton != null)
            mBackButton.setOnClickListener(this);

        if (mToolbar != null)
            setSupportActionBar(mToolbar);
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

    public void hideBackButton() {
        if (mBackButton != null)
            mBackButton.setVisibility(View.GONE);
    }

    public void showBackButton() {
        if (mBackButton != null)
            mBackButton.setVisibility(View.VISIBLE);
    }

    public void setToolbarText(String text) {
        if (mToolbarTextView != null) {

            mToolbarTextView.setText(text);
            mToolbarTextView.setTextSize(16);
        }
    }

    public void setToolbarText(String text, int textSize) {
        if (mToolbarTextView != null) {
            mToolbarTextView.setText(text);
            mToolbarTextView.setTextSize(textSize);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (findViewById(R.id.container_grid) != null) {            // => if you're in portrait mode
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_grid, new MoviesGridFragment())
                    .commit();
        }

    }

    @Override
    public void movieSelected() {

        if (findViewById(R.id.container_details) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_grid, new MovieDetailsFragment())
                    .addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_details, new MovieDetailsFragment())
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onClick(View view) {

        if (findViewById(R.id.back_button) != null) {

            switch (view.getId()) {
                case R.id.back_button:
                    if (findViewById(R.id.container_grid) != null) {            // => if you're in portrait mode
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_grid, new MoviesGridFragment())
                                .commit();
                    }
            }
        }
    }
}
