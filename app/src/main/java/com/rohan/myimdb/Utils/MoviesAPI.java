package com.rohan.myimdb.Utils;

import com.rohan.myimdb.POJOs.ResponseComplete;
import com.rohan.myimdb.POJOs.ResponseListBackdropsAndPosters;
import com.rohan.myimdb.POJOs.ResponseListCastAndCrew;
import com.rohan.myimdb.POJOs.ResponseListReviews;
import com.rohan.myimdb.POJOs.ResponseListTrailer;
import com.rohan.myimdb.POJOs.ResponseSingleResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Rohan on 17-Jul-16.
 */
public interface MoviesAPI {

    //get most_popular/top_rated movies
    @GET("{type}")
    Call<ResponseComplete> getMoviesList(@Path("type") String type, @Query("api_key") String api_key);

    //get movie details
    @GET("{movie_id}")
    Call<ResponseSingleResult> getMovieDetails(@Path("movie_id") String movieID, @Query("api_key") String api_key);

    //get backdrop images
    @GET("{movie_id}/images")
    Call<ResponseListBackdropsAndPosters> getMovieBackdrops(@Path("movie_id") String movieID, @Query("api_key") String api_key);

    //get cast and crew
    @GET("{movie_id}/credits")
    Call<ResponseListCastAndCrew> getCastCrew(@Path("movie_id") String movieID, @Query("api_key") String api_key);

    //get reviews
    @GET("{movie_id}/reviews")
    Call<ResponseListReviews> getReviews(@Path("movie_id") String movieID, @Query("api_key") String api_key);

    //get trailers
    @GET("{movie_id}/videos")
    Call<ResponseListTrailer> getTrailers(@Path("movie_id") String movieID, @Query("api_key") String api_key);

    //get similar movies
    @GET("{movie_id}/similar")
    Call<ResponseComplete> getSimilarMovies(@Path("movie_id") String movieID, @Query("api_key") String api_key);


}
