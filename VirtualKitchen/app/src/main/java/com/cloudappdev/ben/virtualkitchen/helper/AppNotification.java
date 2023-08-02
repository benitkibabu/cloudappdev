package com.cloudappdev.ben.virtualkitchen.helper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.cloudappdev.ben.virtualkitchen.main.MainActivity;
import com.cloudappdev.ben.virtualkitchen.R;

/**
 * Created by Ben on 19/10/2016.
 */

public class AppNotification {
    private static final String NOTIFICATION_TAG = "Reminder";

    public static void notify(final Context context, final String id,
                              final String title, String bodyString, final int number) {
        final Resources res = context.getResources();

        final Bitmap picture = BitmapFactory.decodeResource(res, R.mipmap.kitchen_1);

        final String body = bodyString.length() > 100 ? bodyString.substring(0, 99): bodyString;

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.chef)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(context,R.color.colorAccent))
                .setLargeIcon(picture)
                .setTicker(title)
                .setNumber(number)
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0, navIntent(context, id),
                                PendingIntent.FLAG_UPDATE_CURRENT))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(title)
                        .setBigContentTitle(context.getResources().getString(R.string.app_name))
                        .setSummaryText(body))
                .setAutoCancel(true);

        notify(context, builder.build());
    }

    public static Intent navIntent(Context context, String id){
        //Need to change this
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("id", id);
        return i;
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}
