package com.rohan.myimdb.POJOs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSingleCast {

    @SerializedName("cast_id")
    @Expose
    private Long castId;
    @SerializedName("character")
    @Expose
    private String character;
    @SerializedName("credit_id")
    @Expose
    private String creditId;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("order")
    @Expose
    private Long order;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    /**
     *
     * @return
     * The castId
     */
    public Long getCastId() {
        return castId;
    }

    /**
     *
     * @param castId
     * The cast_id
     */
    public void setCastId(Long castId) {
        this.castId = castId;
    }

    /**
     *
     * @return
     * The character
     */
    public String getCharacter() {
        return character;
    }

    /**
     *
     * @param character
     * The character
     */
    public void setCharacter(String character) {
        this.character = character;
    }

    /**
     *
     * @return
     * The creditId
     */
    public String getCreditId() {
        return creditId;
    }

    /**
     *
     * @param creditId
     * The credit_id
     */
    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

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
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The order
     */
    public Long getOrder() {
        return order;
    }

    /**
     *
     * @param order
     * The order
     */
    public void setOrder(Long order) {
        this.order = order;
    }

    /**
     *
     * @return
     * The profilePath
     */
    public String getProfilePath() {
        return profilePath;
    }

    /**
     *
     * @param profilePath
     * The profile_path
     */
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

}