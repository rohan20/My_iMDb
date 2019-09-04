package com.rohan.movieroll.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.rohan.movieroll.DBHelper;
import com.rohan.movieroll.R;
import com.rohan.movieroll.adapter.MoviesGridRecyclerViewAdapter;
import com.rohan.movieroll.model.Movie;
import com.rohan.movieroll.pojo.ResponseComplete;
import com.rohan.movieroll.pojo.ResponseListResults;
import com.rohan.movieroll.util.Constants;
import com.rohan.movieroll.util.IOnMovieSelected;
import com.rohan.movieroll.util.IOnMovieSelectedAdapter;
import com.rohan.movieroll.util.NetworkUtil;
import com.rohan.movieroll.util.RESTAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;

public class GridFragment extends Fragment implements IOnMovieSelectedAdapter {

    static final int AD_AFTER_NUMBER_OF_ITEMS = 4, VOICE_RECOGNITION_REQUEST_CODE = 13;

    private Bundle mBundleRecyclerViewState;
    private RESTAdapter retrofitAdapter;
    private IOnMovieSelected mOnMovieSelectedListener;

    private RelativeLayout mRelativeLayout;
    private RecyclerView mRecyclerView;
    private MoviesGridRecyclerViewAdapter mGridAdapter;

    private ArrayList<Movie> mMostPopularMoviesList;
    private ArrayList<Movie> mHighestRatedMoviesList;
    private ArrayList<Movie> mFavouritesMoviesList;
    private ArrayList<Movie> mSearchedMoviesList;

    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;

    MaterialSearchView searchView;
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

        mRelativeLayout = rootView.findViewById(R.id.parent_grid_relative_layout);

