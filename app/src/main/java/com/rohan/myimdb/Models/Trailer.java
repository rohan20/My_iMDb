package com.rohan.myimdb.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rohan on 17-Jul-16.
 */
public class Trailer implements Parcelable {

    String id;
    String key;
    String name;
    String type;

    public Trailer(String id, String key, String name, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.type = type;
    }

    protected Trailer(Parcel in) {
         id = in.readString();
        key = in.readString();
        name = in.readString();
        type = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(type);
    }
}
