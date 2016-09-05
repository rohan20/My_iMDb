package com.rohan.myimdb.POJOs;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseComplete {

    @SerializedName("page")
    @Expose
    private Long page;
    @SerializedName("results")
    @Expose
    private List<ResponseListResults> results = new ArrayList<ResponseListResults>();
    @SerializedName("total_results")
    @Expose
    private Long totalResults;
    @SerializedName("total_pages")
    @Expose
    private Long totalPages;

    /**
     * @return The page
     */
    public Long getPage() {
        return page;
    }

    /**
     * @return The results
     */
    public List<ResponseListResults> getResults() {
        return results;
    }

    /**
     * @return The totalResults
     */
    public Long getTotalResults() {
        return totalResults;
    }

    /**
     * @return The totalPages
     */
    public Long getTotalPages() {
        return totalPages;
    }

}