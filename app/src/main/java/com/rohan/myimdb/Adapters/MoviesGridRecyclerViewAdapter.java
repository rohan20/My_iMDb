package com.rohan.myimdb.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.rohan.myimdb.Models.Movie;
import com.rohan.myimdb.R;
import com.rohan.myimdb.Utils.AdapterCallback;
import com.rohan.myimdb.Utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.R.attr.id;

/**
 * Created by Rohan on 17-Jul-16.
 */
public class MoviesGridRecyclerViewAdapter extends RecyclerView.Adapter<MoviesGridRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Movie> mMoviesList;
    private AdapterCallback mAdapterCallback;

    public MoviesGridRecyclerViewAdapter(Context context, AdapterCallback adapterCallback) {
        mContext = context;
        mAdapterCallback = adapterCallback;
    }

    public void setRecyclerViewList(List<Movie> moviesList) {
        mMoviesList = moviesList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.movies_grid_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        StringBuilder builder = new StringBuilder();
        builder.append(Constants.IMAGE_PATH_PREFIX)
                .append(mMoviesList.get(position).getPosterPath());

        //set poster
        Picasso.with(mContext).load(builder.toString()).placeholder(R.drawable.placeholder_no_image_available).into(holder.mMoviePosterImageView);
        //set favourites button
//        holder.mMoviePosterFavouriteButton.setChecked(false);

        holder.mMoviePosterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.getAdapterPosition() >= mMoviesList.size()) {
                    Toast.makeText(mContext, "Please refresh", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.e("size", mMoviesList.size() + "");
                Log.e("id", mMoviesList.get(position).getId() + "");
                long id = mMoviesList.get(position).getId();
                mAdapterCallback.onMovieClickedCallback(String.valueOf(id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mMoviePosterImageView;
//        SparkButton mMoviePosterFavouriteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.movie_image_on_poster);
//            mMoviePosterFavouriteButton = (SparkButton) itemView.findViewById(R.id.favourites_button_on_poster);
        }
    }

}
