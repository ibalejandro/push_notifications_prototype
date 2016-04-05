package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.network;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.services.RegistrationIntentService;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.utils.RequestQueueSingletonHelper;
import org.json.JSONObject;

/**
 * Created by alejandrosanchezaristizabal on 30/03/16.
 */
public class HttpRequestsClient {

  private static final String TAG = "HttpReqManager";

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
          RegistrationIntentService.setSharedPreferences(context, true);
          Log.i(TAG, "HTTP-Response: " + response.toString());
          RegistrationIntentService.notifyResultToIntentServicesReceiver(context);
        }
      },
      new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          RegistrationIntentService.setSharedPreferences(context, false);
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
    RequestQueueSingletonHelper.getInstance(context).addToRequestQueue(request);
  }

}
