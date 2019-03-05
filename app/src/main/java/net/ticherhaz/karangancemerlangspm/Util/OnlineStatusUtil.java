package net.ticherhaz.karangancemerlangspm.Util;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import net.ticherhaz.karangancemerlangspm.Model.RegisteredUser;

public class OnlineStatusUtil {

    public OnlineStatusUtil() {
    }

    //update user online status 1st, then check online status
    public void updateUserOnlineStatus(String onlineStatus, String registeredUid, FirebaseUser firebaseUser, DatabaseReference databaseReference) {
        if (firebaseUser != null) {
            databaseReference.child("registeredUser").child("main").child(registeredUid).child("onlineStatus").setValue(onlineStatus);
            //After we update the online status, then we CHECK the online status, finally we update the total online status.
            checkOnlineStatus(databaseReference, registeredUid);
        }
    }

    //OKchange plan, we check the user onlinestatus at the registered account.
    private void checkOnlineStatus(final DatabaseReference databaseReference, String registeredUid) {
        databaseReference.child("registeredUser").child("main").child(registeredUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //Get the value of online status
                    String onlineStatus = dataSnapshot.getValue(RegisteredUser.class).getOnlineStatus();
                    if (onlineStatus.equals("Online")) {
                        //Then if the user online, we update total
                        updateTotalOnlineStatus("Online", databaseReference);
                    } else if (onlineStatus.equals("Offline")) {
                        updateTotalOnlineStatus("Offline", databaseReference);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Update the total online status
    private void updateTotalOnlineStatus(final String online, final DatabaseReference databaseReference) {
        if (online.equals("Online")) {
            databaseReference.child("OnlineStatus").child("totalOnline").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        long totalOnline = dataSnapshot.getValue(long.class);
                        long addingThisUser = totalOnline + 1;
                        databaseReference.child("OnlineStatus").child("totalOnline").setValue(addingThisUser);
                    } else {
                        long totalOnline = 0;
                        long addingThisUser = totalOnline + 1;
                        databaseReference.child("OnlineStatus").child("totalOnline").setValue(addingThisUser);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (online.equals("Offline")) {
            databaseReference.child("OnlineStatus").child("totalOnline").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        long totalOnline = dataSnapshot.getValue(long.class);
                        long addingThisUser = totalOnline - 1;
                        databaseReference.child("OnlineStatus").child("totalOnline").setValue(addingThisUser);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}
