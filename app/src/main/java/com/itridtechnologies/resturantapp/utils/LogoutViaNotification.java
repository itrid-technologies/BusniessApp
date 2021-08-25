package com.itridtechnologies.resturantapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.itridtechnologies.resturantapp.UiViews.Activities.MainActivity;
import com.itridtechnologies.resturantapp.notification.FCM;

public final class LogoutViaNotification {

    private static final String TAG = "Cannot invoke method length() on null object";
    private static BroadcastReceiver mRegistrationBroadcastReceiver;

    public static void logoutOnType(){

        // checking for type intent filter
        // gcm successfully registered
        // new push notification is received
        // new push notification is received
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    Log.e(TAG, "onCreate: " + FCM.deviceToken.getValue());

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {

                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    String type = intent.getStringExtra("type");

                    Log.e(TAG, "onReceive: " + type);

                    try {
                        if (type.equals("logout_user")) {
                            AppManager.intent(MainActivity.class);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onReceive: " + e.getMessage());
                    }


                    Log.e(TAG, "onReceive: " + message);

                }
            }
        };
    }

    public static void onResumeFun(){

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(Restaurant.getAppContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(Restaurant.getAppContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationsUtils.clearNotifications(Restaurant.getAppContext());
    }

    public static void onPauseFun(){

        LocalBroadcastManager.getInstance(Restaurant.getAppContext()).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

}
