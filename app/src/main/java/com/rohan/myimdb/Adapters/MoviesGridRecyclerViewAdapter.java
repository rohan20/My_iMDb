package com.rohan.myimdb.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.rohan.myimdb.Models.Movie;
import com.rohan.myimdb.R;
import com.rohan.myimdb.Utils.IOnMovieSelectedAdapter;
import com.rohan.myimdb.Utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Rohan on 17-Jul-16.
 */
public class MoviesGridRecyclerViewAdapter extends RecyclerView.Adapter<MoviesGridRecyclerViewAdapter.ViewHolder> {

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

        Picasso.with(mContext).load(builder.toString()).placeholder(R.drawable.placeholder_no_image_available).into(holder.mMoviePosterImageView);

        holder.mMoviePosterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.getAdapterPosition() >= mMoviesList.size()) {
                    Toast.makeText(mContext, "Please refresh", Toast.LENGTH_SHORT).show();
                    return;
                }

                String id = mMoviesList.get(position).getId();
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

}
