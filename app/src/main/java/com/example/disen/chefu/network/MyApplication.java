package com.example.disen.chefu.network;

import android.app.Application;

/**
 * Created by disen on 4/15/2018.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(NetworkChangeReceiver.ConnectionReceiverListener listener) {
        NetworkChangeReceiver.connectionReceiverListener = listener;
    }
}
