package com.itridtechnologies.resturantapp.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

public class Restaurant extends Application {

    private static Restaurant _instance;

    public static Restaurant getAppContext() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        CreateNotificationChannels();
    }

    private void CreateNotificationChannels() {
        Log.e("TAG", "CreateNotificationChannels: created");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    "CHANNEL_1_ID",
                    "Receive New Orders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("By Setting it Off You Are Not Able To Receive New Orders");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
        getDeviceToken();
    }

    private void getDeviceToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("FCM TOKEN", "onComplete: Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    Constants.DEVICE_TOKEN = task.getResult();
                    String token = Constants.DEVICE_TOKEN;
                    //use token anywhere
                    Log.e("FCM TOKEN Resturant: ", token);
                    if (token != null) {
                        //use token anywhere
                        Log.e("FCM TOKEN", token);
                    }
                });
    }
}
