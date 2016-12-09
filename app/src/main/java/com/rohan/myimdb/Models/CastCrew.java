package com.rohan.myimdb.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rohan on 10-Dec-16.
 */

public class CastCrew implements Parcelable {

    String profilePath;
    String name;
    String id;
    String job;

    public CastCrew(String profilePath, String name, String id, String job) {
        this.profilePath = profilePath;
        this.name = name;
        this.id = id;
        this.job = job;
    }

    protected CastCrew(Parcel in) {
        profilePath = in.readString();
        name = in.readString();
        id = in.readString();
        job = in.readString();
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getJob() {
        return job;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public static final Creator<CastCrew> CREATOR = new Creator<CastCrew>() {
        @Override
        public CastCrew createFromParcel(Parcel in) {
            return new CastCrew(in);
        }

        @Override
        public CastCrew[] newArray(int size) {
            return new CastCrew[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(profilePath);
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeString(job);
    }
}
