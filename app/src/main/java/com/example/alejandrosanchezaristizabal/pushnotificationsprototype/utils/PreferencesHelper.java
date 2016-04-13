package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.utils;

/**
 * Created by alejandrosanchezaristizabal on 24/03/16.
 */
public class PreferencesHelper {

  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String NOTIFICATION_BODY = "body";
  public static final String NOTIFICATION_TITLE = "title";
  public static final String REGISTRATION_COMPLETE = "registrationComplete";
  public static final String REGISTRATION_ID = "registrationId";
  public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
  public static final String TOKEN_REFRESH = "tokenRefresh";
  public static final String USER_ID = "userId";
  public static final String USER_NAME = "VW User";

  // Network attributes.
  public static final String IP_ADDRESS = "YOUR_IP_ADDRESS";
  public static final String PORT = "8080";
  public static final String SERVER_LOC = "http://" + IP_ADDRESS + ":" + PORT;

  // Services' URLs.
  public static final String SEND_REG_ID_URL = SERVER_LOC + "/register";
  public static final String UPDATE_REG_ID_URL = SERVER_LOC + "/updateRegistrationId";
}