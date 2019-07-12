package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.Model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.Model.UmumDetail;
import net.ticherhaz.karangancemerlangspm.Util.Others;
import net.ticherhaz.karangancemerlangspm.Util.RunTransaction;
import net.ticherhaz.karangancemerlangspm.Util.UserTypeColor;
import net.ticherhaz.karangancemerlangspm.ViewHolder.UmumDetailHolder;
import net.ticherhaz.tarikhmasa.TarikhMasa;

import static net.ticherhaz.tarikhmasa.TarikhMasa.ConvertTarikhMasa2LocalTimePattern;
import static net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasa;

public class UmumDetailActivity extends SkinActivity {

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
                        onDateCreatedMonthYear = registeredUser.getOnDateCreated();
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
                //1.
                //Here we will retrieve user data (this specific) from user database.
                databaseReference.child("registeredUser").child(model.getRegisteredUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);
                            if (registeredUser != null) {
                                String registeredUserUidA = registeredUser.getRegisteredUserUid();
                                String userUidA = registeredUser.getUserUid();
                                String typeUserA = registeredUser.getTypeUser();
                                String profileUrlA = registeredUser.getProfileUrl();
                                String emailA = registeredUser.getEmail();
                                String usernameA = registeredUser.getUsername();
                                String sekolahA = registeredUser.getSekolah();
                                String titleTypeA = registeredUser.getTitleType();
                                String customTitleA = registeredUser.getCustomTitle();
                                String bioA = registeredUser.getBio();
                                String genderA = registeredUser.getGender();
                                String stateA = registeredUser.getState();
                                String birthdayA = registeredUser.getBirthday();
                                String modeA = registeredUser.getMode();
                                long postCountA = registeredUser.getPostCount();
                                long reputationA = registeredUser.getReputation();
                                long reputationPowerA = registeredUser.getReputationPower();
                                String onlineStatusA = registeredUser.getOnlineStatus();
                                long lastOnlineA = registeredUser.getLastOnline();
                                String lastCreatedThreadA = registeredUser.getLastCreatedThread();
                                String onDateCreatedA = registeredUser.getOnDateCreated();

                                //Call another class to change color
                                new UserTypeColor().setTextColorUserUmumDetail(registeredUser, holder, UmumDetailActivity.this);


                                //This part is to display
                                holder.getTextViewUsername().setText(usernameA);
                                holder.getTextViewUserTitle().setText(titleTypeA);
                                holder.getTextViewSekolah().setText(sekolahA);
                                holder.getTextViewUserJoinDate().setText("Tarikh Sertai: " + ConvertTarikhMasa2LocalTimePattern(onDateCreatedA, "MMM yyyy"));
                                holder.getTextViewGender().setText("Jantina: " + genderA);
                                holder.getTextViewPos().setText("Pos: " + postCountA);
                                holder.getTextViewReputation().setText(String.valueOf(reputationA));
                                holder.getTextViewState().setText("Negeri: " + stateA);
                                new Others().setStatus(modeA, holder.getTextViewStatus());

                                //Edit part
                                //this part, first, we check if the user is already sign in or not and if the user valid, then he can edit his reply
                                //check if the same person, then, he able to edit it
                                if (firebaseUser != null) {
                                    if (model.getRegisteredUid().equals(firebaseUser.getUid())) {
                                        //then we show the button
                                        holder.getTextViewEditReply().setVisibility(View.VISIBLE);

                                        //Hide the giving of the reputation
                                        holder.getTextViewGiveReputation().setVisibility(View.GONE);

                                        holder.getTextViewEditReply().setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //when we click this one, then we change the layout display from the textview become the edittext.
                                                //so we gone the textview
                                                holder.getTextViewDeskripsi().setVisibility(View.GONE);
                                                //then we display the edit text
                                                holder.getEditTextEdit().setVisibility(View.VISIBLE);
                                                //after that we display the text of the reply.
                                                holder.getEditTextEdit().setText(model.getDeskripsi());
                                                //Then we hide the textview 'reply'
                                                holder.getTextViewEditReply().setVisibility(View.GONE);
                                                //We display with they yes, or cancel to edit
                                                holder.getTextViewEditYes().setVisibility(View.VISIBLE);
                                                holder.getTextViewEditCancel().setVisibility(View.VISIBLE);


                                                //After that we triggered the button yes to edit
                                                holder.getTextViewEditYes().setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String newDeskripsi = holder.getEditTextEdit().getText().toString();

                                                        if (newDeskripsi.isEmpty() && holder.getEditTextEdit().getText().toString().contains(" ")) {
                                                            Toast.makeText(getApplicationContext(), "Sila isi ayat anda...", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            //here we triggered to change in the database
                                                            databaseReference.child("umumPos").child(forumUid).child(umumUid).child(model.getUmumDetailUid()).child("deskripsi").setValue(newDeskripsi);
                                                            //After we finish
                                                            //back to normal
                                                            //then we change back all to normal
                                                            //we hide the edittext deskripsi
                                                            holder.getEditTextEdit().setVisibility(View.GONE);
                                                            //we display textview deskripsi
                                                            holder.getTextViewDeskripsi().setVisibility(View.VISIBLE);
                                                            //We hide this button cancel and yes
                                                            holder.getTextViewEditYes().setVisibility(View.GONE);
                                                            holder.getTextViewEditCancel().setVisibility(View.GONE);
                                                            //Then we display back the edit button
                                                            holder.getTextViewEditReply().setVisibility(View.VISIBLE);
                                                        }

                                                    }
                                                });

                                                //This one is for cancel
                                                holder.getTextViewEditCancel().setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        //then we change back all to normal
                                                        //we hide the edittext deskripsi
                                                        holder.getEditTextEdit().setVisibility(View.GONE);
                                                        //we display textview deskripsi
                                                        holder.getTextViewDeskripsi().setVisibility(View.VISIBLE);
                                                        //We hide this button cancel and yes
                                                        holder.getTextViewEditYes().setVisibility(View.GONE);
                                                        holder.getTextViewEditCancel().setVisibility(View.GONE);
                                                        //Then we display back the edit button
                                                        holder.getTextViewEditReply().setVisibility(View.VISIBLE);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }


