package com.example.disen.chefu.network;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by disen on 4/15/2018.
 */

public class NetworkUtil {
    public static int CONNECTED = 1;
    public static int NOT_CONNECTED = 0;
    public static String not_connected = "Not connected to Internet";
    public static String connected = "connected to Internet";
    public static int getConnectivityStatus(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(null != networkInfo){
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                return CONNECTED;}
            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                return CONNECTED;
            }
        }
        return NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.CONNECTED) {
            status = connected;
        }
        else if(conn == NetworkUtil.NOT_CONNECTED) {
            status = not_connected;
        }
        return status;
    }
}
