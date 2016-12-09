package com.rohan.myimdb.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rohan on 10-Dec-16.
 */

public class Backdrop implements Parcelable {

    String filePath;

    public Backdrop(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    protected Backdrop(Parcel in) {
        filePath = in.readString();
    }

    public static final Creator<Backdrop> CREATOR = new Creator<Backdrop>() {
        @Override
        public Backdrop createFromParcel(Parcel in) {
            return new Backdrop(in);
        }

        @Override
        public Backdrop[] newArray(int size) {
            return new Backdrop[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(filePath);
    }
}
