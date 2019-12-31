package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import net.ticherhaz.karangancemerlangspm.model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.model.Umum;
import net.ticherhaz.karangancemerlangspm.viewHolder.UmumHolder;
import net.ticherhaz.tarikhmasa.TarikhMasa;

import static net.ticherhaz.karangancemerlangspm.util.Others.messageInternetMessage;
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

    private String userType;

    private void retrieveFirebase() {
        if (firebaseUser != null) {
            databaseReference.child("registeredUser").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);
                        if (registeredUser != null) {
                            userType = registeredUser.getTypeUser();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void setFirebaseRecyclerAdapter() {
        progressBar.setVisibility(View.VISIBLE);
        Query query = databaseReference.child("umum").child(forumUid);

        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Umum>()
                .setQuery(query, Umum.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Umum, UmumHolder>(firebaseRecyclerOptions) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull final UmumHolder holder, final int position, @NonNull final Umum model) {

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
                                final String dimulaiOleh = "Dimulai Oleh <b>" + registeredUser.getUsername() + "</b>, " + GetTarikhMasaTimeAgo(model.getOnCreatedDate(), "MY", true, false);
                                holder.getTextViewDimulaiOleh().setText(Html.fromHtml(dimulaiOleh));

                                final String profileUrl = registeredUser.getProfileUrl();
                                //Check if profileUrl is null or not
                                if (profileUrl != null) {
                                    Glide.with(UmumActivity.this)
                                            .load(profileUrl)
                                            .into(holder.getImageViewProfile());
                                } else {
                                    holder.getImageViewProfile().setImageResource(R.drawable.emblem);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                /*
                ok this is new, read last node.
                 */
                //check if null or not
                Query queryDibbalasOleh = databaseReference.child("umumPos").child(forumUid).child(model.getUmumUid()).orderByKey().limitToLast(1);
                queryDibbalasOleh.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                final String registerUidReply = dataSnapshot1.child("registeredUid").getValue(String.class);
                                final String umumDetailUidFirst = dataSnapshot1.child("umumDetailUid").getValue(String.class);


                                //checking umumuid here if same or not to check whether it really the new user reply or not
                                if (model.getUmumUid().equals(umumDetailUidFirst)) {
                                    //After that we GONE it
                                    holder.getTextViewDibalasOleh().setVisibility(View.GONE);
                                } else {
                                    //    After we get this, we display
                                    if (registerUidReply != null) {
                                        databaseReference.child("registeredUser").child(registerUidReply).addValueEventListener(new ValueEventListener() {
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
                                    }
                                }
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if (model.getMasaDibalasOleh() != null) {
                    //This for the masa dibalas oleh
                    holder.getTextViewMasaDibalasOleh().setText(TarikhMasa.GetTarikhMasaTimeAgo(model.getMasaDibalasOleh(), "MY", true, false));
                } else {
                    holder.getTextViewMasaDibalasOleh().setVisibility(View.GONE);
                }


                //This part we take the umumUid to display the total reply
                final String umumUid = model.getUmumUid();
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

                if (firebaseUser != null) {
                    //Set on Long listener to delete this specific, check the user
                    if (userType.equals("admin") || userType.equals("moderator")) {
                        holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                AlertDialog alertDialog = new AlertDialog.Builder(UmumActivity.this)
                                        .setTitle("Options")
                                        .setMessage("Are you sure you want to delete this? \nps: please make sure you remove all the posts inside this thread")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                firebaseRecyclerAdapter.getRef(position).removeValue();
                                                databaseReference.child("umumPos").child(forumUid).child(umumUid).removeValue();
                                                //And then we need to remove the total post that user post
                                                //for example: ticherhaz post in this umumUid 3 posts. how to remove them?


                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();

                                alertDialog.show();
                                return true;
                            }
                        });
                    }
                }


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
        //Firebase
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
            firebaseRecyclerAdapter.notifyDataSetChanged();
        }
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
        // setFirebaseRecyclerAdapter();
        retrieveFirebase();
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
//        if (firebaseRecyclerAdapter != null) {
//            firebaseRecyclerAdapter.stopListening();
//            firebaseRecyclerAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (firebaseRecyclerAdapter != null) {
//            firebaseRecyclerAdapter.startListening();
//            firebaseRecyclerAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setFirebaseRecyclerAdapter();
//        if (firebaseRecyclerAdapter != null) {
//            firebaseRecyclerAdapter.startListening();
//            firebaseRecyclerAdapter.notifyDataSetChanged();
//        }

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