        mMostPopularMoviesList = new ArrayList<>();
        mHighestRatedMoviesList = new ArrayList<>();
        mFavouritesMoviesList = new ArrayList<>();
        mSearchedMoviesList = new ArrayList<>();

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

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mRecyclerView.smoothScrollToPosition(0);
                fetchSearchedMovies(query);
                Toast.makeText(getContext(), getContext().getString(R.string.searching_for) + " " + query + "...", Toast.LENGTH_SHORT).show();
                searchView.closeSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return rootView;
    }

    private void init(View v) {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.fetching_movies));

        mRecyclerView = (RecyclerView) v.findViewById(R.id.movies_grid_recycler_view);
        noFavouritesImage = (ImageView) v.findViewById(R.id.no_favourites_present_image);

        mRecyclerView.setVisibility(View.VISIBLE);
        noFavouritesImage.setVisibility(View.GONE);

        mGridAdapter = new MoviesGridRecyclerViewAdapter(getActivity(), this);
        handleOptionsItemSelected(preferences.getInt(Constants.SELECTED_MENU_ITEM, R.id.action_popular_movies));

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);

        GridLayoutManager.SpanSizeLookup onSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mGridAdapter.getItemViewType(position)) {
                    case MoviesGridRecyclerViewAdapter.AD_VIEW:
                        return 2;
                    default:
                        return 1;
                }
            }
        };

        mGridLayoutManager.setSpanSizeLookup(onSpanSizeLookup);
        mRecyclerView.setAdapter(mGridAdapter);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(23);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mToolbar = (Toolbar) v.findViewById(R.id.toolbar_movies_grid);
        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);

        searchView = v.findViewById(R.id.search_view);
    }

    private void callApi() {

        if (!NetworkUtil.isNetworkConnected(getActivity())) {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();

            Toast.makeText(getActivity(), getString(R.string.internet_error), Toast.LENGTH_LONG).show();
            return;
        }

        Call<ResponseComplete> requestMostPopular = retrofitAdapter.getMoviesAPI().getMoviesList(Constants.MOST_POPULAR, Constants.API_KEY);
        Call<ResponseComplete> requestHighestRated = retrofitAdapter.getMoviesAPI().getMoviesList(Constants.HIGHEST_RATED, Constants.API_KEY);

        requestMostPopular.enqueue(new Callback<ResponseComplete>() {
            @Override
            public void onResponse(Call<ResponseComplete> call, retrofit2.Response<ResponseComplete> response) {

                if (response.isSuccessful()) {
                    mMostPopularMoviesList.clear();
                    mMostPopularMoviesList.addAll(setMoviesList(response));

                    int size = mMostPopularMoviesList.size();
                    for (int position = 0; position < size; position++) {
                        if ((position + 1) % (AD_AFTER_NUMBER_OF_ITEMS + 1) == 0) {
                            mMostPopularMoviesList.add(position, new Movie("-1"));
                        }

                    }

                    handleOptionsItemSelected(preferences.getInt(Constants.SELECTED_MENU_ITEM, R.id.action_popular_movies));
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseComplete> call, Throwable t) {
//                Log.i("Message", t.getMessage());
            }
        });

        requestHighestRated.enqueue(new Callback<ResponseComplete>() {
            @Override
            public void onResponse(Call<ResponseComplete> call, retrofit2.Response<ResponseComplete> response) {

                if (response.isSuccessful()) {
                    mHighestRatedMoviesList.clear();
                    mHighestRatedMoviesList.addAll(setMoviesList(response));

                    int size = mHighestRatedMoviesList.size();
                    for (int position = 0; position < size; position++) {
                        if ((position + 1) % (AD_AFTER_NUMBER_OF_ITEMS + 1) == 0) {
                            mHighestRatedMoviesList.add(position, new Movie("-1"));
                        }

                    }

                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseComplete> call, Throwable t) {
//                Log.i("Message", t.getMessage());
            }
        });
    }

    private List<Movie> setMoviesList(retrofit2.Response<ResponseComplete> response) {

        List<Movie> moviesList = new ArrayList<>();

        if (response.body() != null) {
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

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView.setMenuItem(searchMenuItem);
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

            case R.id.action_other_apps:
                showFullScreenAd();
                Intent i = new Intent(getActivity(), OtherAppsActivity.class);
                startActivity(i);
                break;

            case R.id.action_voice_search:
                startVoiceRecognition();
                break;

            default:
                noFavouritesImage.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                preferencesEditor.putInt(Constants.SELECTED_MENU_ITEM, R.id.action_popular_movies).apply();
                mGridAdapter.setRecyclerViewList(mMostPopularMoviesList);
        }
    }

    private void showFullScreenAd() {
        final InterstitialAd interstitialAd = new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_id_full_screen));
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("5BBDB114D900920A2045F20FC8A733CE")  // MyOnePlus2
                .build();
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitialAd.show();
            }
        });
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

        int size = mFavouritesMoviesList.size();
        for (int position = 0; position < size; position++) {
            if ((position + 1) % (AD_AFTER_NUMBER_OF_ITEMS + 1) == 0) {
                mFavouritesMoviesList.add(position, new Movie("-1"));
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

    public void fetchSearchedMovies(String query) {
        Call<ResponseComplete> requestSearch = retrofitAdapter.getMoviesAPI().searchMovies(Constants.API_KEY, query);
        requestSearch.enqueue(new Callback<ResponseComplete>() {
            @Override
            public void onResponse(Call<ResponseComplete> call, retrofit2.Response<ResponseComplete> response) {

                if (response.isSuccessful()) {
                    mSearchedMoviesList.clear();
                    mSearchedMoviesList.addAll(setMoviesList(response));

                    int size = mSearchedMoviesList.size();
                    for (int position = 0; position < size; position++) {
                        if ((position + 1) % (AD_AFTER_NUMBER_OF_ITEMS + 1) == 0) {
                            mSearchedMoviesList.add(position, new Movie("-1"));
                        }

                    }

                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                    noFavouritesImage.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mGridAdapter.setRecyclerViewList(mSearchedMoviesList);
                } else {
                    // Log.d("RESPONSE UNSUCCESSFULL", "Couldn't search movies");
                }

            }

            @Override
            public void onFailure(Call<ResponseComplete> call, Throwable t) {
                // Log.i("Message", t.getMessage());
            }
        });
    }

    public void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getContext().getString(R.string.search));
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null) {
                if(!matches.isEmpty()) {
                    String query = matches.get(0);
                    fetchSearchedMovies(query);
                    Toast.makeText(getContext(), getContext().getString(R.string.searching_for) + " " + query + "...", Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}