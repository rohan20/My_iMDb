package com.rohan.myimdb.Adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohan.myimdb.Models.Review;
import com.rohan.myimdb.POJOs.ResponseSingleReview;
import com.rohan.myimdb.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rohan on 28-Aug-16.
 */
public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder> {

    Context mContext;
//    List<ResponseSingleReview> mReviews;
    ArrayList<Review> mReviews;

    public ReviewsRecyclerViewAdapter(Context context) {
        mContext = context;
    }

//    public void setReviewsList(List<ResponseSingleReview> reviews) {
    public void setReviewsList(ArrayList<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.review_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.mAuthorTextView.setText(mReviews.get(position).getAuthor());
        holder.mContentTextView.setText(mReviews.get(position).getContent());

        holder.mReviewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);

                View popupView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.review_item_popup, null);
                dialogBuilder.setView(popupView);

                TextView author = (TextView) popupView.findViewById(R.id.review_author_text_view_popup);
                TextView content = (TextView) popupView.findViewById(R.id.review_content_text_view_popup);
                ImageView crossButton = (ImageView) popupView.findViewById(R.id.cross_button_popup);

                author.setText(mReviews.get(position).getAuthor());
                content.setText(mReviews.get(position).getContent());

                final AlertDialog mDialog = dialogBuilder.create();
                mDialog.show();

                crossButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.review_layout)
        CardView mReviewItem;
        @BindView(R.id.review_author_text_view)
        TextView mAuthorTextView;
        @BindView(R.id.review_content_text_view)
        TextView mContentTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
