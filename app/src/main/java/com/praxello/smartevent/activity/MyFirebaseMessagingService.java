package com.praxello.smartevent.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.praxello.smartevent.R;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String s) {
        /*hridayamApp = (HridayamApp) getApplication();
        if (!TextUtils.isEmpty(s))
            hridayamApp.getPreferences().setToken(s);*/
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            sendNotification(remoteMessage.getData().get("message"),m);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            sendNotification(remoteMessage.getNotification().getBody(),m);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    private void sendNotification(String message, int m) {
        Intent intent;
       /* if (hridayamApp.getPreferences().isLoggedInUser()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }*/
        intent = new Intent(this, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Notification n;
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.order_beep);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"id_hridayam");
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setLargeIcon(largeIcon);
//            notificationBuilder.setSmallIcon(R.mipmap.app_icon_noti);
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }
//        notificationBuilder.setContentTitle(getString(R.string.app_name));
        int defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
//        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        notificationBuilder.setContentText(message);
//        notificationBuilder.setSubText(restaurant_name);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setVibrate(new long[] { 1000, 1000});
        notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        notificationBuilder.setDefaults(defaults);
        notificationBuilder.setContentTitle(getResources().getString(R.string.app_name));

//        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(spanned));
        // notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle());
        notificationBuilder.setContentIntent(pendingIntent);

        n = notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message)).build();
        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        //Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        return R.mipmap.ic_launcher;
    }

}
