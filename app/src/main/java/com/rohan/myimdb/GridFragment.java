package com.rohan.myimdb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.rohan.myimdb.Adapters.MoviesGridRecyclerViewAdapter;
import com.rohan.myimdb.Models.Movie;
import com.rohan.myimdb.POJOs.ResponseComplete;
import com.rohan.myimdb.POJOs.ResponseListResults;
import com.rohan.myimdb.Utils.IOnMovieSelectedAdapter;
import com.rohan.myimdb.Utils.Constants;
import com.rohan.myimdb.Utils.IOnMovieSelected;
import com.rohan.myimdb.Utils.RESTAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class GridFragment extends Fragment implements IOnMovieSelectedAdapter {

    private Bundle mBundleRecyclerViewState;
    private RESTAdapter retrofitAdapter;
    private IOnMovieSelected mOnMovieSelectedListener;

    private RecyclerView mRecyclerView;
    private MoviesGridRecyclerViewAdapter mGridAdapter;

    private ArrayList<Movie> mMostPopularMoviesList;
    private ArrayList<Movie> mHighestRatedMoviesList;
    private ArrayList<Movie> mFavouritesMoviesList;

    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;

    Toolbar mToolbar;
    ProgressDialog dialog;
    ImageView noFavouritesImage;

    Menu mMenu;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnMovieSelectedListener = (IOnMovieSelected) context;
        } catch (ClassCastException e) {
            Log.i("ClassCastException", "MainActivity must implement IOnMovieSelected");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnMovieSelectedListener = null;
    }

    public GridFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movies_grid, container, false);

        mMostPopularMoviesList = new ArrayList<>();
        mHighestRatedMoviesList = new ArrayList<>();
        mFavouritesMoviesList = new ArrayList<>();

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();

        init(rootView);

        retrofitAdapter = new RESTAdapter(Constants.MOVIES_DB_BASE_URL);

        if (savedInstanceState == null) {
            dialog.show();
            callApi();
        } else {
            mMostPopularMoviesList = savedInstanceState.getParcelableArrayList(Constants.MOST_POPULAR_MOVIES_LIST_KEY);
            mHighestRatedMoviesList = savedInstanceState.getParcelableArrayList(Constants.HIGHEST_RATED_MOVIES_LIST_KEY);
            mFavouritesMoviesList = savedInstanceState.getParcelableArrayList(Constants.FAVOURITES_MOVIES_LIST_KEY);
            handleOptionsItemSelected(preferences.getInt(Constants.SELECTED_MENU_ITEM, R.id.action_popular_movies));
        }

        return rootView;
    }

    private void init(View v) {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching movies...");

        mRecyclerView = (RecyclerView) v.findViewById(R.id.movies_grid_recycler_view);
        noFavouritesImage = (ImageView) v.findViewById(R.id.no_favourites_present_image);

        mRecyclerView.setVisibility(View.VISIBLE);
        noFavouritesImage.setVisibility(View.GONE);

        mGridAdapter = new MoviesGridRecyclerViewAdapter(getActivity(), this);
        handleOptionsItemSelected(preferences.getInt(Constants.SELECTED_MENU_ITEM, R.id.action_popular_movies));

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
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
                    handleOptionsItemSelected(preferences.getInt(Constants.SELECTED_MENU_ITEM, R.id.action_popular_movies));
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

            movie.setId(result.getId().toString());
            movie.setTitle(result.getTitle());
            movie.setPosterPath(result.getPosterPath());
            movie.setOverview(result.getOverview());
            movie.setVoteAverage(result.getVoteAverage().toString());
            movie.setReleaseDate(result.getReleaseDate());

            //optional
            movie.setBackdropPath(result.getBackdropPath());
            movie.setGenreIds(result.getGenreIds());

            moviesList.add(movie);
        }

        return moviesList;
    }

    @Override
    public void onMovieClickedCallback() {
        mOnMovieSelectedListener.movieSelected();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        mMenu = menu;
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(preferences.getInt(Constants.SELECTED_MENU_ITEM, R.id.action_popular_movies));
        menuItem.setChecked(true);
    }

    private void handleOptionsItemSelected(int itemID) {

        if (mMenu != null) {
            MenuItem item = mMenu.findItem(itemID);
            item.setChecked(true);
        }

        switch (itemID) {
            case R.id.action_highest_rated:
                noFavouritesImage.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

                preferencesEditor.putInt(Constants.SELECTED_MENU_ITEM, R.id.action_highest_rated).apply();
                mGridAdapter.setRecyclerViewList(mHighestRatedMoviesList);
                break;

            case R.id.action_favourites:
                preferencesEditor.putInt(Constants.SELECTED_MENU_ITEM, R.id.action_favourites).apply();
                fetchFavouritesFromDatabase();
                break;

            default:
                noFavouritesImage.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                preferencesEditor.putInt(Constants.SELECTED_MENU_ITEM, R.id.action_popular_movies).apply();
                mGridAdapter.setRecyclerViewList(mMostPopularMoviesList);
        }
    }

    private void fetchFavouritesFromDatabase() {
        noFavouritesImage.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        mFavouritesMoviesList.clear();
        List<String> favouritesIDList = new DBHelper(getActivity()).getAllFavourites();

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

        if (preferences.getInt(Constants.SELECTED_MENU_ITEM, R.id.action_popular_movies) == R.id.action_favourites) {
            setFavourites();
        }

    }

    private void setFavourites() {
        mGridAdapter.setRecyclerViewList(mFavouritesMoviesList);

        if (mFavouritesMoviesList.size() == 0) {
            noFavouritesImage.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        item.setChecked(true);
        handleOptionsItemSelected(item.getItemId());

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(Constants.MOST_POPULAR_MOVIES_LIST_KEY, mMostPopularMoviesList);
        outState.putParcelableArrayList(Constants.HIGHEST_RATED_MOVIES_LIST_KEY, mHighestRatedMoviesList);
        outState.putParcelableArrayList(Constants.FAVOURITES_MOVIES_LIST_KEY, mFavouritesMoviesList);
    }

    public void updateFavouritesGrid() {
        if (mFavouritesMoviesList != null) {
            fetchFavouritesFromDatabase();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(Constants.RECYCLER_VIEW_STATE_KEY, listState);
    }

    @Override
    public void onResume() {
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(Constants.RECYCLER_VIEW_STATE_KEY);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}