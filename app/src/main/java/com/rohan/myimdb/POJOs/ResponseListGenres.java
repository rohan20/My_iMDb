package com.rohan.myimdb.POJOs;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseListGenres {

    @SerializedName("genres")
    @Expose
    private List<ResponseSingleGenre> genres = new ArrayList<>();

    /**
     *
     * @return
     * The genres
     */
    public List<ResponseSingleGenre> getGenres() {
        return genres;
    }

    /**
     *
     * @param genres
     * The genres
     */
    public void setGenres(List<ResponseSingleGenre> genres) {
        this.genres = genres;
    }

}