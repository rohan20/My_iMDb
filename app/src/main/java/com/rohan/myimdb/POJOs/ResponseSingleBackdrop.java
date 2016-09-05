package com.rohan.myimdb.POJOs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSingleBackdrop {

    @SerializedName("file_path")
    @Expose
    private String filePath;

    /**
     *
     * @return
     * The filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     *
     * @param filePath
     * The file_path
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}