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
   * Tries to get a Registration-ID from GCM.
   */
  @Override
  protected void onHandleIntent(Intent intent) {
    try {
      InstanceID instanceID = InstanceID.getInstance(this);
      String registrationId = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
          GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
      Log.i(TAG, "GCM Registration-ID: " + registrationId);
      User user = createUser(registrationId);
      sendRegistrationIdToServer(user);
    }
    catch (IOException e) {
      setSharedPreferences(this, false);
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
   * Creates the request to send the Registration-ID to the server.
   */
  public void sendRegistrationIdToServer(User user) {
    JSONObject params = createParams(PreferencesHelper.SEND_REG_ID_URL, user);
    if (params != null) {
      HttpRequestsClient httpReqManager = new HttpRequestsClient(Request.Method.POST,
        PreferencesHelper.SEND_REG_ID_URL, params, this);
      httpReqManager.sendRegistrationIdToServer();
    }
    else {
      setSharedPreferences(this, false);
      notifyResultToIntentServicesReceiver(this);
    }
  }

  /**
   * Creates parameters as a JSONObject for every particular request.
   */
  public JSONObject createParams(String url, Object object) {
    JSONObject params = new JSONObject();
    try {
      switch (url) {
        case PreferencesHelper.SEND_REG_ID_URL:
          User user = (User) object;
          params.put("name", user.getName());
          params.put("registrationId", user.getRegistrationId());
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
  public static void setSharedPreferences(Context context, boolean isResultSuccessful) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    if (isResultSuccessful) {
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