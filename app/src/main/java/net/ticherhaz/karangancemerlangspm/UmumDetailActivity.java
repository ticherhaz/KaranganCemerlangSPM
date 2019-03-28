package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import net.ticherhaz.karangancemerlangspm.Model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.Model.UmumDetail;
import net.ticherhaz.karangancemerlangspm.Util.ConvertTimeToText;
import net.ticherhaz.karangancemerlangspm.Util.RunTransaction;
import net.ticherhaz.karangancemerlangspm.ViewHolder.UmumDetailHolder;

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
    private long reputationPower;


    private ProgressDialog progressDialog;


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
                        reputationPower = registeredUser.getReputationPower();
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
            protected void onBindViewHolder(@NonNull final UmumDetailHolder holder, int position, @NonNull final UmumDetail model) {
                // @SuppressLint("SimpleDateFormat") final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                //Here we will retrieve user data from user database.
                databaseReference.child("registeredUser").child(model.getRegisteredUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            final RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);

                            if (registeredUser != null) {
                                holder.getTextViewUsername().setText(registeredUser.getUsername());
                                holder.getTextViewUserTitle().setText(registeredUser.getTitleType());
                                holder.getTextViewSekolah().setText(registeredUser.getSekolah());
                                holder.getTextViewUserJoinDate().setText("Masa Menyertai: " + registeredUser.getOnDateCreatedMonthYear());
                                holder.getTextViewGender().setText("Jantina: " + registeredUser.getGender());
                                holder.getTextViewPos().setText("Pos: " + String.valueOf(registeredUser.getPostCount()));
                                holder.getTextViewReputation().setText(String.valueOf(registeredUser.getReputation()));

                                holder.getTextViewGiveReputation().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog alertDialog = new AlertDialog.Builder(UmumDetailActivity.this)
                                                .setCancelable(false)
                                                .setTitle("Memberi Reputasi")
                                                .setMessage("Adakah anda ingin memberi reputasi kepada " + registeredUser.getUsername() + "?")
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(final DialogInterface dialog, int which) {
                                                        //Display the progress dialog
                                                        progressDialog.show();
                                                        //If yes, then we add the reputation power in this specfic user who post.
                                                        databaseReference.child("registeredUser").child(model.getRegisteredUid()).child("reputation").runTransaction(new Transaction.Handler() {
                                                            @NonNull
                                                            @Override
                                                            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                                                if (mutableData.getValue() == null) {
                                                                    mutableData.setValue(0);
                                                                } else {
                                                                    mutableData.setValue((Long) mutableData.getValue() + reputationPower); //add the reputation power
                                                                }
                                                                return Transaction.success(mutableData);
                                                            }

                                                            @Override
                                                            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                                                //Hide the progress dialog after finish give the reputation
                                                                progressDialog.dismiss();
                                                                Toast.makeText(getApplicationContext(), "Berjaya memberi reputasi", Toast.LENGTH_SHORT).show();
                                                                dialog.cancel();
                                                            }
                                                        });
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //if press no
                                                        dialog.cancel();
                                                    }
                                                })
                                                .create();
                                        if (firebaseUser != null) {
                                            alertDialog.show();
                                        } else {
                                            Toast.makeText(UmumDetailActivity.this, "Sila Daftar/Log Masuk Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.getTextViewDeskripsi().setText(model.getDeskripsi());

                //Display the 1minit yg lalu
                holder.getTextViewMasaDibalasOleh().setText(new ConvertTimeToText().covertTimeToText(model.getPostCreatedDate()));

                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


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

        progressDialog = new ProgressDialog(UmumDetailActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Memuat Naik...");

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

            //check if the user is already signed in or not
            if (firebaseUser != null) {
                String umumDetailUid = databaseReference.push().getKey();
                //TODO: this is format date to store string and we can retrieve the string like this : 2 minit yang lalu.
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

                                //after that we increase the amount of post
                                new RunTransaction().runTransactionRegisteredUserPostCount(databaseReference, registeredUidReply);

                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                editTextReply.setText("");
                                //then hide the keyboard
                                try {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (UmumDetailActivity.this.getCurrentFocus() != null)
                                        imm.hideSoftInputFromWindow(UmumDetailActivity.this.getCurrentFocus().getWindowToken(), 0);
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    });
                }
            } else {
                //So tell the user to login 1st.
                Toast.makeText(UmumDetailActivity.this, "Sila Daftar/Log Masuk Terlebih Dahulu", Toast.LENGTH_SHORT).show();
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
