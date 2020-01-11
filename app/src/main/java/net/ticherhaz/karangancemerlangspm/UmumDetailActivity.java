package net.ticherhaz.karangancemerlangspm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.model.UmumDetail;
import net.ticherhaz.karangancemerlangspm.recyclerview.UmumDetailRecyclerView;
import net.ticherhaz.karangancemerlangspm.util.RunTransaction;

import java.util.ArrayList;
import java.util.List;

import static net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasa;

/*
 * @@important
 * IMPORTANT:
 * 24/11/2019: Preparation for the new update coming soon. I will code in here look clean and understandable.
 *             The variable will be very short and readable for all dev. I try to reduce the size of the code by doing that.
 *             1. About xml, we will try to specific xml that we use, for example umum detail xml, so edittext gonna be like this
 *                et_umum_detail .
 *             2. As you can see, we already specified the usage of the edittext only can be use in here.
 */
public class UmumDetailActivity extends SkinActivity {

    /*
     * Make it count when the user press the button send,
     * and it will triggered for 30 seconds for the button
     * disabled.
     *
     */
    private static int c = 30;
    //[START] Firebase [START]
    private FirebaseDatabase fDe;
    private DatabaseReference dRe;
    private FirebaseAuth fAh;
    private FirebaseUser fUr;
    //[END] ---------- [END]

    //[START] Make a recyclerview adapter to display umum detail [START]
    private UmumDetailRecyclerView umumDetailRV;
    private List<UmumDetail> umumDetailL = new ArrayList<>();
    //[END] ---------------------------------------------------- [END]

    //ProgressBar
    private ProgressBar pB;
    //RecyclerView
    private RecyclerView rV;
    //EditText
    private EditText etMessage;
    //Floating Action Button
    private FloatingActionButton fAB;
    //Constraint Layout
    private ConstraintLayout cL;

    //Variables
    private String umumUid, tajukPos, forumUid, registeredUidReply, userType;
    private long reputationPower;
    private boolean isSendable = true;

    /**
     * Countdown for the user after they send the message,
     * and the button will disabled.
     */
    private Runnable countDown = new Runnable() {
        @Override
        public void run() {
            while (c > 0) {
                c--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //It will set to 30 seconds again
            c = 30;
            //Enable the button to send
            isSendable = true;
        }
    };


    /**
     * We will get the reputation power and userType to store as String and long in this activity,
     * it can accessible to another/transfer to another method to use.
     * This is very crucial part where the userType will be initialized and reusable to another method/activity
     */
    private void initData() {
        dRe.child("registeredUser").child(registeredUidReply).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);
                    if (registeredUser != null) {
                        //Get the value of reputation power and user type and store in variables
                        reputationPower = registeredUser.getReputationPower();
                        userType = registeredUser.getTypeUser();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * This is listID where all the values are initialized. So the code will look clean.
     */
    private void listID() {
        fDe = FirebaseDatabase.getInstance();
        dRe = fDe.getReference();
        fAh = FirebaseAuth.getInstance();
        fUr = fAh.getCurrentUser();

        pB = findViewById(R.id.pb_umum_detail);
        rV = findViewById(R.id.rv_umum_detail);
        etMessage = findViewById(R.id.et_message_umum_detail);
        fAB = findViewById(R.id.btn_send_umum_detail);
        cL = findViewById(R.id.cl_umum_detail);

        //Get uid/check if the user is not null or not
        if (fUr != null) {
            registeredUidReply = fUr.getUid();
            initData();
        }

        /*
         * Initialize the Floating Action Button where the button is pressed.
         */
        fAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initB();
            }
        });

        /*
         *
         * Init view for recycler view.
         * [START]
         */
        Query query = dRe.child("umumPos").child(forumUid).child(umumUid);
        rV.setLayoutManager(new LinearLayoutManager(this));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    umumDetailL.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UmumDetail user = snapshot.getValue(UmumDetail.class);
                        umumDetailL.add(user);
                    }
                    umumDetailRV = new UmumDetailRecyclerView(UmumDetailActivity.this, cL, umumDetailL, dRe, fUr, forumUid, umumUid, registeredUidReply, userType, reputationPower);
                    rV.setAdapter(umumDetailRV);
                    umumDetailRV.notifyDataSetChanged();

                    if (umumDetailL.isEmpty())
                        pB.setVisibility(View.VISIBLE);
                    else
                        pB.setVisibility(View.GONE);
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
        /*
         *
         * Init the intent, get the value intent from activity before which is UmumActivity
         */
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            umumUid = intent.getExtras().getString("umumUid");
            tajukPos = intent.getExtras().getString("tajukPos");
            forumUid = intent.getExtras().getString("forumUid");
        }
        setContentView(R.layout.activity_umum_detail);
        setTitle(tajukPos);
        listID();
    }

    /**
     * Check if the value is empty or not before send message press button
     */
    private void initB() {
        if (!TextUtils.isEmpty(etMessage.getText().toString())) {
            //Disable button once there is value, to avoid double data
            fAB.setEnabled(false);

            //check if the user is already signed in or not
            if (fUr != null) {
                if (isSendable) {

                    /*
                     * Init variables to store the database umum detail model class
                     */
                    final String umumDetailUid = dRe.push().getKey();
                    final String onCreatedDate = GetTarikhMasa();
                    final String reply = etMessage.getText().toString();
                    final UmumDetail umumDetail = new UmumDetail(umumDetailUid, registeredUidReply, onCreatedDate, reply);

                    if (umumDetailUid != null) {
                        dRe.child("umumPos").child(forumUid).child(umumUid).child(umumDetailUid).setValue(umumDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //Then we store the data into the umum
                                    dRe.child("umum").child(forumUid).child(umumUid).child("masaDibalasOleh").setValue(onCreatedDate);
                                    dRe.child("umum").child(forumUid).child(umumUid).child("registeredUidLastReply").setValue(registeredUidReply);

                                    //After that we increase the amount of post
                                    new RunTransaction().runTransactionRegisteredUserPostCount(dRe, registeredUidReply);

                                    //Create umum pos participants for notification
                                    dRe.child("umumPosParticipants").child(umumUid).child(fUr.getUid()).setValue(true);

                                    //Clear text and enable back the button
                                    etMessage.getText().clear();
                                    fAB.setEnabled(true);

                                    //Hide keyboard
                                    try {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        if (UmumDetailActivity.this.getCurrentFocus() != null)
                                            if (imm != null) {
                                                imm.hideSoftInputFromWindow(UmumDetailActivity.this.getCurrentFocus().getWindowToken(), 0);
                                            }
                                    } catch (Exception ignored) {
                                    }
                                }
                            }
                        });
                    }

                    isSendable = false;
                    Thread t = new Thread(countDown);
                    t.start();
                } else {
                    Toast.makeText(getApplicationContext(), "Kamu boleh membalas semula " + c + " saat lagi...", Toast.LENGTH_SHORT).show();
                }
            } else {
                //So tell the user to login 1st.
                Toast.makeText(UmumDetailActivity.this, "Sila Daftar/Log Masuk Terlebih Dahulu", Toast.LENGTH_LONG).show();
                etMessage.getText().clear();
            }

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStop() {
        Glide.with(this).pauseRequests();
        super.onStop();
    }
}
