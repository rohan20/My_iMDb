package com.rohan.myimdb.POJOs;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseListReviews {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("results")
    @Expose
    private List<ResponseSingleReview> results = new ArrayList<>();
    @SerializedName("total_results")
    @Expose
    private Long totalResults;

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
     * The results
     */
    public List<ResponseSingleReview> getReviews() {
        return results;
    }


    /**
     *
     * @return
     * The totalResults
     */
    public Long getTotalResults() {
        return totalResults;
    }

    /**
     *
     * @param totalResults
     * The total_results
     */
    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

}