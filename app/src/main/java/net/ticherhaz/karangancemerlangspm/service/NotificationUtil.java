package net.ticherhaz.karangancemerlangspm.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import net.ticherhaz.karangancemerlangspm.R;
import net.ticherhaz.karangancemerlangspm.SplashActivity;

class NotificationUtil {

    //jangan ubah (19/2/2020)
    private static String createNotificationChannel(Context context, String channelId, String channelName, String channelDescription) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            final AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            NotificationChannel notificationChannel =
                    new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                notificationChannel.setAllowBubbles(true);
            }
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.setShowBadge(true);
            notificationChannel.setName("DealDebt");
            notificationChannel.setSound(soundUri, attributes);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
            return channelId;
        } else {
            return null;
        }
    }

    static void AnnouncementNotification(final Context context, final String channelUid, final int notificationUid,
                                         final String channelName, final String channelDescription, final String summary,
                                         final String title, final String description) {
        // 1. Create/Retrieve Notification Channel for O and beyond devices (26+).
        String notificationChannelId =
                NotificationUtil.createNotificationChannel(context, channelUid, channelName, channelDescription);

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_icon_128dp);

        // 2. Build the BIG_TEXT_STYLE.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                // Overrides ContentText in the big form of the template.
                .bigText(description)
                // Overrides ContentTitle in the big form of the template.
                .setBigContentTitle(title)
                // Summary line after the detail section in the big form of the template.
                // Note: To improve readability, don't overload the user with info. If Summary Text
                // doesn't add critical information, you should skip it.
                .setSummaryText(summary);

        // 3. Set up main Intent for notification.
        Intent notifyIntent = new Intent(context, SplashActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, notificationUid, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        assert notificationChannelId != null;
        NotificationCompat.Builder notificationCompatBuilder =
                new NotificationCompat.Builder(
                        context.getApplicationContext(), notificationChannelId);
        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);
        Notification notification = notificationCompatBuilder
                .setStyle(bigTextStyle)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_icon_128dp)
                .setLargeIcon(bitmap)
                .setContentIntent(notifyPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL) //ini untuk sound light and vibrate
                .setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.colorPrimary))
                .setColorized(true)
                .setCategory(Notification.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setChannelId(channelUid)
                .build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context.getApplicationContext());
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManagerCompat.notify(notificationUid, notification);
    }

    static void ForumNotification(final Context context, final String channelUid, final int notificationUid,
                                  final String channelName, final String channelDescription, final String summary,
                                  final String title, final String description) {
        // 1. Create/Retrieve Notification Channel for O and beyond devices (26+).
        String notificationChannelId =
                NotificationUtil.createNotificationChannel(context, channelUid, channelName, channelDescription);

        // 2. Build the BIG_TEXT_STYLE.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                // Overrides ContentText in the big form of the template.
                .bigText(description)
                // Overrides ContentTitle in the big form of the template.
                .setBigContentTitle(title)
                // Summary line after the detail section in the big form of the template.
                // Note: To improve readability, don't overload the user with info. If Summary Text
                // doesn't add critical information, you should skip it.
                .setSummaryText(summary);

        // 3. Set up main Intent for notification.
        Intent notifyIntent = new Intent(context, SplashActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, notificationUid, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_icon_128dp);

        assert notificationChannelId != null;
        NotificationCompat.Builder notificationCompatBuilder =
                new NotificationCompat.Builder(
                        context.getApplicationContext(), notificationChannelId);
        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);
        Notification notification = notificationCompatBuilder
                .setStyle(bigTextStyle)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_icon_128dp)
                .setLargeIcon(bitmap)
                .setContentIntent(notifyPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL) //ini untuk sound light and vibrate
                .setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.colorPrimary))
                .setColorized(true)
                .setCategory(Notification.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setChannelId(channelUid)
                .build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context.getApplicationContext());
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManagerCompat.notify(notificationUid, notification);
    }
}
