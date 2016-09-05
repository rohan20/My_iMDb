package com.rohan.myimdb.POJOs;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseListCastAndCrew {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("cast")
    @Expose
    private List<ResponseSingleCast> cast = new ArrayList<>();
    @SerializedName("crew")
    @Expose
    private List<ResponseSingleCrew> crew = new ArrayList<>();

    /**
     *
     * @return
     * The id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The cast
     */
    public List<ResponseSingleCast> getCast() {
        return cast;
    }

    /**
     *
     * @param cast
     * The cast
     */
    public void setCast(List<ResponseSingleCast> cast) {
        this.cast = cast;
    }

    /**
     *
     * @return
     * The crew
     */
    public List<ResponseSingleCrew> getCrew() {
        return crew;
    }

    /**
     *
     * @param crew
     * The crew
     */
    public void setCrew(List<ResponseSingleCrew> crew) {
        this.crew = crew;
    }

}