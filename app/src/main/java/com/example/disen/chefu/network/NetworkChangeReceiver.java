package com.example.disen.chefu.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by disen on 4/15/2018.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    public static ConnectionReceiverListener connectionReceiverListener;
    public NetworkChangeReceiver(){
        super();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if (connectionReceiverListener != null) {
            connectionReceiverListener.onNetworkConnectionChanged(status);
        }
    }


    public interface ConnectionReceiverListener {
        void onNetworkConnectionChanged(String status);
    }
}
