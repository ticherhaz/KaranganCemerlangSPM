package net.ticherhaz.karangancemerlangspm.service;


import static net.ticherhaz.karangancemerlangspm.service.NotificationUtil.AnnouncementNotification;
import static net.ticherhaz.karangancemerlangspm.service.NotificationUtil.ForumNotification;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //so ini untuk announcement, sebab kita pakai dekat firebase console
            final Context context = getApplicationContext();
            final String title = remoteMessage.getNotification().getTitle();
            final String message = remoteMessage.getNotification().getBody();
            final int notifcationUid = (int) System.currentTimeMillis();
            final String channelName = "channel" + title;

            AnnouncementNotification(context, "Announcement", notifcationUid, channelName, message, "Announcement Channel", title, message);
        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            String type = remoteMessage.getData().get("type");
            final String title = remoteMessage.getData().get("title");
            final String message = remoteMessage.getData().get("message");

            if (type == null)
                type = "Forum";

            final Context context = getApplicationContext();
            final int notificationUid = (int) System.currentTimeMillis();
            final String channelName = "channel" + type;
            ForumNotification(context, type, notificationUid, channelName, message, type, title, message);
        }
    }


    @Override
    public void onNewToken(@NonNull String s) {
        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String token) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            FirebaseDatabase.getInstance().getReference().child("registeredUserTokenUid").child(firebaseUser.getUid()).child(token).setValue(true);
        }
    }
}
