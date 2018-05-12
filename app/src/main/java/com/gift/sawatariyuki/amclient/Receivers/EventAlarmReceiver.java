package com.gift.sawatariyuki.amclient.Receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import com.gift.sawatariyuki.amclient.HomeActivity;
import com.gift.sawatariyuki.amclient.R;

public class EventAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("event_title");
        sendEventStartNotification(context, msg);
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(2000);
        }
    }

    private static void sendEventStartNotification(Context context, String msg) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent descIntent = new Intent(context, HomeActivity.class);
        PendingIntent intent = PendingIntent.getActivity(context, 0, descIntent, 0);

        Notification notification = new Notification.Builder(context)
                .setContentTitle("Event Alarm")
                .setContentText("event: " + msg)
                .setSmallIcon(R.drawable.vibrate_img)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(intent)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .build();
        if (manager != null) {
            manager.notify(1, notification);
        }
    }
}
