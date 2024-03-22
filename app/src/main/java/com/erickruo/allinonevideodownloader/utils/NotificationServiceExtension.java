/*
 * *
 *  * Created by Syed Usama Ahmad on 3/3/23, 4:58 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/3/23, 4:41 PM
 *
 */

package com.erickruo.allinonevideodownloader.utils;


import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.erickruo.allinonevideodownloader.activities.SplashScreen;
import com.onesignal.OSMutableNotification;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal.OSRemoteNotificationReceivedHandler;

import org.json.JSONObject;

@SuppressWarnings("unused")
public class NotificationServiceExtension implements OSRemoteNotificationReceivedHandler {

    @Override
    public void remoteNotificationReceived(Context context, OSNotificationReceivedEvent notificationReceivedEvent) {
        OSNotification notification = notificationReceivedEvent.getNotification();

        // Example of modifying the notification's accent color
        OSMutableNotification mutableNotification = notification.mutableCopy();

        try {
            JSONObject data = notification.getAdditionalData();
            Log.i("OneSignalExample", "Received Notification Data: " + data);

            if (data != null && data.has("status") && data.getBoolean("status")) {

                System.out.println("mydatais bacground  = " + data);

                AndroidNetworking.get(Constants.adminApiUrl + "/all")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    System.out.println("Apidata datais  " + response.toString());
                                    JSONObject jsonObject = new JSONObject(response.toString());

                                    if (jsonObject.getBoolean("status")) {
                                        new SharedPrefsMainApp(context).setPREFERENCE_adminpanelapidata(jsonObject.toString());
                                        SplashScreen.checkExistingAppDataFromAdminPanelAndSetData(context);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("Apidata error " + e.getMessage());
                                }

                            }

                            @Override
                            public void onError(ANError error) {
                                error.printStackTrace();
                                System.out.println("Apidata error " + error.getMessage());

                            }
                        });

                notificationReceivedEvent.complete(null);
                return;
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Apidata error " + e.getMessage());
        }

        // If complete isn't call within a time period of 25 seconds, OneSignal internal logic will show the original notification
        // To omit displaying a notification, pass `null` to complete()

        notificationReceivedEvent.complete(mutableNotification);
    }
}
