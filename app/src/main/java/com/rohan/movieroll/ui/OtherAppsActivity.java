package com.rohan.movieroll.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rohan.movieroll.R;
import com.rohan.movieroll.adapter.AppsAdapter;
import com.rohan.movieroll.model.App;
import com.rohan.movieroll.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class OtherAppsActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout mParentLayout;
    RecyclerView mAppsRecyclerView;
    AppsAdapter mAppsAdapter;

    ImageView mFacebookImageView;
    ImageView mLinkedInImageView;
    ImageView mGmailImageView;
    ImageView mGithubImageView;
    ImageView mQuoraImageView;

    String mURL;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_apps);

        AdView mAdView = (AdView) findViewById(R.id.banner_ad);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("5BBDB114D900920A2045F20FC8A733CE")  // MyOnePlus2
                .build();
        mAdView.loadAd(adRequest);

        initUI();
    }

    private void initUI() {

        mParentLayout = (RelativeLayout) findViewById(R.id.about_us_parent_layout);
        mAppsRecyclerView = (RecyclerView) findViewById(R.id.apps_recycler_view);
        showSnackbar(getString(R.string.view_app_on_playstore));

        mAppsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAppsAdapter = new AppsAdapter(this, getAppsList());
        mAppsRecyclerView.setAdapter(mAppsAdapter);

        mFacebookImageView = (ImageView) findViewById(R.id.facebook_icon_image_view);
        mLinkedInImageView = (ImageView) findViewById(R.id.linkedin_icon_image_view);
        mGmailImageView = (ImageView) findViewById(R.id.gmail_icon_image_view);
        mGithubImageView = (ImageView) findViewById(R.id.github_icon_image_view);
        mQuoraImageView = (ImageView) findViewById(R.id.quora_icon_image_view);

        mFacebookImageView.setOnClickListener(this);
        mLinkedInImageView.setOnClickListener(this);
        mGmailImageView.setOnClickListener(this);
        mGithubImageView.setOnClickListener(this);
        mQuoraImageView.setOnClickListener(this);

        mURL = Constants.MY_LINKEDIN_URL;
        i = new Intent(Intent.ACTION_VIEW);
    }

    private List<App> getAppsList() {

        List<App> appsList = new ArrayList<>();

        appsList.add(new App("Call Note", "Now save your 'after call' notes hassle free. All notes include call details. Uses Google login so that you can access your notes from anywhere!", "http://i.imgur.com/S2eUZTi.png",
                "https://play.google.com/store/apps/details?id=com.rohan.callnote2"));
        appsList.add(new App("Balloon Popper", "Easy to play game where you get to pop as many balloons" +
                " as you can by just touching them. The catch? You get only 5 lives!",
                "http://i.imgur.com/w5GY0yX.png", "https://play.google" +
                ".com/store/apps/details?id=com.rohan.balloongame"));

        return appsList;
    }

    public void showSnackbar(String snackbarText) {
        Snackbar.make(mParentLayout, snackbarText, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.facebook_icon_image_view:
                mURL = Constants.MY_FACEBOOK_URL;
                break;

            case R.id.linkedin_icon_image_view:
                mURL = Constants.MY_LINKEDIN_URL;
                break;

            case R.id.quora_icon_image_view:
                mURL = Constants.MY_QUORA_URL;
                break;

            case R.id.github_icon_image_view:
                mURL = Constants.MY_GITHUB_URL;
                break;

            case R.id.gmail_icon_image_view:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: " + Constants.MY_GMAIL_EMAIL));
                startActivity(emailIntent);
                return;

        }

        i.setData(Uri.parse(mURL));
        startActivity(i);

    }

}
