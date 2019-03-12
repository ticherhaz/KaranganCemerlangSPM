package net.ticherhaz.karangancemerlangspm.Util;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class OnlineStatusUtil {

    public OnlineStatusUtil() {
    }

    //So before the disconnect happened, we check first if the user already online or offline

    //update user online status 1st, then check online status
    public void updateUserOnlineStatus(String onlineStatus, String registeredUid, FirebaseUser firebaseUser, DatabaseReference databaseReference) {
        if (firebaseUser != null) {
            databaseReference.child("registeredUser").child("main").child(registeredUid).child("onlineStatus").setValue(onlineStatus);
            //    databaseReference.child("registeredUser").child("main").child(registeredUid).child("onlineStatus").onDisconnect().setValue("Offline");
            //After we update the online status, then we CHECK the online status, finally we update the total online status.
            checkOnlineStatus(registeredUid, onlineStatus);
        }
    }

    //OKchange plan, we check the user onlinestatus at the registered account.
    private void checkOnlineStatus(final String registeredUid, final String onlineStatusA) {
        //After the user is updating to ONLINE then it proceed here
        //Selepas kita dah update yg user tu dah online
        if (onlineStatusA.equals("Online")) {
            //So kita tambah dekat total online
        } else if (onlineStatusA.equals("Offline")) {
            final DatabaseReference databaseReferenceLastOnline = FirebaseDatabase.getInstance().getReference().child("registeredUser").child("main").child(registeredUid).child("lastOnline");
            databaseReferenceLastOnline.setValue(ServerValue.TIMESTAMP);
        }
    }

//    private void minusTotal(final DatabaseReference databaseReference) {
//        //TODO: This run transaction is special for simultaneous user online or many user click like at the same time.
//        databaseReference.child("OnlineStatus").child("totalOnline").runTransaction(new Transaction.Handler() {
//            @NonNull
//            @Override
//            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                if (mutableData.getValue() == null) {
//                    mutableData.setValue(0);
//                } else {
//                    mutableData.setValue((Long) mutableData.getValue() - 1);
//                }
//                return Transaction.success(mutableData);
//            }
//
//            @Override
//            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//
//            }
//        });
//    }
}
