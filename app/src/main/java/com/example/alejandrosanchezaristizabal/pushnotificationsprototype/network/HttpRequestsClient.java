package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.services.RegistrationIntentService;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.utils.PreferencesHelper;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.utils.RequestQueueSingletonHelper;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alejandrosanchezaristizabal on 30/03/16.
 */
public class HttpRequestsClient {

  private static final String TAG = "HttpReqClient";

  private int reqMethod;
  private String url;
  private JSONObject params;
  private Context context;

  public HttpRequestsClient(int reqMethod, String url, JSONObject params, Context context) {
    this.reqMethod = reqMethod;
    this.url = url;
    this.params = params;
    this.context = context;
  }

  /**
   * Sends the registration-ID to the server in order to identify the user's device in the future.
   */
  public void sendRegistrationIdToServer() {
    JsonObjectRequest jsObjRequest = new JsonObjectRequest(reqMethod, url, params,
      new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          String userId = null;
          try {
            userId = String.valueOf(response.getLong(PreferencesHelper.ID));
          }
          catch (JSONException e) {
            e.printStackTrace();
          }
          RegistrationIntentService.setSharedPreferences(context, true, userId);
          Log.i(TAG, "HTTP-Response: " + response.toString());
          RegistrationIntentService.notifyResultToIntentServicesReceiver(context);
        }
      },
      new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          RegistrationIntentService.setSharedPreferences(context, false, null);
          Log.i(TAG, "Failed getting HTTP-Response");
          error.printStackTrace();
          RegistrationIntentService.notifyResultToIntentServicesReceiver(context);
        }
      });

    addRequestToQueue(jsObjRequest);
  }

  /**
   * Accesses the RequestQueue through the singleton class and adds the current request.
   */
  public void addRequestToQueue(Request request) {
    if (isNetworkAvailable()) {
      RequestQueueSingletonHelper.getInstance(context).addToRequestQueue(request);
    }
    else {
      showInternetUnavailabilityMessage(context);
      Log.i(TAG, "Not available Internet connection");
    }
  }

  /**
   * Checks if there is an available Internet connection in order to execute the request.
   */
  private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager = (ConnectivityManager) context
      .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  public static void showInternetUnavailabilityMessage(final Context context) {
    Handler mainThread = new Handler(Looper.getMainLooper());
    mainThread.post(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(context, "You need to be connected to the Internet", Toast.LENGTH_SHORT)
          .show();
      }
    });
  }
}
