package net.ticherhaz.karangancemerlangspm.Util;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OnlineStatusUtil {


    public OnlineStatusUtil() {
    }

    //So before the disconnect happened, we check first if the user already online or offline

    //update user online status 1st, then check online status
    public void updateUserOnlineStatus(String onlineStatus, String registeredUid, FirebaseUser firebaseUser, DatabaseReference databaseReference, final String activitySessionUid, final String activityDate) {
        if (firebaseUser != null) {
            databaseReference.child("registeredUser").child(registeredUid).child("onlineStatus").setValue(onlineStatus);


            //    databaseReference.child("registeredUser").child("main").child(registeredUid).child("onlineStatus").onDisconnect().setValue("Offline");
            //After we update the online status, then we CHECK the online status, finally we update the total online status.
            checkOnlineStatus(registeredUid, onlineStatus, databaseReference, activitySessionUid, activityDate);
        }
    }

    //OKchange plan, we check the user onlinestatus at the registered account.
    private void checkOnlineStatus(final String registeredUid, final String onlineStatusA, final DatabaseReference databaseReference, final String activitySessionUid, final String activityDate) {

        //After that we make a new session which is when the user online, then we start take the date that he online than make a new table.
        //   final String activitySession = databaseReference.push().getKey();
        // Date currentDate = Calendar.getInstance().getTime();
        //  String date = String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a", new Date()));
        String onlineTime = String.valueOf(android.text.format.DateFormat.format("hh:mm:ss a", new Date()));


        //After the user is updating to ONLINE then it proceed here
        //Selepas kita dah update yg user tu dah online
        if (onlineStatusA.equals("Online")) {
            //So kita tambah dekat total online
            if (activitySessionUid != null) {
                databaseReference.child("activitySession").child(registeredUid).child(activityDate).child(activitySessionUid).setValue(activitySessionUid);
                databaseReference.child("activitySession").child(registeredUid).child(activityDate).child(activitySessionUid).child("activitySessionUid").setValue(activitySessionUid);
                databaseReference.child("activitySession").child(registeredUid).child(activityDate).child(activitySessionUid).child("date").setValue(activityDate);
                databaseReference.child("activitySession").child(registeredUid).child(activityDate).child(activitySessionUid).child("onlineTime").setValue(onlineTime);
            }

        } else if (onlineStatusA.equals("Offline")) {

            final DatabaseReference databaseReferenceLastOnline = FirebaseDatabase.getInstance().getReference().child("registeredUser").child(registeredUid);
            databaseReferenceLastOnline.child("lastOnline").setValue(ServerValue.TIMESTAMP);
            final String offlineTime = String.valueOf(android.text.format.DateFormat.format("hh:mm:ss a", new Date()));
            if (activitySessionUid != null) {
                databaseReference.child("activitySession").child(registeredUid).child(activityDate).child(activitySessionUid).child("offlineTime").setValue(offlineTime);
                //After that we calculate the total second that he active in the forum
                //We need to retrieve the data online time
                databaseReference.child("activitySession").child(registeredUid).child(activityDate).child(activitySessionUid).child("onlineTime").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            //we get the value first
                            String onlineTime = dataSnapshot.getValue(String.class);
                            try {

                                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
                                Date date1 = sdf.parse(onlineTime);
                                Date date2 = sdf.parse(offlineTime);

                                long millse = date1.getTime() - date2.getTime();
                                long mills = Math.abs(millse);

                                int Hours = (int) (mills / (1000 * 60 * 60));
                                int Mins = (int) (mills / (1000 * 60)) % 60;
                                long Secs = (int) (mills / 1000) % 60;

                                String totalTime = Hours + " hour, " + Mins + " mins, " + Secs + " secs";
                                //Then we add new value in the database
                                databaseReference.child("activitySession").child(registeredUid).child(activityDate).child(activitySessionUid).child("totalOnline").setValue(totalTime);


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


        }
    }

    public void onDisc(final FirebaseUser firebaseUser, final DatabaseReference databaseReference, final String registeredUid, final String activitySessionUid, final String activityDate) {
        if (firebaseUser != null) {

            databaseReference.child("registeredUser").child(registeredUid).child("onlineStatus").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String online = dataSnapshot.getValue(String.class);
                    if (online != null && online.equals("Online")) {
                        //Ni kalau dia dc
                        //TODO: OnDisconnect
                        databaseReference.child("registeredUser").child(registeredUid).child("onlineStatus").onDisconnect().setValue("Offline");
                        final DatabaseReference databaseReferenceLastOnline = FirebaseDatabase.getInstance().getReference().child("registeredUser").child(registeredUid).child("lastOnline");
                        databaseReferenceLastOnline.onDisconnect().setValue(ServerValue.TIMESTAMP);

                        //Then we update the activity after he want to close
                        final String offlineTime = String.valueOf(android.text.format.DateFormat.format("hh:mm:ss a", new Date()));
                        if (activitySessionUid != null) {

                            //ok new plan, since they will update base on this session uid,
                            //looks like we have to retrieve the value of the activity session uid from the database
                            databaseReference.child("activitySession").child(registeredUid).child(activityDate).child(activitySessionUid).child("activitySessionUid").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        final String oldActUid = dataSnapshot.getValue(String.class);


                                        databaseReference.child("activitySession").child(registeredUid).child(activityDate).child(activitySessionUid).child("offlineTime").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (!dataSnapshot.exists()) {


                                                    //Then we store the value base on the old act uid

                                                    databaseReference.child("activitySession").child(registeredUid).child(activityDate).child(oldActUid).child("offlineTime").onDisconnect().setValue(offlineTime);
                                                    //After that we calculate the total second that he active in the forum
                                                    //We need to retrieve the data online time
                                                    databaseReference.child("activitySession").child(registeredUid).child(activityDate).child(oldActUid).child("onlineTime").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            //at here we check if the offline time is already exist or not, if not, then we can create the ondisconnect. :)
                                                            if (dataSnapshot.exists()) {

                                                                //we get the value first
                                                                String onlineTime = dataSnapshot.getValue(String.class);
                                                                try {

                                                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
                                                                    Date date1 = sdf.parse(onlineTime);
                                                                    Date date2 = sdf.parse(offlineTime);

                                                                    long millse = date1.getTime() - date2.getTime();
                                                                    long mills = Math.abs(millse);

                                                                    int Hours = (int) (mills / (1000 * 60 * 60));
                                                                    int Mins = (int) (mills / (1000 * 60)) % 60;
                                                                    long Secs = (int) (mills / 1000) % 60;

                                                                    String totalTime = Hours + " hour, " + Mins + " mins, " + Secs + " secs";
                                                                    //Then we add new value in the database
                                                                    databaseReference.child("activitySession").child(registeredUid).child(activityDate).child(oldActUid).child("totalOnline").onDisconnect().setValue(totalTime);


                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });


                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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
