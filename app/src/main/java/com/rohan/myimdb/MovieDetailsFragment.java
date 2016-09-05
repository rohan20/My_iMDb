package com.rohan.myimdb;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.rohan.myimdb.Adapters.CastRecyclerViewAdapter;
import com.rohan.myimdb.Adapters.ReviewsRecyclerViewAdapter;
import com.rohan.myimdb.Adapters.SimilarMoviesRecyclerViewAdapter;
import com.rohan.myimdb.Adapters.TrailersRecyclerViewAdapter;
import com.rohan.myimdb.POJOs.ResponseComplete;
import com.rohan.myimdb.POJOs.ResponseListBackdropsAndPosters;
import com.rohan.myimdb.POJOs.ResponseListCastAndCrew;
import com.rohan.myimdb.POJOs.ResponseListResults;
import com.rohan.myimdb.POJOs.ResponseListReviews;
import com.rohan.myimdb.POJOs.ResponseListTrailer;
import com.rohan.myimdb.POJOs.ResponseSingleCast;
import com.rohan.myimdb.POJOs.ResponseSingleCrew;
import com.rohan.myimdb.POJOs.ResponseSingleResult;
import com.rohan.myimdb.POJOs.ResponseSingleReview;
import com.rohan.myimdb.POJOs.ResponseSingleTrailer;
import com.rohan.myimdb.Utils.Constants;
import com.rohan.myimdb.Utils.RESTAdapter;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO: 17-Jul-16 Trailers RecyclerGridView having cards
// TODO: 17-Jul-16 Reviews StaggeredRecyclerGridView having cards

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends Fragment {

    @BindView(R.id.image_slider)
    SliderLayout mImageSlider;
    @BindView(R.id.genre_text_view)
    TextView mGenresTextView;
    @BindView(R.id.duration_text_view)
    TextView mDurationTextView;
    @BindView(R.id.overview_text_view)
    TextView mOverviewTextView;
    @BindView(R.id.movie_poster_image_view)
    ImageView mMoviePosterImageView;
    @BindView(R.id.release_date_text_view)
    TextView mReleaseDateTextView;
    @BindView(R.id.director_text_view)
    TextView mDirectorTextView;
    @BindView(R.id.rating_text_view)
    TextView mRatingTextView;

    @BindView(R.id.similar_movies_text_view)
    TextView mSimilarMoviesTextView;
    @BindView(R.id.reviews_text_view)
    TextView mReviewsTextView;
    @BindView(R.id.trailers_text_view)
    TextView mTrailersTextView;
    @BindView(R.id.cast_crew_text_view)
    TextView mCastCrewTextVIew;

    @BindView(R.id.cast_recycler_view)
    RecyclerView mCastCrewRecyclerView;
    @BindView(R.id.reviews_recycler_view)
    RecyclerView mReviewsRecyclerView;
    @BindView(R.id.trailers_recycler_view)
    RecyclerView mTrailersRecyclerView;
    @BindView(R.id.similar_movies_recycler_view)
    RecyclerView mSimilarMoviesRecyclerView;

    ProgressDialog dialogDetails;
    ProgressDialog dialogCast;
    ProgressDialog dialogReviews;
    ProgressDialog dialogTrailers;
    ProgressDialog dialogSimilarMovies;

    String movieID;
    RESTAdapter restAdapter;

    Response<ResponseSingleResult> mResponseDetails;
    Response<ResponseListBackdropsAndPosters> mResponseBackdrops;
    Response<ResponseListCastAndCrew> mResponseCastCrew;
    Response<ResponseListTrailer> mResponseTrailers;
    Response<ResponseListReviews> mResponseReviews;
    Response<ResponseComplete> mResponseSimilarMovies;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        ((MainActivity) getActivity()).showBackButton();
//        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();

//        toolbar.setBackgroundColor(Color.parseColor("#80FFFFFF"));

        dialogDetails = new ProgressDialog(getActivity());
        dialogCast = new ProgressDialog(getActivity());
        dialogReviews = new ProgressDialog(getActivity());
        dialogTrailers = new ProgressDialog(getActivity());
        dialogSimilarMovies = new ProgressDialog(getActivity());

        dialogCast.setMessage("Fetching movie cast details...");
        dialogReviews.setMessage("Fetching movie reviews...");
        dialogTrailers.setMessage("Fetching movie trailers...");
        dialogDetails.setMessage("Fetching movie details...");
        dialogSimilarMovies.setMessage("Fetching similar movies...");

        dialogCast.show();
        dialogReviews.show();
        dialogTrailers.show();
        dialogDetails.show();
        dialogSimilarMovies.show();

        callAPIs();

    }

    private void callAPIs() {

        Bundle b = getArguments();

        if (b == null)
            return;

        movieID = b.getString(Constants.MOVIE_ID);

        restAdapter = new RESTAdapter(Constants.MOVIES_DB_BASE_URL);

        callMovieDetailsAPI();
        callBackdropsAPI();
        callCastCrewAPI();
        callReviewsAPI();
        callTrailersAPI();
        callSimilarMoviesAPI();
    }

    private void callSimilarMoviesAPI() {

        Call<ResponseComplete> requestSimilarMovies = restAdapter.getMoviesAPI().getSimilarMovies(movieID, Constants.API_KEY);

        requestSimilarMovies.enqueue(new Callback<ResponseComplete>() {
            @Override
            public void onResponse(Call<ResponseComplete> call, Response<ResponseComplete> response) {
                if (response.isSuccessful()) {
                    mResponseSimilarMovies = response;
                    setSimilarMovies();
                }
            }

            @Override
            public void onFailure(Call<ResponseComplete> call, Throwable t) {
                Toast.makeText(getActivity(), "API response unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setSimilarMovies() {

        mSimilarMoviesTextView.setVisibility(View.VISIBLE);
        mSimilarMoviesRecyclerView.setVisibility(View.VISIBLE);

        List<ResponseListResults> similarMoviesList = new ArrayList<>();

        for (ResponseListResults similarMovie : mResponseSimilarMovies.body().getResults()) {
            similarMoviesList.add(similarMovie);
        }

        SimilarMoviesRecyclerViewAdapter adapter = new SimilarMoviesRecyclerViewAdapter(getActivity());
        mSimilarMoviesRecyclerView.setAdapter(adapter);
        adapter.setRecyclerViewList(similarMoviesList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.HORIZONTAL, false);
        mSimilarMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        if (similarMoviesList.size() == 0) {
            mSimilarMoviesTextView.setVisibility(View.GONE);
            mSimilarMoviesRecyclerView.setVisibility(View.GONE);
        }

        dialogSimilarMovies.dismiss();
    }

    private void callTrailersAPI() {

        Call<ResponseListTrailer> requestTrailers = restAdapter.getMoviesAPI().getTrailers(movieID, Constants.API_KEY);

        requestTrailers.enqueue(new Callback<ResponseListTrailer>() {
            @Override
            public void onResponse(Call<ResponseListTrailer> call, Response<ResponseListTrailer> response) {
                if (response.isSuccessful()) {
                    mResponseTrailers = response;
                    setTrailers();
                }
            }

            @Override
            public void onFailure(Call<ResponseListTrailer> call, Throwable t) {
                Toast.makeText(getActivity(), "API response unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setTrailers() {

        mTrailersTextView.setVisibility(View.VISIBLE);
        mTrailersRecyclerView.setVisibility(View.VISIBLE);

        List<ResponseSingleTrailer> trailersList = new ArrayList<>();

        for (ResponseSingleTrailer trailer : mResponseTrailers.body().getTrailers()) {
            trailersList.add(trailer);
        }

        TrailersRecyclerViewAdapter adapter = new TrailersRecyclerViewAdapter(getActivity());
        mTrailersRecyclerView.setAdapter(adapter);
        adapter.setTrailersList(trailersList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mTrailersRecyclerView.setLayoutManager(linearLayoutManager);

        if (trailersList.size() == 0) {
            mTrailersTextView.setVisibility(View.GONE);
            mTrailersRecyclerView.setVisibility(View.GONE);
        }

        dialogTrailers.dismiss();
    }

    private void callReviewsAPI() {

        Call<ResponseListReviews> requestReviews = restAdapter.getMoviesAPI().getReviews(movieID, Constants.API_KEY);

        requestReviews.enqueue(new Callback<ResponseListReviews>() {
            @Override
            public void onResponse(Call<ResponseListReviews> call, Response<ResponseListReviews> response) {
                if (response.isSuccessful()) {
                    mResponseReviews = response;
                    setReviews();
                }
            }

            @Override
            public void onFailure(Call<ResponseListReviews> call, Throwable t) {
                Toast.makeText(getActivity(), "API response unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setReviews() {

        mReviewsTextView.setVisibility(View.VISIBLE);
        mReviewsRecyclerView.setVisibility(View.VISIBLE);

        List<ResponseSingleReview> reviewsList = new ArrayList<>();

        for (ResponseSingleReview review : mResponseReviews.body().getReviews()) {
            reviewsList.add(review);
        }

        ReviewsRecyclerViewAdapter adapter = new ReviewsRecyclerViewAdapter(getActivity());
        mReviewsRecyclerView.setAdapter(adapter);
        adapter.setReviewsList(reviewsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(layoutManager);

        if (reviewsList.size() == 0) {
            mReviewsTextView.setVisibility(View.GONE);
            mReviewsRecyclerView.setVisibility(View.GONE);
        }

        dialogReviews.dismiss();


    }

    private void callCastCrewAPI() {
        Call<ResponseListCastAndCrew> requestCastCrew = restAdapter.getMoviesAPI().getCastCrew(movieID, Constants.API_KEY);

        requestCastCrew.enqueue(new Callback<ResponseListCastAndCrew>() {
            @Override
            public void onResponse(Call<ResponseListCastAndCrew> call, Response<ResponseListCastAndCrew> response) {
                if (response.isSuccessful()) {
                    mResponseCastCrew = response;
                    setCastCrew();
                }
            }

            @Override
            public void onFailure(Call<ResponseListCastAndCrew> call, Throwable t) {
                Toast.makeText(getActivity(), "API response unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setCastCrew() {

        mCastCrewTextVIew.setVisibility(View.VISIBLE);
        mCastCrewRecyclerView.setVisibility(View.VISIBLE);

        //set director's name
        for (ResponseSingleCrew crew : mResponseCastCrew.body().getCrew()) {
            if (crew.getJob().equals("Director")) {
                mDirectorTextView.setText("Directed By:\n" + crew.getName());
                break;
            }
        }

        int min = Math.min(mResponseCastCrew.body().getCast().size(), 15);

        List<ResponseSingleCast> castList = new ArrayList<>();

        for (int i = 0; i < min; i++) {
            castList.add(mResponseCastCrew.body().getCast().get(i));
        }

        CastRecyclerViewAdapter adapter = new CastRecyclerViewAdapter(getActivity());
        mCastCrewRecyclerView.setAdapter(adapter);
        adapter.setRecyclerViewList(castList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mCastCrewRecyclerView.setLayoutManager(linearLayoutManager);

        if (castList.size() == 0) {
            mCastCrewTextVIew.setVisibility(View.GONE);
            mCastCrewRecyclerView.setVisibility(View.GONE);
        }

        dialogCast.dismiss();

    }

    private void callBackdropsAPI() {

        Call<ResponseListBackdropsAndPosters> requestMovieBackdrops = restAdapter.getMoviesAPI().getMovieBackdrops(movieID, Constants.API_KEY);

        requestMovieBackdrops.enqueue(new Callback<ResponseListBackdropsAndPosters>() {
            @Override
            public void onResponse(Call<ResponseListBackdropsAndPosters> call, Response<ResponseListBackdropsAndPosters> response) {
                if (response.isSuccessful()) {
                    mResponseBackdrops = response;
                    setMovieBackdrops();
                } else
                    Toast.makeText(getActivity(), "API response unsuccessful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseListBackdropsAndPosters> call, Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setMovieBackdrops() {

        int min = Math.min(mResponseBackdrops.body().getBackdrops().size(), 5);

        for (int i = 0; i < min; i++) {
            DefaultSliderView mSliderView = new DefaultSliderView(getActivity());
            mSliderView.image(Constants.IMAGE_PATH_PREFIX + mResponseBackdrops.body().getBackdrops().get(i).getFilePath());
            mImageSlider.addSlider(mSliderView);
        }

        mImageSlider.setDuration(2000);
        mImageSlider.setPresetTransformer(12);
        mImageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

    }

    private void callMovieDetailsAPI() {

        Call<ResponseSingleResult> requestMovieDetails = restAdapter.getMoviesAPI().getMovieDetails(movieID, Constants.API_KEY);

        requestMovieDetails.enqueue(new Callback<ResponseSingleResult>() {
            @Override
            public void onResponse(Call<ResponseSingleResult> call, Response<ResponseSingleResult> response) {
                if (response.isSuccessful()) {
                    mResponseDetails = response;
                    setMovieDetails();
                } else
                    Toast.makeText(getActivity(), "API response unsuccessful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseSingleResult> call, Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setMovieDetails() {

        mDurationTextView.setText(mResponseDetails.body().getRuntime() + " mins");
        mOverviewTextView.setText(mResponseDetails.body().getOverview());

        mGenresTextView.setText("");

        for (int i = 0; i < mResponseDetails.body().getGenres().size(); i++) {
            mGenresTextView.append(mResponseDetails.body().getGenres().get(i).getName());
            if (i != mResponseDetails.body().getGenres().size() - 1)
                mGenresTextView.append(" | ");
        }

        ((MainActivity) getActivity()).setToolbarText(mResponseDetails.body().getTitle());

        Picasso.with(getActivity()).load(Constants.IMAGE_PATH_PREFIX + mResponseDetails.body().getPosterPath()).placeholder(R.drawable.placeholder_no_image_available).fit().into(mMoviePosterImageView);

        String dateString = mResponseDetails.body().getReleaseDate();

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            mReleaseDateTextView.setText("Release Date:\n" + mResponseDetails.body().getReleaseDate());
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("dd MMM yyyy");
        mReleaseDateTextView.setText("Release Date:\n" + fmtOut.format(date));

        // TODO: 27-Aug-16 Get imdb_id from API and create icon to show movie in IMDB app using intent

        mRatingTextView.setText(mResponseDetails.body().getVoteAverage() + " / 10");

        dialogDetails.dismiss();
    }
}
