package com.rohan.myimdb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import retrofit2.Call;
import retrofit2.Callback;

public class MoviesGridFragment extends Fragment implements AdapterCallback {


    private RESTAdapter retrofitAdapter;
    private OnMovieSelected onMovieSelectedListener;
    private retrofit2.Response<ResponseComplete> mResponse;

    private RecyclerView mRecyclerView;
    private MoviesGridRecyclerViewAdapter mGridAdapter;
    private GridLayoutManager mGridLayoutManager;

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

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
        preferences.getInt(Constants.SELECTED_MENU_ITEM, -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movies_grid, container, false);

        mMostPopularMoviesList = new ArrayList<>();
        mHighestRatedMoviesList = new ArrayList<>();
        mFavouritesMoviesList = new ArrayList<>();

        init(rootView);

        retrofitAdapter = new RESTAdapter(Constants.MOVIES_DB_BASE_URL);

        if (savedInstanceState == null) {
            dialog.show();
            callApi();
        } else {
            mMostPopularMoviesList = savedInstanceState.getParcelableArrayList(Constants.MOST_POPULAR_MOVIES_LIST_KEY);
            mHighestRatedMoviesList = savedInstanceState.getParcelableArrayList(Constants.HIGHEST_RATED_MOVIES_LIST_KEY);
            mFavouritesMoviesList = savedInstanceState.getParcelableArrayList(Constants.FAVOURITES_MOVIES_LIST_KEY);
            handleOptionsItemSelected(preferences.getInt(Constants.SELECTED_MENU_ITEM, -1));
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
//        mGridAdapter.setRecyclerViewList(mMostPopularMoviesList);

        handleOptionsItemSelected(preferences.getInt(Constants.SELECTED_MENU_ITEM, -1));

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
                    handleOptionsItemSelected(preferences.getInt(Constants.SELECTED_MENU_ITEM, -1));
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
    public void onMovieClickedCallback(String movieID) {
        onMovieSelectedListener.movieSelected(movieID);
    }

    public interface OnMovieSelected {
        void movieSelected(String id);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        mMenu = menu;
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    private void handleOptionsItemSelected(int itemID) {

        if (mMenu != null && itemID != -1) {
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
                noFavouritesImage.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                preferencesEditor.putInt(Constants.SELECTED_MENU_ITEM, R.id.action_favourites).apply();

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

                mGridAdapter.setRecyclerViewList(mFavouritesMoviesList);

                if (mFavouritesMoviesList.size() == 0) {
                    noFavouritesImage.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }

            default:
                noFavouritesImage.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                preferencesEditor.putInt(Constants.SELECTED_MENU_ITEM, R.id.action_popular_movies).apply();
                mGridAdapter.setRecyclerViewList(mMostPopularMoviesList);
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
}
