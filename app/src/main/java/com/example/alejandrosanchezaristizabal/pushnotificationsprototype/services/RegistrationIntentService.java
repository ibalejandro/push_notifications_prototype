package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.android.volley.Request;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.R;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.models.User;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.network.HttpRequestsClient;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.utils.PreferencesHelper;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

/**
 * Created by alejandrosanchezaristizabal on 24/03/16.
 */
public class RegistrationIntentService extends IntentService {

  private static final String TAG = "RegIntentService";
  private static final String[] TOPICS = {"global"};

  public RegistrationIntentService() {
    super(TAG);
  }

  /**
   * Tries to get a (new or updated) Registration-ID from GCM.
   */
  @Override
  protected void onHandleIntent(Intent intent) {
    try {
      InstanceID instanceID = InstanceID.getInstance(this);
      String registrationId = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
      Log.i(TAG, "GCM Registration-ID: " + registrationId);
      User user = createUser(registrationId);
      if (!intent.getExtras().getBoolean(PreferencesHelper.TOKEN_REFRESH)) {
        // It's a normal Registration-ID.
        sendRegistrationIdToServer(PreferencesHelper.SEND_REG_ID_URL, user);
      }
      else {
        // It's a refreshed Registration-ID.
        sendRegistrationIdToServer(PreferencesHelper.UPDATE_REG_ID_URL, user);
      }
    }
    catch (IOException e) {
      setSharedPreferences(this, false, null);
      Log.d(TAG, "Failed getting Registration-ID");
      e.printStackTrace();
      notifyResultToIntentServicesReceiver(this);
    }
  }

  /**
   * Creates a new user with a fixed username for the received Registration-ID.
   */
  public User createUser(String registrationId) {
    return new User(PreferencesHelper.USER_NAME, registrationId);
  }

  /**
   * Creates the request to send the Registration-ID (refreshed or not) to the server.
   */
  public void sendRegistrationIdToServer(String serviceUrl, User user) {
    JSONObject params = createParams(serviceUrl, user);
    if (!params.isNull(PreferencesHelper.REGISTRATION_ID)) {
      HttpRequestsClient httpReqClient = new HttpRequestsClient(Request.Method.POST, serviceUrl,
        params, this);
      httpReqClient.sendRegistrationIdToServer();
    }
    else {
      setSharedPreferences(this, false, null);
      notifyResultToIntentServicesReceiver(this);
    }
  }

  /**
   * Creates parameters as a JSONObject for every particular request.
   */
  public JSONObject createParams(String serviceUrl, Object object) {
    JSONObject params = new JSONObject();
    try {
      switch (serviceUrl) {
        case PreferencesHelper.SEND_REG_ID_URL:
        case PreferencesHelper.UPDATE_REG_ID_URL:
          User user = (User) object;
          SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences
            (getApplicationContext());
          params.put(PreferencesHelper.ID, sharedPreferences.getString(PreferencesHelper.USER_ID,
            null));
          params.put(PreferencesHelper.NAME, user.getName());
          params.put(PreferencesHelper.REGISTRATION_ID, user.getRegistrationId());
          break;
        default:
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }

    return params;
  }

  /**
   * Set the variables' values in the Preferences according to the IntentService's result.
   */
  public static void setSharedPreferences(Context context, boolean isResultSuccessful,
    String userId) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    if (isResultSuccessful) {
      sharedPreferences.edit().putString(PreferencesHelper.USER_ID, userId).apply();
      sharedPreferences.edit().putBoolean(PreferencesHelper.SENT_TOKEN_TO_SERVER, true).apply();
    }
    else {
      sharedPreferences.edit().putBoolean(PreferencesHelper.SENT_TOKEN_TO_SERVER, false).apply();
    }
  }

  /**
   * Notifies the IntentService's result to the BroadcastReceiver.
   */
  public static void notifyResultToIntentServicesReceiver(Context context) {
    Intent registrationComplete = new Intent(PreferencesHelper.REGISTRATION_COMPLETE);
    LocalBroadcastManager.getInstance(context).sendBroadcast(registrationComplete);
  }
}