package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.services;

import android.content.Intent;
import android.util.Log;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.utils.PreferencesHelper;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by alejandrosanchezaristizabal on 24/03/16.
 */
public class MyInstanceIdListenerService extends InstanceIDListenerService {

  private static final String TAG = "MyInstanceIdLS";

  /**
   * Called if InstanceID token is updated. This may occur if the security of the previous token
   * had been compromised. This call is initiated by the InstanceID provider.
   */
  @Override
  public void onTokenRefresh() {
    Intent registrationIntentService = new Intent(this, RegistrationIntentService.class);
    registrationIntentService.putExtra(PreferencesHelper.TOKEN_REFRESH, true);
    Log.i(TAG, "Registration-ID needs to be refreshed");
    startService(registrationIntentService);
  }
}