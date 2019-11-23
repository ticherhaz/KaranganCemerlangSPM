package net.ticherhaz.karangancemerlangspm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.model.Umum;
import net.ticherhaz.karangancemerlangspm.model.UmumDetail;
import net.ticherhaz.karangancemerlangspm.model.UmumPosUser;
import net.ticherhaz.karangancemerlangspm.util.RunTransaction;

import static net.ticherhaz.karangancemerlangspm.util.Others.isNetworkAvailable;
import static net.ticherhaz.karangancemerlangspm.util.Others.messageInternetMessage;
import static net.ticherhaz.karangancemerlangspm.util.ProgressDialogCustom.dismissProgressDialog;
import static net.ticherhaz.karangancemerlangspm.util.ProgressDialogCustom.showProgressDialog;
import static net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasa;

public class TopikBaruActivity extends SkinActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private EditText editTextTajuk;
    private EditText editTextDeskripsi;
    private Button buttonHantar;

    private String userName;
    private String profileUrl;
    private String registeredUid;
    private String userTitle;
    private String sekolah;
    private String onAccountCreatedDate;
    private String gender;
    private long pos;
    private long reputation;

    private String title;
    private String forumUid;

    private void listID() {
        editTextTajuk = findViewById(R.id.edit_text_tajuk);
        editTextDeskripsi = findViewById(R.id.edit_text_deskripsi);
        buttonHantar = findViewById(R.id.button_hantar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            userName = firebaseUser.getDisplayName();
            profileUrl = String.valueOf(firebaseUser.getPhotoUrl());
            registeredUid = firebaseUser.getUid();

            retrieveData();
        }
    }

    //Retrieve value
    private void retrieveData() {
        databaseReference.child("registeredUser").child(registeredUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);
                    if (registeredUser != null) {
                        userTitle = registeredUser.getTitleType();
                        sekolah = registeredUser.getSekolah();
                        onAccountCreatedDate = registeredUser.getOnDateCreated();
                        pos = registeredUser.getPostCount();
                        reputation = registeredUser.getReputation();
                        gender = registeredUser.getGender();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveIntent();
        setContentView(R.layout.activity_topik_baru);
        listID();
        setButtonHantar();
    }

    private void setButtonHantar() {
        buttonHantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternet();
            }
        });
    }

    private void checkInternet() {
        showProgressDialog(TopikBaruActivity.this);
        //I think it is better we check the edittext is write down or not then we check the internet connection.
        if (!TextUtils.isEmpty(editTextTajuk.getText().toString()) && !TextUtils.isEmpty(editTextDeskripsi.getText().toString())) {
            if (isNetworkAvailable(TopikBaruActivity.this)) {
                storeDatabase();
            } else {
                messageInternetMessage(TopikBaruActivity.this);
            }
        }
    }

    private void storeDatabase() {

        //Make the umumUid
        final String umumUid = databaseReference.push().getKey();
        final String tajuk = editTextTajuk.getText().toString();
        String deskripsi = editTextDeskripsi.getText().toString();
        long viewed = 0;
        long jumlahBalas = 0;
        long kedudukan = 1;
        String dimulaiOleh = userName;
        long masaDimulaiOleh = System.currentTimeMillis();
        String dibalasOleh = "";
        String masaDibalasOleh = null;

        /* Using instant which is the latest java 8 iso 8906
         *  Import instant from the org.threeabp which is support for lower api
         *
         * */

        final String onCreatedDate = GetTarikhMasa();

        String registeredUidLastReply = null;

        String activityUmumLogUid = databaseReference.push().push().getKey();
        String activityKududukanLogUid = databaseReference.push().push().push().getKey();
        String umumPosUserUid = databaseReference.push().push().push().push().getKey();
        String type = title;
        String lastVisitedUser = userName;


        //Call class to store the value
        Umum umum = new Umum(umumUid, registeredUid, registeredUidLastReply, tajuk, deskripsi, viewed, jumlahBalas, kedudukan, dimulaiOleh, masaDimulaiOleh, dibalasOleh,
                masaDibalasOleh, onCreatedDate, activityUmumLogUid, activityKududukanLogUid, type, lastVisitedUser);


        UmumDetail umumDetail = new UmumDetail(umumUid, registeredUid, onCreatedDate, deskripsi);

        //Update umumPos of this user who want to post
        UmumPosUser umumPosUser = new UmumPosUser(umumPosUserUid, umumUid, registeredUid, 1);

        if (umumUid != null) {
            if (umumPosUserUid != null) {
                databaseReference.child("umumPosUser").child(umumUid).setValue(umumPosUser);
            }
            databaseReference.child("umum").child(forumUid).child(umumUid).setValue(umum);
            databaseReference.child("umumPos").child(forumUid).child(umumUid).child(umumUid).setValue(umumDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        //Then, after the data at the forum
                        runTransac(tajuk);

                        //after that we increase the amount of post
                        new RunTransaction().runTransactionRegisteredUserPostCount(databaseReference, registeredUid);

                        //This one we check if the user post is 50 or not togive reputation power
                        new RunTransaction().postCountReward(databaseReference, registeredUid, pos);

                        dismissProgressDialog();
                        editTextTajuk.setText("");
                        editTextDeskripsi.setText("");
                        Toast.makeText(getApplicationContext(), "Berjaya pos topik baru", Toast.LENGTH_SHORT).show();

                        //Create umum pos participants for notification
                        databaseReference.child("umumPosParticipants").child(umumUid).child(registeredUid).setValue(true);

                        //Then we proceed to the activity for that
                        Intent intent = new Intent(TopikBaruActivity.this, UmumDetailActivity2.class);
                        intent.putExtra("umumUid", umumUid);
                        intent.putExtra("tajukPos", tajuk);
                        intent.putExtra("forumUid", forumUid);
                        startActivities(new Intent[]{intent});
                        finish();
                    }
                }
            });
        }

    }


    private void runTransac(final String tajuk) {
        databaseReference.child("forum").child(forumUid).child("postThreadsCount").runTransaction(new Transaction.Handler() {
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
        databaseReference.child("forum").child(forumUid).child("threads").runTransaction(new Transaction.Handler() {
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

                //Then after complete them.
                //So this part we want to update the u
                databaseReference.child("forum").child(forumUid).child("lastThreadByUser").setValue(userName);
                databaseReference.child("forum").child(forumUid).child("lastThreadPost").setValue(tajuk);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void retrieveIntent() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            title = intent.getExtras().getString("title");
            forumUid = intent.getExtras().getString("forumUid");
        }
    }
}
