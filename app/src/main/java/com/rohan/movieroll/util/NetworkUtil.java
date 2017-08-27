package com.rohan.movieroll.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by rohantaneja on 27-Aug-2017
 */

public class NetworkUtil {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