                                //GIVE REPUTATION
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

                                                        //First we check the reputationLimit for this guy
                                                        databaseReference.child("reputationLimit").child(registeredUidReply).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()) {

                                                                    final long totalLimitReputation = dataSnapshot.getValue(Long.class);

                                                                    //If has
                                                                    if (totalLimitReputation > 0) {

                                                                        //Then we check if the user already given the reputation baru2 ni or not.
                                                                        Query query1 = databaseReference.child("reputationRecord").child(registeredUidReply).limitToLast(2);

                                                                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                if (dataSnapshot.exists()) {
                                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                                                                        String existedUser = dataSnapshot1.getValue(String.class);

                                                                                        //then check
                                                                                        if (model.getRegisteredUid().equals(existedUser)) {
                                                                                            Toast.makeText(getApplicationContext(), "Sila memberi reputasi kepada orang lain sama", Toast.LENGTH_SHORT).show();
                                                                                            progressDialog.dismiss();
                                                                                        } else if (!model.getRegisteredUid().equals(existedUser)) {

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

                                                                                                    //After we update the value, then we reduce the reputationLimit for this guy
                                                                                                    long afterDeductReputationLimit = totalLimitReputation - 1;
                                                                                                    databaseReference.child("reputationLimit").child(registeredUidReply).setValue(afterDeductReputationLimit);

                                                                                                    //After that, we catat the data that user already given the reputation.
                                                                                                    databaseReference.child("reputationRecord").child(registeredUidReply).push().setValue(model.getRegisteredUid());


                                                                                                    progressDialog.dismiss();
                                                                                                    Toast.makeText(getApplicationContext(), "Berjaya memberi reputasi", Toast.LENGTH_SHORT).show();
                                                                                                    dialog.cancel();
                                                                                                }
                                                                                            });
                                                                                        } else {
                                                                                            Toast.makeText(getApplicationContext(), "Masalah", Toast.LENGTH_SHORT).show();
                                                                                            progressDialog.dismiss();
                                                                                        }

                                                                                    }
                                                                                } else {


                                                                                    //If no data exist, then we proceed here.
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

                                                                                            //After we update the value, then we reduce the reputationLimit for this guy
                                                                                            long afterDeductReputationLimit = totalLimitReputation - 1;
                                                                                            databaseReference.child("reputationLimit").child(registeredUidReply).setValue(afterDeductReputationLimit);

                                                                                            //After that, we catat the data that user already given the reputation.
                                                                                            databaseReference.child("reputationRecord").child(registeredUidReply).push().setValue(model.getRegisteredUid());


                                                                                            progressDialog.dismiss();
                                                                                            Toast.makeText(getApplicationContext(), "Berjaya memberi reputasi", Toast.LENGTH_SHORT).show();
                                                                                            dialog.cancel();
                                                                                        }
                                                                                    });

                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });


                                                                    }
                                                                    //Then if the user already reach 0 total to give reputation
                                                                    else {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(getApplicationContext(), "Maaf, reputatasi hari ini sudah habis, sila tunggu esok", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }


                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

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

                                        //If the user is not null when pressing the button give reputation, mean its valid then display the alert dialog
                                        if (firebaseUser != null) {
                                            //and check if the user and the one who post is not the same person
                                            if (!model.getRegisteredUid().equals(firebaseUser.getUid())) {
                                                alertDialog.show();
                                            } else {
                                                Toast.makeText(UmumDetailActivity.this, "Tidak Boleh Reputasi Diri Sendiri", Toast.LENGTH_SHORT).show();
                                            }
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
                //   holder.getTextViewMasaDibalasOleh().setText(new TimeCustom().convertTimeToAgo(model.getPostCreatedDate()));
                holder.getTextViewMasaDibalasOleh().setText(TarikhMasa.GetTarikhMasaTimeAgo(model.getPostCreatedDate(), "MY", true, false));

                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


            }

            @NonNull
            @Override
            public UmumDetailHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.umum_detail_item, viewGroup, false);
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

        //Get uid/ check if the user is not null or not
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

                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                editTextReply.setText("");
                                //then hide the keyboard
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
