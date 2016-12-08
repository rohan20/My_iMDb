package com.rohan.myimdb.Utils;

import com.rohan.myimdb.BuildConfig;

/**
 * Created by Rohan on 17-Jul-16.
 */
public class Constants {

    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String MOVIES_DB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String MOST_POPULAR = "popular";
    public static final String MOVIE_ID = "movie_id";
    public static final String HIGHEST_RATED = "top_rated";
    public static final String IMAGE_PATH_PREFIX = "http://image.tmdb.org/t/p/w342";
    public static final String YOUTUBE_VIDEO_PREFIX = "https://www.youtube.com/watch?v=";
    public static final String YOUTUBE_THUMBNAIL_PREFIX = "http://img.youtube.com/vi/";
    public static final String YOUTUBE_THUMBNAIL_SUFFIX = "/default.jpg";

    public final static String SELECTED_MENU_ITEM = "menu_item_id";

    public static final String MOST_POPULAR_MOVIES_LIST_KEY = "most_popular_key";
    public static final String HIGHEST_RATED_MOVIES_LIST_KEY = "highest_rated_key";
    public static final String FAVOURITES_MOVIES_LIST_KEY = "favourites_key";

}
