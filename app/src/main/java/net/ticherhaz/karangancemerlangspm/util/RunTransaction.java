package net.ticherhaz.karangancemerlangspm.util;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import net.ticherhaz.karangancemerlangspm.KaranganDetailActivity;

import java.util.Calendar;

public class RunTransaction {

    public void runTransactionKaranganMostVisited(final ProgressBar progressBar, final DatabaseReference databaseReference, final Activity context, final String userUid, final String modelUid, final String modelTajukPenuh, final String modelDeskripsiPenuh, final String modelTarikh, final String modelKarangan, final int modelVote, final int modelMostVisited, final String modelUserLastVisitedDate, final String karanganJenis) {
        //Hide the progressbar
        progressBar.setVisibility(View.INVISIBLE);
        context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        //After that we need to update this karangan about the lastuservisited
        String tarikh = Calendar.getInstance().getTime().toString();
        databaseReference.child("karangan").child(karanganJenis).child(modelUid).child("userLastVisitedDate").setValue(tarikh);
        databaseReference.child("karangan").child(karanganJenis).child(modelUid).child("userLastVisitedDate").setValue(tarikh);

        //This part we continue to the next activity
        Intent intent = new Intent(context, KaranganDetailActivity.class);
        //18.5.2019: Adding new one, tajukUid because we want to change from the title to become titleUid when user press like
        intent.putExtra("karanganJenis", karanganJenis);
        intent.putExtra("userUid", userUid);
        intent.putExtra("uidKarangan", modelUid);
        intent.putExtra("tajukPenuh", modelTajukPenuh);
        intent.putExtra("deskripsiPenuh", modelDeskripsiPenuh);
        intent.putExtra("tarikh", modelTarikh);
        intent.putExtra("karangan", modelKarangan);
        intent.putExtra("vote", modelVote);
        intent.putExtra("mostVisited", modelMostVisited);
        intent.putExtra("userLastVisitedDate", modelUserLastVisitedDate);
        context.startActivities(new Intent[]{intent});

        databaseReference.child("karangan").child(karanganJenis).child(modelUid).child("mostVisited").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue((Long) mutableData.getValue() + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {


            }
        });
    }

    public void runTransactionUserClick(final DatabaseReference databaseReference, final String userUid, final String tajukUid) {
        databaseReference.child("userFirstKaranganClick").child(userUid).child(tajukUid).child("click").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue((Long) mutableData.getValue() + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }

    public void runTransactionUserVoteKarangan(final DatabaseReference databaseReference, final String karanganJenis, final String karanganUid) {
        databaseReference.child("karangan").child(karanganJenis).child(karanganUid).child("vote").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue((Long) mutableData.getValue() + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }

    public void runTransactionRegisteredUserPostCount(final DatabaseReference databaseReference, final String registeredUid) {
        databaseReference.child("registeredUser").child(registeredUid).child("postCount").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(0);
                } else {
                    mutableData.setValue((Long) mutableData.getValue() + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }

    public void runTransactionRegisteredUserPostCountRemove(final DatabaseReference databaseReference, final String registeredUid) {
        databaseReference.child("registeredUser").child(registeredUid).child("postCount").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(0);
                } else {
                    mutableData.setValue((Long) mutableData.getValue() - 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }


    public void postCountReward(final DatabaseReference databaseReference, final String registeredUid, final long pos) {
        long defaultReward = 50;

        while (pos == defaultReward) {

            databaseReference.child("registeredUser").child(registeredUid).child("reputationPower").runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    if (mutableData.getValue() == null) {
                        mutableData.setValue(0);
                    } else {
                        mutableData.setValue((Long) mutableData.getValue() + 1);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                }
            });
            defaultReward += 50;
        }
    }
}
