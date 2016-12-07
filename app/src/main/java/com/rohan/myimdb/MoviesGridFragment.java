package com.rohan.myimdb;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.rohan.myimdb.Adapters.MoviesGridRecyclerViewAdapter;
import com.rohan.myimdb.Models.Movie;
import com.rohan.myimdb.POJOs.ResponseComplete;
import com.rohan.myimdb.POJOs.ResponseListResults;
import com.rohan.myimdb.Utils.AdapterCallback;
import com.rohan.myimdb.Utils.Constants;
import com.rohan.myimdb.Utils.RESTAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;

public class MoviesGridFragment extends Fragment implements AdapterCallback {


    private RESTAdapter retrofitAdapter;
    private OnMovieSelected onMovieSelectedListener;
    private retrofit2.Response<ResponseComplete> mResponse;

    private RecyclerView mRecyclerView;
    private MoviesGridRecyclerViewAdapter mGridAdapter;
    private GridLayoutManager mGridLayoutManager;

    private List<Movie> mMostPopularMoviesList;
    private List<Movie> mHighestRatedMoviesList;
    private List<Movie> mFavouritesMoviesList;

    Toolbar mToolbar;
    ProgressDialog dialog;
    View rootView;
    ImageView noFavouritesImage;

    Bundle mSavedInstanceState;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onMovieSelectedListener = (OnMovieSelected) context;
        } catch (ClassCastException e) {
            Log.i("ClassCastException", "MainActivity must implement OnMovieSelected");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        onMovieSelectedListener = null;
    }

    public MoviesGridFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies_grid, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        rootView = getView();
        init(rootView);

        retrofitAdapter = new RESTAdapter(Constants.MOVIES_DB_BASE_URL);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching movies...");
        dialog.show();
        callApi();
    }

    private void init(View v) {

        mMostPopularMoviesList = new ArrayList<>();
        mHighestRatedMoviesList = new ArrayList<>();
        mFavouritesMoviesList = new ArrayList<>();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.movies_grid_recycler_view);
        noFavouritesImage = (ImageView) v.findViewById(R.id.no_favourites_present_image);

        mRecyclerView.setVisibility(View.VISIBLE);
        noFavouritesImage.setVisibility(View.GONE);

        mGridAdapter = new MoviesGridRecyclerViewAdapter(getActivity(), this);
        mGridAdapter.setRecyclerViewList(mMostPopularMoviesList);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setAdapter(mGridAdapter);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mToolbar = (Toolbar) v.findViewById(R.id.toolbar_movies_grid);
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);
    }

    private void callApi() {

        Call<ResponseComplete> requestMostPopular = retrofitAdapter.getMoviesAPI().getMoviesList(Constants.MOST_POPULAR, Constants.API_KEY);
        Call<ResponseComplete> requestHighestRated = retrofitAdapter.getMoviesAPI().getMoviesList(Constants.HIGHEST_RATED, Constants.API_KEY);

        requestMostPopular.enqueue(new Callback<ResponseComplete>() {
            @Override
            public void onResponse(Call<ResponseComplete> call, retrofit2.Response<ResponseComplete> response) {

                if (response.isSuccessful()) {
                    mMostPopularMoviesList.clear();
                    mMostPopularMoviesList.addAll(setMoviesList(response));
                    mGridAdapter.setRecyclerViewList(mMostPopularMoviesList);

                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseComplete> call, Throwable t) {
                Log.i("Message", t.getMessage());
            }
        });

        requestHighestRated.enqueue(new Callback<ResponseComplete>() {
            @Override
            public void onResponse(Call<ResponseComplete> call, retrofit2.Response<ResponseComplete> response) {

                if (response.isSuccessful()) {
                    mHighestRatedMoviesList.clear();
                    mHighestRatedMoviesList.addAll(setMoviesList(response));

                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseComplete> call, Throwable t) {
                Log.i("Message", t.getMessage());
            }
        });
    }

    private List<Movie> setMoviesList(retrofit2.Response<ResponseComplete> response) {

        List<Movie> moviesList = new ArrayList<>();

        for (ResponseListResults result : response.body().getResults()) {
            Movie movie = new Movie();

            movie.setId(result.getId());
            movie.setTitle(result.getTitle());
            movie.setPosterPath(result.getPosterPath());
            movie.setOverview(result.getOverview());
            movie.setVoteAverage(result.getVoteAverage());
            movie.setReleaseDate(result.getReleaseDate());

            movie.setFavourite(false);

            //optional
            movie.setBackdropPath(result.getBackdropPath());
            movie.setGenreIds(result.getGenreIds());

            moviesList.add(movie);
        }

        return moviesList;
    }

    @Override
    public void onMovieClickedCallback(String movieID) {
        onMovieSelectedListener.movieSelected(movieID);
    }

    public interface OnMovieSelected {
        void movieSelected(String id);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        item.setChecked(true);
        noFavouritesImage.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        switch (item.getItemId()) {

            case R.id.action_popular_movies:
                mGridAdapter.setRecyclerViewList(mMostPopularMoviesList);
                return true;

            case R.id.action_highest_rated:
                mGridAdapter.setRecyclerViewList(mHighestRatedMoviesList);
                return true;

            case R.id.action_favourites:

                mFavouritesMoviesList.clear();
                List<Long> favouritesIDList = new DBHelper(getActivity()).getAllFavourites();

                for (Movie movie : mMostPopularMoviesList) {
                    if (favouritesIDList.contains(movie.getId())) {
                        mFavouritesMoviesList.add(movie);
                    }
                }

                for (Movie movie : mHighestRatedMoviesList) {
                    if (favouritesIDList.contains(movie.getId())) {
                        mFavouritesMoviesList.add(movie);
                    }
                }

                mGridAdapter.setRecyclerViewList(mFavouritesMoviesList);

                if (mFavouritesMoviesList.size() == 0) {
                    noFavouritesImage.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
