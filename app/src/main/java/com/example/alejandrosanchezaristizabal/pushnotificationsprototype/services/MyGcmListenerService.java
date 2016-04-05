package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.R;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.activities.NotificationsDisplayerActivity;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.utils.PreferencesHelper;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by alejandrosanchezaristizabal on 24/03/16.
 */
public class MyGcmListenerService extends GcmListenerService {

  private static final String TAG = "MyGcmListenerService";

  /**
   * It's called when a notification is received.
   */
  @Override
  public void onMessageReceived(String senderId, Bundle notification) {
    Log.i(TAG, "From (Sender-ID): " + senderId);
    Log.i(TAG, "Message: " + notification.getString(PreferencesHelper.NOTIFICATION_BODY));

    showNotification(notification);
  }

  /**
   * Shows the created notification to the user.
   */
  private void showNotification(Bundle notification) {
    NotificationManager notificationManager = (NotificationManager) getSystemService
      (Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0 /* ID of notification */, createNotificationBuilder(notification)
      .build());
  }

  /**
   * Creates a simple notification containing the received GCM notification.
   */
  private NotificationCompat.Builder createNotificationBuilder(Bundle notification) {
    Intent notificationIntent = new Intent(this, NotificationsDisplayerActivity.class);
    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    notificationIntent.putExtras(notification);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */,
      notificationIntent, PendingIntent.FLAG_ONE_SHOT);

    String notificationTitle = notification.getString(PreferencesHelper.NOTIFICATION_TITLE);
    String notificationBody = notification.getString(PreferencesHelper.NOTIFICATION_BODY);

    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
      .setSmallIcon(R.drawable.common_ic_googleplayservices).setContentTitle(notificationTitle)
        .setContentText(notificationBody).setAutoCancel(true).setSound(defaultSoundUri)
          .setContentIntent(pendingIntent);

    Log.i(TAG, "The notification was created successfully");
    return notificationBuilder;
  }
}
