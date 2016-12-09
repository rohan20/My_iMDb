package com.rohan.myimdb.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohan.myimdb.Models.Trailer;
import com.rohan.myimdb.POJOs.ResponseSingleTrailer;
import com.rohan.myimdb.R;
import com.rohan.myimdb.Utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rohan on 28-Aug-16.
 */
public class TrailersRecyclerViewAdapter extends RecyclerView.Adapter<TrailersRecyclerViewAdapter.ViewHolder> {

    Context mContext;
//    List<ResponseSingleTrailer> mTrailers;
    ArrayList<Trailer> mTrailers;

    public TrailersRecyclerViewAdapter(Context context) {
        mContext = context;
    }

//    public void setTrailersList(List<ResponseSingleTrailer> trailers) {
    public void setTrailersList(ArrayList<Trailer> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.trailer_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Picasso.with(mContext).load(Constants.YOUTUBE_THUMBNAIL_PREFIX + mTrailers.get(position).getKey() + Constants.YOUTUBE_THUMBNAIL_SUFFIX).fit().placeholder(R.drawable.placeholder_no_image_available).into(holder.mTrailerImageView);
        holder.mTrailerNameTextView.setText(mTrailers.get(position).getName());

        holder.mTrailerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_VIDEO_PREFIX + mTrailers.get(position).getKey())));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trailer_card_view)
        CardView mTrailerItem;
        @BindView(R.id.trailer_image_view)
        ImageView mTrailerImageView;
        @BindView(R.id.trailer_name_text_view)
        TextView mTrailerNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
