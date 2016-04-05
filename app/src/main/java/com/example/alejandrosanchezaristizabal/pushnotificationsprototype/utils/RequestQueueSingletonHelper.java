package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.utils;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by alejandrosanchezaristizabal on 30/03/16.
 */
public class RequestQueueSingletonHelper {

  private static RequestQueueSingletonHelper mInstance;
  private RequestQueue mRequestQueue;
  private static Context mCtx;

  private RequestQueueSingletonHelper(Context context) {
    mCtx = context;
    mRequestQueue = getRequestQueue();
  }

  public static synchronized RequestQueueSingletonHelper getInstance(Context context) {
    if (mInstance == null) {
      mInstance = new RequestQueueSingletonHelper(context);
    }
    return mInstance;
  }

  /**
   * Returns the current instance for the RequestQueue or creates a new one if it didn't exist.
   */
  public RequestQueue getRequestQueue() {
    if (mRequestQueue == null) {
      // getApplicationContext() is key, it keeps you from leaking the
      // Activity or BroadcastReceiver if someone passes one in.
      mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
    }
    return mRequestQueue;
  }

  /**
   * Adds a Request to the RequestQueue.
   */
  public <T> void addToRequestQueue(Request<T> req) {
    getRequestQueue().add(req);
  }
}