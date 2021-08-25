package com.itridtechnologies.resturantapp.notification;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.itridtechnologies.resturantapp.UiViews.Activities.BasicActvity;
import com.itridtechnologies.resturantapp.utils.Config;
import com.itridtechnologies.resturantapp.utils.NotificationsUtils;

import org.json.JSONException;
import org.json.JSONObject;



public class FCM extends FirebaseMessagingService {
    private static final String TAG="FCM";
    public static final MutableLiveData<String> deviceToken = new MutableLiveData<>();
    private NotificationsUtils notificationUtils;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        if (!s.isEmpty()) {
            deviceToken.postValue(s);
            Log.e(TAG, "onNewToken: " + s);
        } else {
            Log.e(TAG, "onNewToken: no new token");
        }
    }//onNewToken

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e(TAG, "onMessageReceived: Function Started " );
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getData().toString(),
                    remoteMessage.getNotification().getBody(),
                    remoteMessage.getData().get("type"));
        }

        //Check if message contains a data payload
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                Log.e(TAG, "onMessageReceived: json object" );
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

        Log.e("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        Log.e("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        generatePushNotification();
    }


    public void generatePushNotification() {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        Notification notification = new NotificationCompat.Builder(this, "CHANNEL_1_ID")
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .setContentTitle("Notification")
                .setContentText("bla bla bla")
                .build();

        Log.e(TAG, "generatePushNotification: working");

        notificationManager.notify(1, notification);
        ///we need to generate a random number if we want more than 1 notification in tray
        ///in place of id in NotificationManager.notify()
    }


    //Function for handling notification

    private void handleNotification(String orderId,String message, String logoutType) {
        if (!NotificationsUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            pushNotification.putExtra("type", logoutType);
            pushNotification.putExtra("idByNotification", orderId);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationsUtils NotificationsUtils = new NotificationsUtils(getApplicationContext());
            NotificationsUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
            Log.e(TAG, "handleNotification: app is in background");
        }
    }//handleNotification


    ///If payload is existed in message
    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            int data = Integer.parseInt(String.valueOf(json.getJSONObject("orderId")));
            String id = String.valueOf(data);
//            JSONObject payload = data.getJSONObject("payload");
//            Log.e(TAG, "payload: " + payload.toString());

            if (!NotificationsUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("idByNotification", id);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationsUtils NotificationsUtils = new NotificationsUtils(getApplicationContext());
                NotificationsUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), BasicActvity.class);
                resultIntent.putExtra("idByNotification", id);

//                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
//                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationsUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationsUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }


}
