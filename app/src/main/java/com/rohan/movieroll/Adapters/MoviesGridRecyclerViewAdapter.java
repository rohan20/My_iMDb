package com.rohan.movieroll.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.rohan.movieroll.Models.Movie;
import com.rohan.movieroll.R;
import com.rohan.movieroll.Utils.IOnMovieSelectedAdapter;
import com.rohan.movieroll.Utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Rohan on 17-Jul-16.
 */
public class MoviesGridRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int AD_AFTER_NUMBER_OF_ITEMS = 6;
    private static final int MOVIE_VIEW = 1;
    public static final int AD_VIEW = 2;

    private Context mContext;
    private List<Movie> mMoviesList;
    private IOnMovieSelectedAdapter mMovieClickedAdapterListener;

    public MoviesGridRecyclerViewAdapter(Context context, IOnMovieSelectedAdapter adapterCallback) {
        mContext = context;
        mMovieClickedAdapterListener = adapterCallback;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder viewholder, int position) {

        if (viewholder instanceof NativeAdViewHolder) {

            NativeAdViewHolder holder = (NativeAdViewHolder) viewholder;

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

                if (holder.getAdapterPosition() >= mMoviesList.size()) {
                    Toast.makeText(mContext, "Please refresh", Toast.LENGTH_SHORT).show();
                    return;
                }

                String id = movie.getId();
                mContext.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE).edit().putString(Constants.SELECTED_ID, id).apply();
                mMovieClickedAdapterListener.onMovieClickedCallback();

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

            //hide add until it's loaded (see onAdLoaded())
            itemView.setVisibility(View.GONE);

            mNativeAd = (NativeExpressAdView) itemView.findViewById(R.id.native_ad);
            mNativeAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    itemView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

}
