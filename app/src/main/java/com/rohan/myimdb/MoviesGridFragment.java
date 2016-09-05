package com.rohan.myimdb;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

// TODO: 18-Jul-16 Movies don't come up initally unless overflowMenuItem/refreshButton is clicked
// TODO: 18-Jul-16 Popular Movies in overflow menu isn't checked on first run

public class MoviesGridFragment extends Fragment implements AdapterCallback {

    private RESTAdapter retrofitAdapter;
    private OnMovieSelected onMovieSelectedListener;
    private retrofit2.Response<ResponseComplete> mResponse;

    private RecyclerView mRecyclerView;
    private MoviesGridRecyclerViewAdapter mGridAdapter;
    private GridLayoutManager mGridLayoutManager;

    private List<Movie> mMoviesList;
    private List<Movie> mMostPopularMoviesList;
    private List<Movie> mHighestRatedMoviesList;
    private List<Movie> mFavouritesMoviesList;

    ProgressDialog dialog;
    View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onMovieSelectedListener = (OnMovieSelected) context;
        } catch (ClassCastException e) {
            Log.i("ClassCastException", "MainActivity must implement OnMovieSelected");
        }

    }

    public MoviesGridFragment() {
        // Required empty public constructor
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

        ((MainActivity) getActivity()).hideBackButton();
        ((MainActivity) getActivity()).setToolbarText("My iMDb", 20);

        rootView = getView();
        setHasOptionsMenu(true);
        init(rootView);

        retrofitAdapter = new RESTAdapter(Constants.MOVIES_DB_BASE_URL);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching movies...");
        dialog.show();
        callApi();
    }

    private void init(View v) {

        mMoviesList = new ArrayList<>();
        mMostPopularMoviesList = new ArrayList<>();
        mHighestRatedMoviesList = new ArrayList<>();
        mFavouritesMoviesList = new ArrayList<>();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.movies_grid_recycler_view);
        mGridAdapter = new MoviesGridRecyclerViewAdapter(getActivity(), this);
        mGridAdapter.setRecyclerViewList(mMoviesList);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setAdapter(mGridAdapter);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
    }

    private void callApi() {

        Call<ResponseComplete> requestMostPopular = retrofitAdapter.getMoviesAPI().getMoviesList(Constants.MOST_POPULAR, Constants.API_KEY);
        Call<ResponseComplete> requestHighestRated = retrofitAdapter.getMoviesAPI().getMoviesList(Constants.HIGHEST_RATED, Constants.API_KEY);

        requestMostPopular.enqueue(new Callback<ResponseComplete>() {
            @Override
            public void onResponse(Call<ResponseComplete> call, retrofit2.Response<ResponseComplete> response) {

                if (response.isSuccessful()) {
                    mMoviesList.clear();
                    mMostPopularMoviesList.addAll(setMoviesList(response));
                    mMoviesList.addAll(mMostPopularMoviesList);
                    mGridAdapter.notifyDataSetChanged();

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
                    mMoviesList.clear();
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

        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle b = new Bundle();
        b.putString(Constants.MOVIE_ID, movieID);
        fragment.setArguments(b);

//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container_grid, fragment)
//                .addToBackStack(null)
//                .commit();

        if (getActivity().findViewById(R.id.container_details) == null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_grid, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_details, fragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    public interface OnMovieSelected {
        void movieSelected();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        mMoviesList.clear();
        item.setChecked(true);

        switch (item.getItemId()) {

            case R.id.action_popular_movies:
                ((MainActivity) getActivity()).setToolbarText("My iMDb", 20);
                mMoviesList.addAll(mMostPopularMoviesList);
                mGridAdapter.notifyDataSetChanged();
                return true;

            case R.id.action_highest_rated:
                ((MainActivity) getActivity()).setToolbarText("My iMDb", 20);
                mMoviesList.addAll(mHighestRatedMoviesList);
                mGridAdapter.notifyDataSetChanged();
                return true;

            case R.id.action_favourites:
                ((MainActivity) getActivity()).setToolbarText("My iMDb", 20);
                mMoviesList.addAll(mFavouritesMoviesList);
                mGridAdapter.notifyDataSetChanged();
                return true;

            case R.id.actiom_refresh:
                ((MainActivity) getActivity()).setToolbarText("My iMDb", 20);
                dialog.show();
                callApi();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
