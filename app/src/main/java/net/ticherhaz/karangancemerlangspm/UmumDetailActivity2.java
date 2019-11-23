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

public class UmumDetailActivity2 extends SkinActivity {

    int _count = 30;//30 seconds
    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    //FirebaseAuth
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //ProgressBar
    private ProgressBar progressBar;
    //RecyclerView
    private RecyclerView recyclerView;
    //EditText
    private EditText editTextReply;
    //ButtonFabReply
    private FloatingActionButton floatingActionButton;
    private String umumUid;
    private String tajukPos;
    private String forumUid;
    private String registeredUidReply;

    private String userType;
    private long reputationPower;

    private ConstraintLayout linearLayoutBottom;
    private boolean canSendMessage = true;
    private Runnable countDown = new Runnable() {
        @Override
        public void run() {
            while (_count > 0) {
                _count--;
                try {
                    Thread.sleep(1000);//1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            _count = 30;//again set to 30 seconds
            canSendMessage = true;//enable send
        }
    };
    private UmumDetailRecyclerView umumDetailRecyclerView;
    private List<UmumDetail> mUserList = new ArrayList<>();

    private void retrieveUserData() {
        databaseReference.child("registeredUser").child(registeredUidReply).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);
                    if (registeredUser != null) {
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

    private void listID() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressBar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.recycler_view_umum_detail);

        editTextReply = findViewById(R.id.edit_text_reply_pos);
        floatingActionButton = findViewById(R.id.button_reply);

        linearLayoutBottom = findViewById(R.id.linear_layout_bottom);

        //Get uid/ check if the user is not null or not
        if (firebaseUser != null) {
            registeredUidReply = firebaseUser.getUid();
            retrieveUserData();
        }

        setFloatingActionButton();
    }

    private void retrieveIntent() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            umumUid = intent.getExtras().getString("umumUid");
            tajukPos = intent.getExtras().getString("tajukPos");
            forumUid = intent.getExtras().getString("forumUid");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveIntent();
        setContentView(R.layout.activity_umum_detail2);
        setTitle(tajukPos);
        listID();
        initView();
    }

    private void initView() {
        Query query = databaseReference.child("umumPos").child(forumUid).child(umumUid);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    mUserList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UmumDetail user = snapshot.getValue(UmumDetail.class);
                        mUserList.add(user);
                    }
                    umumDetailRecyclerView = new UmumDetailRecyclerView(UmumDetailActivity2.this, linearLayoutBottom, mUserList, databaseReference, firebaseUser, forumUid, umumUid, registeredUidReply, userType, reputationPower);
                    recyclerView.setAdapter(umumDetailRecyclerView);
                    umumDetailRecyclerView.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Button Reply FAB
    private void setFloatingActionButton() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmpty();
            }
        });
    }

    //Method check
    private void checkEmpty() {
        if (!TextUtils.isEmpty(editTextReply.getText().toString())) {
            floatingActionButton.setEnabled(false);

            //check if the user is already signed in or not
            if (firebaseUser != null) {

                if (canSendMessage) {

                    String umumDetailUid = databaseReference.push().getKey();
                    //TODO: this is format date to store string and we can retrieve the string like this : 2 minit yang lalu.
                    final String onCreatedDate = GetTarikhMasa();
                    String reply = editTextReply.getText().toString();
                    UmumDetail umumDetail = new UmumDetail(umumDetailUid, registeredUidReply, onCreatedDate, reply);

                    if (umumDetailUid != null) {
                        databaseReference.child("umumPos").child(forumUid).child(umumUid).child(umumDetailUid).setValue(umumDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //Then we store the data into the umum
                                    databaseReference.child("umum").child(forumUid).child(umumUid).child("masaDibalasOleh").setValue(onCreatedDate);
                                    databaseReference.child("umum").child(forumUid).child(umumUid).child("registeredUidLastReply").setValue(registeredUidReply);

                                    //after that we increase the amount of post
                                    new RunTransaction().runTransactionRegisteredUserPostCount(databaseReference, registeredUidReply);

                                    //Create umum pos participants for notification
                                    databaseReference.child("umumPosParticipants").child(umumUid).child(firebaseUser.getUid()).setValue(true);

                                    Toast.makeText(getApplicationContext(), "Berjaya membalas forum ini", Toast.LENGTH_LONG).show();

                                    //Clear text and enable back the button
                                    editTextReply.setText("");
                                    floatingActionButton.setEnabled(true);
                                    //then hide the keyboard
                                    try {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        if (UmumDetailActivity2.this.getCurrentFocus() != null)
                                            if (imm != null) {
                                                imm.hideSoftInputFromWindow(UmumDetailActivity2.this.getCurrentFocus().getWindowToken(), 0);
                                            }
                                    } catch (Exception ignored) {
                                    }
                                }
                            }
                        });
                    }

                    canSendMessage = false;
                    Thread t = new Thread(countDown);
                    t.start();
                } else {
                    Toast.makeText(getApplicationContext(), "Kamu boleh membalas semula " + _count + " saat lagi...", Toast.LENGTH_SHORT).show();
                }
            } else {
                //So tell the user to login 1st.
                Toast.makeText(UmumDetailActivity2.this, "Sila Daftar/Log Masuk Terlebih Dahulu", Toast.LENGTH_LONG).show();
                editTextReply.setText("");
            }

        }
    }

    //OnBackPressed
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
