package com.rohan.myimdb;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.rohan.myimdb.Adapters.CastRecyclerViewAdapter;
import com.rohan.myimdb.Adapters.ReviewsRecyclerViewAdapter;
import com.rohan.myimdb.Adapters.SimilarMoviesRecyclerViewAdapter;
import com.rohan.myimdb.Adapters.TrailersRecyclerViewAdapter;
import com.rohan.myimdb.Models.Backdrop;
import com.rohan.myimdb.Models.CastCrew;
import com.rohan.myimdb.Models.Movie;
import com.rohan.myimdb.Models.Review;
import com.rohan.myimdb.Models.Trailer;
import com.rohan.myimdb.POJOs.ResponseComplete;
import com.rohan.myimdb.POJOs.ResponseListBackdropsAndPosters;
import com.rohan.myimdb.POJOs.ResponseListCastAndCrew;
import com.rohan.myimdb.POJOs.ResponseListResults;
import com.rohan.myimdb.POJOs.ResponseListReviews;
import com.rohan.myimdb.POJOs.ResponseListTrailer;
import com.rohan.myimdb.POJOs.ResponseSingleBackdrop;
import com.rohan.myimdb.POJOs.ResponseSingleCast;
import com.rohan.myimdb.POJOs.ResponseSingleCrew;
import com.rohan.myimdb.POJOs.ResponseSingleGenre;
import com.rohan.myimdb.POJOs.ResponseSingleResult;
import com.rohan.myimdb.POJOs.ResponseSingleReview;
import com.rohan.myimdb.POJOs.ResponseSingleTrailer;
import com.rohan.myimdb.Utils.Constants;
import com.rohan.myimdb.Utils.RESTAdapter;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.details_fragment_scroll_view)
    ScrollView mScrollView;
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
    @BindView(R.id.title_text_view)
    TextView mMovieTitleTextView;
    @BindView(R.id.add_to_favourites_button)
    SparkButton mFavouriteSparkButton;

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

    @BindView(R.id.parent_details_relative_layout)
    RelativeLayout mRelativeLayout;

    ProgressDialog dialogDetails;
    ProgressDialog dialogCast;
    ProgressDialog dialogReviews;
    ProgressDialog dialogTrailers;
    ProgressDialog dialogSimilarMovies;

    String movieID;
    RESTAdapter restAdapter;
    DBHelper dbHelper;

    ArrayList<Review> mReviewsList;
    ArrayList<Trailer> mTrailersList;
    ArrayList<Movie> mSimilarMoviesList;
    ArrayList<CastCrew> mCastCrewList;
    ArrayList<Backdrop> mBackdropList;
    Movie mCurrentMovie;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, v);

        dbHelper = new DBHelper(getActivity());
        mRelativeLayout.setVisibility(View.GONE);
        mFavouriteSparkButton.setOnClickListener(this);

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

        mReviewsList = new ArrayList<>();
        mTrailersList = new ArrayList<>();
        mSimilarMoviesList = new ArrayList<>();
        mCastCrewList = new ArrayList<>();
        mBackdropList = new ArrayList<>();

        callAPIs(savedInstanceState);

        return v;
    }

    private void callAPIs(Bundle savedInstanceState) {

        movieID = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SELECTED_ID, null);

        if (movieID == null)
            return;

        mRelativeLayout.setVisibility(View.VISIBLE);
        long id = Long.parseLong(movieID);

        restAdapter = new RESTAdapter(Constants.MOVIES_DB_BASE_URL);

        if (dbHelper.isFavourite(String.valueOf(id))) {
            mFavouriteSparkButton.setChecked(false);
        } else {
            mFavouriteSparkButton.setChecked(true);
        }

        if (savedInstanceState == null) {
            callMovieDetailsAPI();
            callReviewsAPI();
            callTrailersAPI();
            callSimilarMoviesAPI();
            callCastCrewAPI();
            callBackdropsAPI();
            return;
        }

        if (savedInstanceState.getParcelableArrayList(Constants.REVIEWS_LIST_KEY) != null) {
            mReviewsList = savedInstanceState.getParcelableArrayList(Constants.REVIEWS_LIST_KEY);
            setReviews();
        } else
            callReviewsAPI();

        if (savedInstanceState.getParcelableArrayList(Constants.TRAILERS_LIST_KEY) != null) {
            mTrailersList = savedInstanceState.getParcelableArrayList(Constants.TRAILERS_LIST_KEY);
            setTrailers();
        } else
            callTrailersAPI();

        if (savedInstanceState.getParcelableArrayList(Constants.SIMILAR_MOVIES_LIST_KEY) != null) {
            mSimilarMoviesList = savedInstanceState.getParcelableArrayList(Constants.SIMILAR_MOVIES_LIST_KEY);
            setSimilarMovies();
        } else
            callSimilarMoviesAPI();

        if (savedInstanceState.getParcelableArrayList(Constants.CAST_CREW_LIST_KEY) != null) {
            mCastCrewList = savedInstanceState.getParcelableArrayList(Constants.CAST_CREW_LIST_KEY);
            setCastCrew();
        } else
            callCastCrewAPI();

        if (savedInstanceState.getParcelableArrayList(Constants.BACKDROPS_LIST_KEY) != null) {
            mBackdropList = savedInstanceState.getParcelableArrayList(Constants.BACKDROPS_LIST_KEY);
            setMovieBackdrops();
        } else
            callBackdropsAPI();

        if (savedInstanceState.getParcelable(Constants.CURRENT_MOVIE) != null) {
            mCurrentMovie = savedInstanceState.getParcelable(Constants.CURRENT_MOVIE);
            setMovieDetails();
        } else
            callMovieDetailsAPI();

    }

    private void callSimilarMoviesAPI() {
        dialogSimilarMovies.show();

        Call<ResponseComplete> requestSimilarMovies = restAdapter.getMoviesAPI().getSimilarMovies(movieID, Constants.API_KEY);

        requestSimilarMovies.enqueue(new Callback<ResponseComplete>() {
            @Override
            public void onResponse(Call<ResponseComplete> call, Response<ResponseComplete> response) {
                if (response.isSuccessful()) {
                    for (ResponseListResults movie : response.body().getResults()) {
                        mSimilarMoviesList.add(new Movie(movie.getPosterPath(), movie.getOverview(), movie.getReleaseDate(), movie.getGenreIds(), movie.getId(), movie.getTitle(), movie.getBackdropPath(), movie.getVoteAverage()));
                    }
                    setSimilarMovies();
                }
                dialogSimilarMovies.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseComplete> call, Throwable t) {
                Toast.makeText(getActivity(), "Unable to fetch similar movies", Toast.LENGTH_SHORT).show();
                dialogSimilarMovies.dismiss();
            }
        });

    }

    private void setSimilarMovies() {

        mSimilarMoviesTextView.setVisibility(View.VISIBLE);
        mSimilarMoviesRecyclerView.setVisibility(View.VISIBLE);

        SimilarMoviesRecyclerViewAdapter similarMoviesAdapter = new SimilarMoviesRecyclerViewAdapter(getActivity());
        mSimilarMoviesRecyclerView.setAdapter(similarMoviesAdapter);
        similarMoviesAdapter.setRecyclerViewList(mSimilarMoviesList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.HORIZONTAL, false);
        mSimilarMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        if (mSimilarMoviesList.size() == 0) {
            mSimilarMoviesTextView.setVisibility(View.GONE);
            mSimilarMoviesRecyclerView.setVisibility(View.GONE);
        }

        dialogSimilarMovies.dismiss();
    }

    private void callTrailersAPI() {
        dialogTrailers.show();

        Call<ResponseListTrailer> requestTrailers = restAdapter.getMoviesAPI().getTrailers(movieID, Constants.API_KEY);

        requestTrailers.enqueue(new Callback<ResponseListTrailer>() {
            @Override
            public void onResponse(Call<ResponseListTrailer> call, Response<ResponseListTrailer> response) {
                if (response.isSuccessful()) {
                    for (ResponseSingleTrailer trailer : response.body().getTrailers()) {
                        mTrailersList.add(new Trailer(trailer.getId(), trailer.getKey(), trailer.getName(), trailer.getType()));
                    }
                    setTrailers();
                }
                dialogTrailers.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseListTrailer> call, Throwable t) {
                Toast.makeText(getActivity(), "Unable to fetch movie trailers", Toast.LENGTH_SHORT).show();
                dialogTrailers.dismiss();
            }
        });

    }

    private void setTrailers() {

        mTrailersTextView.setVisibility(View.VISIBLE);
        mTrailersRecyclerView.setVisibility(View.VISIBLE);

        TrailersRecyclerViewAdapter trailersAdapter = new TrailersRecyclerViewAdapter(getActivity());
        mTrailersRecyclerView.setAdapter(trailersAdapter);
        trailersAdapter.setTrailersList(mTrailersList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mTrailersRecyclerView.setLayoutManager(linearLayoutManager);

        if (mTrailersList.size() == 0) {
            mTrailersTextView.setVisibility(View.GONE);
            mTrailersRecyclerView.setVisibility(View.GONE);
        }

        dialogTrailers.dismiss();
    }

    private void callReviewsAPI() {
        dialogReviews.show();

        Call<ResponseListReviews> requestReviews = restAdapter.getMoviesAPI().getReviews(movieID, Constants.API_KEY);

        requestReviews.enqueue(new Callback<ResponseListReviews>() {
            @Override
            public void onResponse(Call<ResponseListReviews> call, Response<ResponseListReviews> response) {
                if (response.isSuccessful()) {
                    for (ResponseSingleReview review : response.body().getReviews()) {
                        mReviewsList.add(new Review(review.getId(), review.getAuthor(), review.getContent()));
                    }
                    setReviews();
                }
                dialogReviews.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseListReviews> call, Throwable t) {
                Toast.makeText(getActivity(), "Unable to fetch movie reviews", Toast.LENGTH_SHORT).show();
                dialogReviews.dismiss();
            }
        });

    }

    private void setReviews() {
        mReviewsTextView.setVisibility(View.VISIBLE);
        mReviewsRecyclerView.setVisibility(View.VISIBLE);

        ReviewsRecyclerViewAdapter reviewsAdapter = new ReviewsRecyclerViewAdapter(getActivity());
        mReviewsRecyclerView.setAdapter(reviewsAdapter);
        reviewsAdapter.setReviewsList(mReviewsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(layoutManager);


        if (mReviewsList.size() == 0) {
            mReviewsTextView.setVisibility(View.GONE);
            mReviewsRecyclerView.setVisibility(View.GONE);
        }

        dialogReviews.dismiss();
    }

    private void callCastCrewAPI() {
        dialogCast.show();

        Call<ResponseListCastAndCrew> requestCastCrew = restAdapter.getMoviesAPI().getCastCrew(movieID, Constants.API_KEY);

        requestCastCrew.enqueue(new Callback<ResponseListCastAndCrew>() {
            @Override
            public void onResponse(Call<ResponseListCastAndCrew> call, Response<ResponseListCastAndCrew> response) {
                if (response.isSuccessful()) {

                    List<ResponseSingleCast> castList = new ArrayList<>();

                    //for director's name
                    for (ResponseSingleCrew crew : response.body().getCrew()) {
                        if (crew.getJob().equals("Director")) {
                            mDirectorTextView.setText("Directed By:\n" + crew.getName());
                            break;
                        }

                        mCastCrewList.add(new CastCrew(null, crew.getName(), null, crew.getJob()));
                    }

                    int min = Math.min(response.body().getCast().size(), 15);
                    for (int i = 0; i < min; i++) {
                        castList.add(response.body().getCast().get(i));
                    }


                    for (ResponseSingleCast castItem : castList) {
                        mCastCrewList.add(new CastCrew(castItem.getProfilePath(), castItem.getName(), castItem.getId().toString(), null));
                    }

                    setCastCrew();
                }

                dialogCast.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseListCastAndCrew> call, Throwable t) {
                Toast.makeText(getActivity(), "Unable to fetch movie cast", Toast.LENGTH_SHORT).show();
                dialogCast.dismiss();
            }
        });

    }

    private void setCastCrew() {

        mCastCrewTextVIew.setVisibility(View.VISIBLE);
        mCastCrewRecyclerView.setVisibility(View.VISIBLE);

        //set director's name
        for (CastCrew castCrew : mCastCrewList) {
            if (castCrew.getJob() != null && castCrew.getJob().equals("Director")) {
                mDirectorTextView.setText("Directed By:\n" + castCrew.getName());
                break;
            }
        }

        CastRecyclerViewAdapter castCrewAdapter = new CastRecyclerViewAdapter(getActivity());
        mCastCrewRecyclerView.setAdapter(castCrewAdapter);
        castCrewAdapter.setRecyclerViewList(mCastCrewList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mCastCrewRecyclerView.setLayoutManager(linearLayoutManager);

        if (mCastCrewList.size() == 0) {
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

                    int min = Math.min(response.body().getBackdrops().size(), 5);
                    for (int i = 0; i < min; i++)
                        mBackdropList.add(new Backdrop(response.body().getBackdrops().get(i).getFilePath()));

                    setMovieBackdrops();
                }
            }

            @Override
            public void onFailure(Call<ResponseListBackdropsAndPosters> call, Throwable t) {
                Toast.makeText(getActivity(), "Unable to fetch movie backdrops", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setMovieBackdrops() {

        for (Backdrop backdrop : mBackdropList) {
            DefaultSliderView mSliderView = new DefaultSliderView(getActivity());
            mSliderView.image(Constants.IMAGE_PATH_PREFIX + backdrop.getFilePath());
            mImageSlider.addSlider(mSliderView);
        }

        mImageSlider.setDuration(2000);
        mImageSlider.setPresetTransformer(12);
        mImageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

    }

    private void callMovieDetailsAPI() {

        dialogDetails.show();

        Call<ResponseSingleResult> requestMovieDetails = restAdapter.getMoviesAPI().getMovieDetails(movieID, Constants.API_KEY);

        requestMovieDetails.enqueue(new Callback<ResponseSingleResult>() {
            @Override
            public void onResponse(Call<ResponseSingleResult> call, Response<ResponseSingleResult> response) {
                if (response.isSuccessful()) {
                    List<String> genresList = new ArrayList<>();
                    for (ResponseSingleGenre genre : response.body().getGenres()) {
                        genresList.add(genre.getName());
                    }

                    mCurrentMovie = new Movie(response.body().getPosterPath(), response.body().getOverview(), response.body().getReleaseDate(), genresList, response.body().getId().toString(), response.body().getTitle(), response.body().getBackdropPath(), response.body().getVoteAverage().toString(), response.body().getRuntime().toString());

                    setMovieDetails();
                }
                dialogDetails.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseSingleResult> call, Throwable t) {
                Toast.makeText(getActivity(), "Unable to fetch movie details", Toast.LENGTH_SHORT).show();
                dialogDetails.dismiss();
            }
        });

    }

    private void setMovieDetails() {

        mMovieTitleTextView.setText(mCurrentMovie.getTitle());

        mDurationTextView.setText(mCurrentMovie.getRuntime() + " mins");
        mOverviewTextView.setText(mCurrentMovie.getOverview());

        mGenresTextView.setText("");

        for (int i = 0; i < mCurrentMovie.getGenreIds().size(); i++) {
            mGenresTextView.append(mCurrentMovie.getGenreIds().get(i));
            if (i != mCurrentMovie.getGenreIds().size() - 1)
                mGenresTextView.append(" | ");
        }

        Picasso.with(getActivity()).load(Constants.IMAGE_PATH_PREFIX + mCurrentMovie.getPosterPath()).placeholder(R.drawable.placeholder_no_image_available).fit().into(mMoviePosterImageView);

        String dateString = mCurrentMovie.getReleaseDate();

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            mReleaseDateTextView.setText("Release Date:\n" + mCurrentMovie.getReleaseDate());
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("dd MMM yyyy");
        mReleaseDateTextView.setText("Release Date:\n" + fmtOut.format(date));

        mRatingTextView.setText(mCurrentMovie.getVoteAverage() + " / 10");

        dialogDetails.dismiss();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_to_favourites_button:
                long id = Long.parseLong(movieID);
                if (dbHelper.isFavourite(String.valueOf(id))) {
                    Toast.makeText(getActivity(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                    dbHelper.removeFromFavourites(String.valueOf(id));
                    mFavouriteSparkButton.setChecked(true);

                    if (getActivity() instanceof MainActivity)
                        ((MainActivity) getActivity()).favouritesUpdated();
                } else {
                    Toast.makeText(getActivity(), "Added to favourites", Toast.LENGTH_SHORT).show();
                    dbHelper.addToFavourites(String.valueOf(id));
                    mFavouriteSparkButton.setChecked(false);

                    if (getActivity() instanceof MainActivity)
                        ((MainActivity) getActivity()).favouritesUpdated();
                }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(Constants.REVIEWS_LIST_KEY, mReviewsList);
        outState.putParcelableArrayList(Constants.TRAILERS_LIST_KEY, mTrailersList);
        outState.putParcelableArrayList(Constants.SIMILAR_MOVIES_LIST_KEY, mSimilarMoviesList);
        outState.putParcelableArrayList(Constants.CAST_CREW_LIST_KEY, mCastCrewList);
        outState.putParcelableArrayList(Constants.BACKDROPS_LIST_KEY, mBackdropList);
        outState.putParcelable(Constants.CURRENT_MOVIE, mCurrentMovie);
    }

}
