package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.ticherhaz.karangancemerlangspm.Model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.Model.UmumDetail;
import net.ticherhaz.karangancemerlangspm.ViewHolder.UmumDetailHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UmumDetailActivity extends AppCompatActivity {


    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //FirebaseAuth
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //FirebaseUi
    private FirebaseRecyclerOptions<UmumDetail> firebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<UmumDetail, UmumDetailHolder> firebaseRecyclerAdapter;

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

    private String username;
    private String userTitle;
    private String sekolah;
    private String onDateCreatedMonthYear;
    private String gender;
    private long post;
    private long reputation;


    private void retrieveUserData() {
        databaseReference.child("registeredUser").child(registeredUidReply).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);

                    if (registeredUser != null) {
                        username = registeredUser.getUsername();
                        userTitle = registeredUser.getTitleType();
                        sekolah = registeredUser.getSekolah();
                        onDateCreatedMonthYear = registeredUser.getOnDateCreatedMonthYear();
                        gender = registeredUser.getGender();
                        post = registeredUser.getPostCount();
                        reputation = registeredUser.getReputation();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setFirebaseRecyclerAdapter() {
        progressBar.setVisibility(View.VISIBLE);
        Query query = databaseReference.child("umumPos").child(forumUid).child(umumUid);
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<UmumDetail>()
                .setQuery(query, UmumDetail.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UmumDetail, UmumDetailHolder>(firebaseRecyclerOptions) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull final UmumDetailHolder holder, int position, @NonNull UmumDetail model) {
                @SuppressLint("SimpleDateFormat") final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                //Change the date to ago
                try {
                    Date date = inputFormat.parse(model.getPostCreatedDate());
                    @SuppressLint({"NewApi", "LocalSuppress"}) String niceDateStr = String.valueOf(DateUtils.getRelativeTimeSpanString(date.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS));


                    //Here we will retrieve user data from user database.
                    databaseReference.child("registeredUser").child(model.getRegisteredUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);

                                if (registeredUser != null) {
                                    holder.getTextViewUsername().setText(registeredUser.getUsername());
                                    holder.getTextViewUserTitle().setText(registeredUser.getTitleType());
                                    holder.getTextViewSekolah().setText(registeredUser.getSekolah());
                                    holder.getTextViewUserJoinDate().setText("Masa Menyertai: " + registeredUser.getOnDateCreatedMonthYear());
                                    holder.getTextViewGender().setText("Jantina: " + registeredUser.getGender());
                                    holder.getTextViewPos().setText("Pos: " + String.valueOf(registeredUser.getPostCount()));
                                    holder.getTextViewReputation().setText(String.valueOf(registeredUser.getReputation()));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    holder.getTextViewDeskripsi().setText(model.getDeskripsi());
                    holder.getTextViewMasaDibalasOleh().setText(String.valueOf(niceDateStr));

                    holder.getView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });


                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }

            @NonNull
            @Override
            public UmumDetailHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.umum_detail_item, viewGroup, false);
                return new UmumDetailHolder(view);
            }

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        };

        //Display
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();
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

        //Get uid
        if (firebaseUser != null) {
            registeredUidReply = firebaseUser.getUid();
            retrieveUserData();
        }


        setFirebaseRecyclerAdapter();

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
        setContentView(R.layout.activity_umum_detail);
        setTitle(tajukPos);
        listID();
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

            String umumDetailUid = databaseReference.push().getKey();
            final String onCreatedDate = String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd'T'HH:mm:ss", new Date()));
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


                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            editTextReply.setText("");
                            //then hide the keyboard
                            try {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(UmumDetailActivity.this.getCurrentFocus().getWindowToken(), 0);
                            } catch (Exception ignored) {
                            }
                        }
                    }
                });
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
