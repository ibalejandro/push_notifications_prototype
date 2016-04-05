package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.utils.PreferencesHelper;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.R;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.services.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

  private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
  private static final String TAG = "MainActivity";

  private BroadcastReceiver intentServicesReceiver;
  private boolean isIntentServicesReceiverRegistered;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    createIntentServicesReceiver();

    registerIntentServicesReceiver();

    startRegistrationIntentService();
  }

  /**
   * Creates a receiver for the IntentServices' results. When te notification of the IntentService
   * arrives, it checks whether the transaction was successful or not.
   */
  public void createIntentServicesReceiver() {
    intentServicesReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager
          .getDefaultSharedPreferences(context);
        boolean sentRegistrationId = sharedPreferences
          .getBoolean(PreferencesHelper.SENT_TOKEN_TO_SERVER, false);
        if (sentRegistrationId) {
          Toast.makeText(getApplicationContext(), "Your device was successfully registered",
            Toast.LENGTH_SHORT).show();
          Log.i(TAG, "Registration-ID successfully sent to the server");
        }
        else {
          Toast.makeText(getApplicationContext(), "Failed registering your device",
            Toast.LENGTH_SHORT).show();
          Log.i(TAG, "Failed sending Registration-ID to the server");
        }
      }
    };
  }

  /**
   * Registers the receiver for the IntentServices' results in case it wasn't.
   */
  private void registerIntentServicesReceiver() {
    if (!isIntentServicesReceiverRegistered) {
      LocalBroadcastManager.getInstance(this).registerReceiver(intentServicesReceiver,
        new IntentFilter(PreferencesHelper.REGISTRATION_COMPLETE));
      isIntentServicesReceiverRegistered = true;
    }
  }

  /**
   * Starts the IntentService to get a Registration-ID from GCM.
   */
  public void startRegistrationIntentService() {
    Toast.makeText(getApplicationContext(), "Registering your device", Toast.LENGTH_SHORT)
      .show();
    if (arePlayServicesAvailable()) {
      Intent registrationIntentService = new Intent(this, RegistrationIntentService.class);
      startService(registrationIntentService);
    }
  }

  /**
   * Checks the device to make sure it has the Google Play Services APK. If it doesn't, displays a
   * dialog that allows users to download the APK from the Google Play Store or enable it in the
   * device's system settings.
   */
  private boolean arePlayServicesAvailable() {
    GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
    int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
    if (resultCode != ConnectionResult.SUCCESS) {
      if (apiAvailability.isUserResolvableError(resultCode)) {
        Log.i(TAG, "This device is able to be supported");
        apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
      }
      else {
        Toast.makeText(getApplicationContext(), "Sorry, this device is not supported",
          Toast.LENGTH_SHORT).show();
        Log.i(TAG, "This device is not supported");
        finish();
      }
      return false;
    }
    Log.i(TAG, "This device is supported");
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onResume() {
    super.onResume();
    registerIntentServicesReceiver();
  }

  @Override
  protected void onPause() {
    LocalBroadcastManager.getInstance(this).unregisterReceiver(intentServicesReceiver);
    isIntentServicesReceiverRegistered = false;
    super.onPause();
  }
}