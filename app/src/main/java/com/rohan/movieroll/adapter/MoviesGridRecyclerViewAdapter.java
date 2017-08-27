package com.rohan.movieroll.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.rohan.movieroll.model.Movie;
import com.rohan.movieroll.R;
import com.rohan.movieroll.util.Constants;
import com.rohan.movieroll.util.IOnMovieSelectedAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Rohan on 17-Jul-16.
 */
public class MoviesGridRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MOVIE_VIEW = 1;
    public static final int AD_VIEW = 2;

    private Context mContext;
    private List<Movie> mMoviesList;
    private IOnMovieSelectedAdapter mMovieClickedAdapterListener;
    private SharedPreferences mPrefs;

    public MoviesGridRecyclerViewAdapter(Context context, IOnMovieSelectedAdapter adapterCallback) {
        mContext = context;
        mMovieClickedAdapterListener = adapterCallback;
        mPrefs = mContext.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
    }

    public void setRecyclerViewList(List<Movie> moviesList) {
        mMoviesList = moviesList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v;
        if (viewType == AD_VIEW) {
            v = inflater.inflate(R.layout.ad_movie_reyclcer_view_item, parent, false);
            return new NativeAdViewHolder(v);
        }

        v = inflater.inflate(R.layout.movies_grid_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {

        if (mMoviesList.get(position).getId().equals("-1")) {
            return AD_VIEW;
        }

        return MOVIE_VIEW;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewholder, final int position) {

        if (viewholder instanceof NativeAdViewHolder) {

            final NativeAdViewHolder holder = (NativeAdViewHolder) viewholder;

            if (mContext.getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE) {
                holder.itemView.setLayoutParams(new LinearLayoutCompat.LayoutParams(0, 0));
                return;
            }

            AdRequest request = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                    .addTestDevice("5BBDB114D900920A2045F20FC8A733CE")  // MyOnePlus2
                    .build();

            holder.mNativeAd.loadAd(request);
            return;
        }

        final Movie movie = mMoviesList.get(position);

        final ViewHolder holder = (ViewHolder) viewholder;

        StringBuilder builder = new StringBuilder();
        builder.append(Constants.IMAGE_PATH_PREFIX)
                .append(movie.getPosterPath());

        Picasso.with(mContext).load(builder.toString()).placeholder(R.drawable.placeholder_no_image_available).into(holder.mMoviePosterImageView);

        holder.mMoviePosterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int movieSelectedNumberOfTimes = mPrefs.getInt(Constants.MOVIE_CLICKED_NUMBER_OF_TIMES, 0);

                movieSelectedNumberOfTimes++;

                mPrefs.edit().putInt(Constants.MOVIE_CLICKED_NUMBER_OF_TIMES, movieSelectedNumberOfTimes).apply();

                if (holder.getAdapterPosition() >= mMoviesList.size()) {
                    Toast.makeText(mContext, mContext.getString(R.string.please_refresh), Toast.LENGTH_SHORT).show();
                    return;
                }

                String id = movie.getId();
                mPrefs.edit().putString(Constants.SELECTED_ID, id).apply();
                mMovieClickedAdapterListener.onMovieClickedCallback();

                //show interstial ad on every 3rd time a movie selected
                if (mContext.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
                        .getInt(Constants.MOVIE_CLICKED_NUMBER_OF_TIMES, 0) % 3 == 0) {
                    final InterstitialAd interstitialAd = new InterstitialAd(mContext);
                    interstitialAd.setAdUnitId(mContext.getString(R.string.ad_unit_id_full_screen));
                    AdRequest adRequest = new AdRequest.Builder()
                            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                            .addTestDevice("5BBDB114D900920A2045F20FC8A733CE")  // MyOnePlus2
                            .build();
                    interstitialAd.loadAd(adRequest);
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            interstitialAd.show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mMoviePosterImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.movie_image_on_poster);
        }
    }

    public class NativeAdViewHolder extends RecyclerView.ViewHolder {

        NativeExpressAdView mNativeAd;

        public NativeAdViewHolder(final View itemView) {
            super(itemView);

            mNativeAd = (NativeExpressAdView) itemView.findViewById(R.id.native_ad);

            mNativeAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mNativeAd.setVisibility(View.VISIBLE);
                }
            });
        }
    }

}
