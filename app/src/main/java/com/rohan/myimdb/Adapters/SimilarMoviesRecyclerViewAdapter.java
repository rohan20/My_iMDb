package com.rohan.myimdb.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohan.myimdb.Models.Movie;
import com.rohan.myimdb.POJOs.ResponseListResults;
import com.rohan.myimdb.R;
import com.rohan.myimdb.Utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rohan on 17-Jul-16.
 */
public class SimilarMoviesRecyclerViewAdapter extends RecyclerView.Adapter<SimilarMoviesRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Movie> mSimilarMoviesList;

    public SimilarMoviesRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public void setRecyclerViewList(List<Movie> similarMoviesList) {
        mSimilarMoviesList = similarMoviesList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.cast_crew_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(mContext).load(Constants.IMAGE_PATH_PREFIX + mSimilarMoviesList.get(position).getPosterPath()).into(holder.mMoviePoster);
        holder.mMovieName.setText(mSimilarMoviesList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mSimilarMoviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cast_crew_image)
        ImageView mMoviePoster;
        @BindView(R.id.cast_crew_name)
        TextView mMovieName;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

}
