package com.rohan.myimdb.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rohan on 17-Jul-16.
 */
public class Review implements Parcelable {

    String id;
    String author;
    String content;

    public Review(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    protected Review(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(content);
    }
}
