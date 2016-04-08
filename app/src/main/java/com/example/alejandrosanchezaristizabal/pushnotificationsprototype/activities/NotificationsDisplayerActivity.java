package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.utils.PreferencesHelper;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.R;

public class NotificationsDisplayerActivity extends AppCompatActivity {

  private static final String TAG = "NotifDisplayerActivity";

  private TextView title;
  private TextView body;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notifications_displayer);

    initViewComponents();
    showNotificationContent(getIntent().getExtras());
  }

  /**
   * Initializes the view components to show the notification's content.
   */
  public void initViewComponents() {
    title = (TextView) findViewById(R.id.tv_notification_title);
    body = (TextView) findViewById(R.id.tv_notification_body);
  }

  /**
   * Displays the notification's content as a structured text.
   */
  public void showNotificationContent(Bundle notification) {
    title.setText(notification.getString(PreferencesHelper.NOTIFICATION_TITLE));
    body.setText
      (notification.getString(PreferencesHelper.NOTIFICATION_BODY));
    Log.i(TAG, "The notification content was displayed successfully");
  }
}