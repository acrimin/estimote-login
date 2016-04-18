package com.jmarque.cpsc399project;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

public class MyApplication extends Application {

    private BeaconManager beaconManager;
    private static final int icy = 25545;
    private static final int mint = 32451;
    private static final int blueberry = 27888;

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
//                showNotification("Beacon Found: ",region.getIdentifier());

            }

            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
//                showNotification("Leaving Region: ",region.getIdentifier());
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                //mint green
                beaconManager.startMonitoring(new Region("Mint green",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 35445, 37670));


                /*//icy Marshmallow
                beaconManager.startMonitoring(new Region("Icy Marshmallow",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 26819, 33810));

                //Blueberry pie
                beaconManager.startMonitoring(new Region("Blueberry Pie",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 65035, 24542));*/
            }
        });
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
