package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import net.ticherhaz.karangancemerlangspm.Model.Umum;
import net.ticherhaz.karangancemerlangspm.ViewHolder.UmumHolder;
import net.ticherhaz.tarikhmasa.TarikhMasa;

import static net.ticherhaz.karangancemerlangspm.Util.Others.messageInternetMessage;
import static net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasaTimeAgo;

public class UmumActivity extends SkinActivity {

    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //FirebaseAuth
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //FirebaseUi
    private FirebaseRecyclerOptions<Umum> firebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<Umum, UmumHolder> firebaseRecyclerAdapter;

    //ProgressBar
    private ProgressBar progressBar;

    //RecyclerView
    private RecyclerView recyclerView;

    //Button
    private Button buttonTopikBaru;

    private String title;
    private String forumUid;
    private String userUid;

    private void setFirebaseRecyclerAdapter() {
        progressBar.setVisibility(View.VISIBLE);
        Query query = databaseReference.child("umum").child(forumUid);

        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Umum>()
                .setQuery(query, Umum.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Umum, UmumHolder>(firebaseRecyclerOptions) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull final UmumHolder holder, int position, @NonNull final Umum model) {
                //new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                //Change the date to ago

                /*Kita tukar pakai yg class untuk tukar time to text

                 */

                //At this part, we retrieve the value user information according to the registeredUid that we saved
                //for the name thread
                databaseReference.child("registeredUser").child(model.getRegisteredUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);
                            if (registeredUser != null) {
                                String dimulaiOleh = "Dimulai Oleh <b>" + registeredUser.getUsername() + "</b>, " + GetTarikhMasaTimeAgo(model.getOnCreatedDate(), "MY", true, false);
                                holder.getTextViewDimulaiOleh().setText(Html.fromHtml(dimulaiOleh));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //check if null or not
                if (model.getRegisteredUidLastReply() != null) {
                    //for the name of last reply
                    databaseReference.child("registeredUser").child(model.getRegisteredUidLastReply()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);
                                if (registeredUser != null) {
                                    String dibalasOleh = "Dibalas Oleh <b>" + registeredUser.getUsername() + "</b>";
                                    holder.getTextViewDibalasOleh().setText(Html.fromHtml(dibalasOleh));
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    holder.getTextViewDibalasOleh().setVisibility(View.GONE);
                }


                if (model.getMasaDibalasOleh() != null) {
                    //This for the masa dibalas oleh
                    // holder.getTextViewMasaDibalasOleh().setText(new TimeCustom().convertTimeToAgo(model.getMasaDibalasOleh()));
                    holder.getTextViewMasaDibalasOleh().setText(TarikhMasa.GetTarikhMasaTimeAgo(model.getMasaDibalasOleh(), "MY", true, false));
                } else {
                    holder.getTextViewMasaDibalasOleh().setVisibility(View.GONE);
                }


                //This part we take the umumUid to display the total reply
                String umumUid = model.getUmumUid();
                databaseReference.child("umumPos").child(forumUid).child(umumUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            long size = dataSnapshot.getChildrenCount();
                            holder.getTextViewJumlahBalas().setText(String.valueOf(size - 1));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.getTextViewUmumTitle().setText(model.getTajuk());
                holder.getTextViewKedudukan().setText(String.valueOf(model.getKedudukan()));
                holder.getTextViewUmumViews().setText(String.valueOf(model.getViewed()));
                holder.getTextViewJumlahBalas().setText(String.valueOf(model.getJumlahBalas()));


                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Onclick we update the number of the views
                        databaseReference.child("umum").child(forumUid).child(model.getUmumUid()).child("viewed").runTransaction(new Transaction.Handler() {
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


                        Intent intent = new Intent(UmumActivity.this, UmumDetailActivity.class);
                        intent.putExtra("umumUid", model.getUmumUid());
                        intent.putExtra("tajukPos", model.getTajuk());
                        intent.putExtra("forumUid", forumUid);
                        startActivities(new Intent[]{intent});
                    }
                });
            }

            @NonNull
            @Override
            public UmumHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.umum_item, viewGroup, false);
                return new UmumHolder(view);
            }

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        };

        //Display
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
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
        buttonTopikBaru = findViewById(R.id.button_topik);
        recyclerView = findViewById(R.id.recycler_view_umum);

        setTotalOnlineSpecific(userUid, "Online");
        setFirebaseRecyclerAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveIntent();
        setContentView(R.layout.activity_umum);
        setTitle(title);
        listID();
        setButtonTopikBaru();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.stopListening();
            firebaseRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
            firebaseRecyclerAdapter.notifyDataSetChanged();
        }
    }

    //Button topik
    private void setButtonTopikBaru() {
        buttonTopikBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser != null) {
                    Intent intent = new Intent(UmumActivity.this, TopikBaruActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("forumUid", forumUid);
                    startActivities(new Intent[]{intent});

                } else {
                    Toast.makeText(getApplicationContext(), "Sila daftar/Log Masuk Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Check if there is internet or not
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    messageInternetMessage(UmumActivity.this);
                }

            }
        }, 5000);
    }

    private void setTotalOnlineSpecific(final String userUid, String onlineStatus) {
        //Update the total user is seeing this umum activity.
        if (userUid != null) {
            databaseReference.child("onlineStatusSpecific").child(forumUid).child(userUid).child("onlineStatus").setValue(onlineStatus);
            databaseReference.child("onlineStatusSpecific").child(forumUid).child(userUid).child("onlineStatus").onDisconnect().setValue("Offline");
        }
    }

    //OnBackPressed
    @Override
    public void onBackPressed() {
        setTotalOnlineSpecific(userUid, "Offline");
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
            userUid = intent.getExtras().getString("userUid");
        }
    }
}
