package com.app.mobiledev.apphris.notifikasi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.mobiledev.apphris.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MyFCMService extends FirebaseMessagingService {

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        Log.d("TAG_ONMSG_SENT", "onMessageSent: " + s);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("TAG_REMOTE_MESSAGE", "onMessageReceived: " + remoteMessage.getNotification());

        String title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String body = Objects.requireNonNull(remoteMessage.getNotification()).getBody();
        String click_action = remoteMessage.getData().get("click_action");

        Log.d("TAG_REMOTE_MESSAGE0", "onMessageReceived: " + title);
        Log.d("TAG_REMOTE_MESSAGE1", "onMessageReceived: " + body);
        Log.d("TAG_REMOTE_MESSAGE2", "onMessageReceived: " + click_action);

        //sendNotification(title, body, click_action);
        //sendNotifL(title, body, click_action);
        showNotificationMessage(title, body, click_action);
    }

    /*@RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification(String title, String body, String click_action) {

        Intent intent = new Intent(click_action);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainApplication.context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainApplication.context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) MainApplication.context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void sendNotifL(String title, String body, String click_action) {

        Intent intent = new Intent(click_action);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainApplication.context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification notification  = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true).build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationMessage(String title, String body, String click_action) {

        Intent intent = new Intent(click_action);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainApplication.context, channelId)
                .setSmallIcon(R.drawable.ic_hris)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_hris))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[] { 10, 10, 10, 10, 10 })
                .setColor(getResources().getColor(R.color.blue))
                .setContentIntent(pendingIntent);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainApplication.context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());


    }

}