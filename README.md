# push_notifications_prototype
A proof of concept for Android Push Notifications.

This app allows a user to register its device for receiving push notifications. When a notification is sent from an app server, this app handles the receipt of the Google Cloud Messaging (GCM) message and shows the respective notification to the user, allowing him to see its content in a separate activity. When the Registration-ID of an user has been refreshed, this app also triggers the Updating service to save the corresponding changes.

### Instructions
* Download this project and the [push_notifications_prototype_server][push_notif_proto_server].
* Import both projects into the corresponding IDEs.
* In this project, set the value of the constant String IP_ADDRESS (under ${PROJECT_HOME}/app/src/main/java/<package>/utils/PreferencesHelper.java) with your IP-address. Something like:

    ```sh
    public static final String IP_ADDRESS = "192.166.1.142".
    ```
* Set also the value of the constant String PORT (located under the same path) if it differs from the commonly used one (i.e. 8080). Something like:

    ```sh
    public static final String PORT = "8081".
    ```
* Run the [push_notifications_prototype_server][push_notif_proto_server] you downloaded and make sure that it starts successfully before you run this Android app.

### References
* [GCM Client App for Android] - Set up a GCM Client App on Android (Visited on March 2016).
* [GCM Quickstart] - Google Cloud Messaging Quickstart (Visited on March 2016).
* [Android Volley Tutorial] - Android Volley Tutorial – Making HTTP GET, POST, PUT (Visited on April 2016).
* [Transmitting Network Data] - Transmitting Network Data Using Volley (Visited on April 2016).


[//]: # (These are reference links used in the body of this note)
   [push_notif_proto_server]: <https://github.com/ibalejandro/push_notifications_prototype_server>
   [GCM Client App for Android]: <https://developers.google.com/cloud-messaging/android/client#sample-register>
   [GCM Quickstart]: <https://github.com/googlesamples/google-services/tree/master/android/gcm>
   [Android Volley Tutorial]: <http://www.itsalif.info/content/android-volley-tutorial-http-get-post-put>
   [Transmitting Network Data]: <http://developer.android.com/intl/es/training/volley/index.html>
