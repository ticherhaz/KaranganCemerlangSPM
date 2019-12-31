package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.model.Forum;
import net.ticherhaz.karangancemerlangspm.model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.util.OnlineStatusUtil;
import net.ticherhaz.karangancemerlangspm.viewHolder.ForumViewHolder;

import java.util.Calendar;

import static net.ticherhaz.karangancemerlangspm.util.Others.messageInternetMessage;
import static net.ticherhaz.karangancemerlangspm.util.Others.setStatus;
import static net.ticherhaz.tarikhmasa.TarikhMasa.ConvertTarikhMasa2LocalTimePattern;
import static net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasa;

public class ForumActivity extends SkinActivity {

    //Database
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //TextView Signed User
    private TextView textViewUsername;
    private TextView textViewSekolah;
    private TextView textViewReputation;
    private TextView textViewSignOut;
    private TextView textViewOnlineRightNow;

    //Button
    private Button buttonSignIn;
    private Button buttonSignUp;

    //Linear Layout
    private LinearLayout linearLayoutNewUser;
    private LinearLayout linearLayoutOlderUser;

    private String registeredUid;
    private String userUid;

    //RecyclerView for the forum
    private RecyclerView recyclerViewForum;
    private FirebaseRecyclerOptions<Forum> firebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<Forum, ForumViewHolder> firebaseRecyclerAdapter;

    //ActivitySessionUid
    private String activitySessionUid = FirebaseDatabase.getInstance().getReference().push().getKey();

    private String activityDate = ConvertTarikhMasa2LocalTimePattern(GetTarikhMasa(), "dd:MM:yyyy");

    //Progressbar
    private ProgressBar progressBar;
    private TextView textViewTotalPosCount;
    private TextView textViewSetting;
    private TextView textViewStatus;
    private ImageView ivProfile;

    //Method setFirebaseRecycler
    private void setFirebaseRecyclerAdapter() {
        progressBar.setVisibility(View.VISIBLE);
        //We need to make the query for the firebase recycler adapter
        final DatabaseReference databaseReferenceForum = FirebaseDatabase.getInstance().getReference().child("forum");
        Query query = databaseReferenceForum.orderByChild("forumPriority");
        //Setup for the recycler adapter option
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Forum>()
                .setQuery(query, Forum.class)
                .build();

        //After that apply in the firebase recycler adapter
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Forum, ForumViewHolder>(firebaseRecyclerOptions) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull final ForumViewHolder holder, int position, @NonNull final Forum model) {

                holder.getTextViewForumTitle().setText(model.getForumTitle());
                holder.getTextViewUserViewing().setText("(" + model.getForumUserViewing() + " Pemerhati)");
                holder.getTextViewForumDescrption().setText(model.getForumDescription());
                holder.getTextViewForumViews().setText(String.valueOf(model.getForumViews()));


                  /*
                ok this is new, read last node.
                 */
                //check if null or not
                Query queryDibbalasOleh = databaseReference.child("umum").child(model.getForumUid()).orderByKey().limitToLast(1);
                queryDibbalasOleh.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //Get children
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.exists()) {

                                    final String tajuk = dataSnapshot1.child("tajuk").getValue(String.class);
                                    final String dimulaiOleh = dataSnapshot1.child("dimulaiOleh").getValue(String.class);

                                    // String tajukTerbaru = model.getLastThreadPost();
                                    if (tajuk == null) {
                                        holder.getTextViewLastThreadPost().setVisibility(View.GONE);
                                    } else {
                                        holder.getTextViewLastThreadPost().setText("Tajuk Terbaru: " + model.getLastThreadPost());
                                    }

                                    //  String lastThreadByUser = model.getLastThreadByUser();
                                    if (dimulaiOleh == null) {
                                        holder.getTextViewLastThreadByUser().setVisibility(View.GONE);
                                    } else {
                                        //making the name bold
                                        String daripada = "Daripada <b>" + dimulaiOleh + "</b>";
                                        holder.getTextViewLastThreadByUser().setText(Html.fromHtml(daripada));
                                    }


                                }
                            }
                        } else {
                            holder.getTextViewLastThreadPost().setVisibility(View.GONE);
                            holder.getTextViewLastThreadByUser().setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //value for the jumlah tajuk
                if (model.getForumUid() != null) {
                    databaseReference.child("umum").child(model.getForumUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                long size = dataSnapshot.getChildrenCount();
                                holder.getTextViewThreads().setText("Jumlah Tajuk: " + size);
                            } else {
                                //Hide it
                                holder.getTextViewThreads().setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //value for the jumlah pos
                    databaseReference.child("umumPos").child(model.getForumUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long sum = 0;
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    long size = dataSnapshot1.getChildrenCount();
                                    sum += size;
                                    holder.getTextViewPostThreadsCount().setText("Jumlah Pos: " + sum);
                                }
                            } else {
                                //Hide
                                holder.getTextViewPostThreadsCount().setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    //Display for the total user who is watching the umum specific
                    calculateAllOnlineSpecific(model.getForumUid(), holder.getTextViewUserViewing());

                }


                //When it clicked
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Onclick we update the number of the views
                        databaseReferenceForum.child(model.getForumUid()).child("forumViews").runTransaction(new Transaction.Handler() {
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

                        Intent intent = new Intent(ForumActivity.this, UmumActivity.class);
                        intent.putExtra("title", model.getForumTitle());
                        intent.putExtra("userUid", userUid);
                        intent.putExtra("forumUid", model.getForumUid());
                        startActivities(new Intent[]{intent});
                    }
                });

            }

            @NonNull
            @Override
            public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forum_item, viewGroup, false);
                return new ForumViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.INVISIBLE);
            }

        };

        //After that we apply into the recycler adapter
        //Display
        //1. Set the recycler view
        recyclerViewForum.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewForum.setAdapter(firebaseRecyclerAdapter);
        //2. FirebaseUI
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
            firebaseRecyclerAdapter.notifyDataSetChanged();
        }
    }

    //Make a new calculation.
    private void calculateAllOnlineRegisteredUser() {
        databaseReference.child("registeredUser").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long totalOnline = 0;
                long online;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String userOnline = dataSnapshot1.child("onlineStatus").getValue(String.class);

                    if (userOnline != null && userOnline.equals("Online")) {
                        online = 1;
                        totalOnline = totalOnline + online;
                    } else {
                        online = 0;
                        totalOnline = totalOnline + online;
                    }
                    textViewOnlineRightNow.setText(totalOnline + " Orang Dalam Talian");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Make a new calculation.
    private void calculateAllOnlineSpecific(final String forumUid, final TextView textView) {
        databaseReference.child("onlineStatusSpecific").child(forumUid).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long totalOnline = 0;
                long online;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String userOnline = dataSnapshot1.child("onlineStatus").getValue(String.class);

                    if (userOnline != null && userOnline.equals("Online")) {
                        online = 1;
                        totalOnline = totalOnline + online;
                    } else {
                        online = 0;
                        totalOnline = totalOnline + online;
                    }
                    textView.setText("(" + totalOnline + " Pemerhati)");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Make token for registeredUser
    private void setTokenUid(final FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    if (instanceIdResult != null)
                        databaseReference.child("registeredUserTokenUid").child(firebaseUser.getUid()).child(instanceIdResult.getToken()).setValue(true);
                }
            });

        }
    }

    private void listID() {
        textViewUsername = findViewById(R.id.text_view_username);
        //This is for the textview auto gerak kalau nama dia panjang sangat
        textViewUsername.setSelected(true);
        textViewSekolah = findViewById(R.id.text_view_sekolah);
        textViewReputation = findViewById(R.id.text_view_reputation);
        textViewSignOut = findViewById(R.id.text_view_sign_out);
        textViewOnlineRightNow = findViewById(R.id.text_view_online_right_now);
        textViewTotalPosCount = findViewById(R.id.text_view_pos);
        textViewSetting = findViewById(R.id.text_view_setting);
        textViewStatus = findViewById(R.id.text_view_status);

        //Image View
        ivProfile = findViewById(R.id.iv_profile);

        //Button Initialize
        buttonSignIn = findViewById(R.id.button_sign_in);
        buttonSignUp = findViewById(R.id.button_sign_up);

        linearLayoutNewUser = findViewById(R.id.linear_layout_new_user);
        linearLayoutOlderUser = findViewById(R.id.linear_layout_signed_in_user);

        progressBar = findViewById(R.id.progressbar);

        //RecyclerView
        recyclerViewForum = findViewById(R.id.recycler_view_forum);

        //Initialize the database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //Auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        setTokenUid(firebaseUser);

        //Get the value of the userUid
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            userUid = intent.getExtras().getString("userUid");
        }
        //Check if the user online or not
        checkIfUserOnline();

        //Display all the online user number
        calculateAllOnlineRegisteredUser();
        setTextViewOnlineRightNowClick();
        setTextViewSetting();
        setTextViewStatus();
    }

    private void checkIfUserOnline() {
        //Check if already login or not
        if (firebaseUser != null) {

            registeredUid = firebaseUser.getUid();
            //already sign in
            linearLayoutOlderUser.setVisibility(View.VISIBLE);
            linearLayoutNewUser.setVisibility(View.GONE);

            //TODO: This part is for the reset day, it is for the reputation. When the new day, it will reset and give user to give reputation for other user.
            //First we need to detect the date
            Calendar calendar = Calendar.getInstance();
            int thisDay = calendar.get(Calendar.DAY_OF_YEAR);
            long todayMillis = calendar.getTimeInMillis();

            //We are using the shared preference
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            long last = sharedPreferences.getLong("date", 0);
            calendar.setTimeInMillis(last);
            int lastDay = calendar.get(Calendar.DAY_OF_YEAR);

            if (last == 0 || lastDay != thisDay) {
                //New day
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putLong("date", todayMillis);
                edit.apply();
                long newReputation = 3;
                //Then we store the
                databaseReference.child("reputationLimit").child(registeredUid).setValue(newReputation);
            }

            //Add the info in the userOnlineStatus
            new OnlineStatusUtil().updateUserOnlineStatus("Online", registeredUid, firebaseUser, databaseReference, activitySessionUid, activityDate);

            //Get the data from the database
            databaseReference.child("registeredUser").child(registeredUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);

                        if (registeredUser != null) {
                            final String sekolah = registeredUser.getSekolah();
                            final String reputation = String.valueOf(registeredUser.getReputation());
                            final String username = registeredUser.getUsername();
                            final String onlineStatus = registeredUser.getOnlineStatus();
                            final String totalPos = String.valueOf(registeredUser.getPostCount());
                            final String status = registeredUser.getMode();
                            final String profileUrl = registeredUser.getProfileUrl();
                            //Check the online status, if online or not
                            if (onlineStatus.equals("Online")) {
                                textViewUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sign_online_green, 0, 0, 0);
                                textViewUsername.setCompoundDrawablePadding(1);
                            } else {
                                textViewUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sign_online, 0, 0, 0);
                                textViewUsername.setCompoundDrawablePadding(1);
                            }
                            textViewReputation.setText(reputation);
                            textViewSekolah.setText(sekolah);

                            //Check type of user
                            switch (registeredUser.getTypeUser()) {
                                case "admin":
                                    textViewUsername.setTextColor(getResources().getColor(R.color.colorAdmin));
                                    break;
                                case "moderator":
                                    textViewUsername.setTextColor(getResources().getColor(R.color.colorModerator));
                                    break;
                                case "cikgu":
                                    textViewUsername.setTextColor(getResources().getColor(R.color.colorCikgu));
                                    break;
                                case "ahliPremium":
                                    textViewUsername.setTextColor(getResources().getColor(R.color.colorAhliPremium));
                                    break;
                            }

                            //Check for image if null or not (profileUrl)
                            if (profileUrl != null) {
                                Glide.with(ForumActivity.this)
                                        .load(profileUrl)
                                        .into(ivProfile);
                            } else {
                                ivProfile.setImageResource(R.drawable.emblem);
                            }
                            textViewUsername.setText(username);
                            textViewTotalPosCount.setText(totalPos);
                            setStatus(status, textViewStatus);
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            linearLayoutNewUser.setVisibility(View.VISIBLE);
            linearLayoutOlderUser.setVisibility(View.GONE);
        }
    }

    private void setTextViewSetting() {
        textViewSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumActivity.this, SettingActivity.class);
                intent.putExtra("userUid", userUid);
                startActivities(new Intent[]{intent});
            }
        });
    }

    private void setTextViewStatus() {
        textViewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusDialog statusDialog = new StatusDialog(ForumActivity.this);
                statusDialog.setRegisteredUid(registeredUid);
                statusDialog.show();
            }
        });
    }

    //Method set text view online right now
    private void setTextViewOnlineRightNowClick() {
        textViewOnlineRightNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForumActivity.this, OnlineUserActivity.class));
            }
        });
    }

    //Method sign in
    private void setButtonSignIn() {
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInDialog signInDialog = new SignInDialog(ForumActivity.this);
                signInDialog.setActivity(ForumActivity.this);
                signInDialog.setLinearLayoutNewUser(linearLayoutNewUser);
                signInDialog.setLinearLayoutOldUser(linearLayoutOlderUser);
                signInDialog.setTextViewUsername(textViewUsername);
                signInDialog.setTextViewSekolah(textViewSekolah);
                signInDialog.setTextViewReputation(textViewReputation);
                signInDialog.setUserUid(userUid);
                signInDialog.show();
            }
        });
    }

    //Method sign up
    private void setButtonSignUp() {
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpDialog signUpDialog = new SignUpDialog(ForumActivity.this);
                signUpDialog.setInitialUid(userUid);
                signUpDialog.setLinearLayoutNewUser(linearLayoutNewUser);
                signUpDialog.setActivity(ForumActivity.this);
                signUpDialog.setLinearLayoutOldUser(linearLayoutOlderUser);
                signUpDialog.setTextViewUsername(textViewUsername);
                signUpDialog.setTextViewSekolah(textViewSekolah);
                signUpDialog.setTextViewReputation(textViewReputation);
                signUpDialog.show();
            }
        });
    }


    //Method sign out
    private void setTextViewSignOut() {
        textViewSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make alert dialog
                AlertDialog alertDialog = new AlertDialog.Builder(ForumActivity.this)
                        .setTitle("Log Keluar?")
                        .setMessage("Anda pasti untuk log keluar?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new OnlineStatusUtil().updateUserOnlineStatus("Offline", registeredUid, firebaseUser, databaseReference, activitySessionUid, activityDate);
                                //Clear the tokenUid
                                FirebaseDatabase.getInstance().getReference().child("registeredUserTokenUid").child(firebaseUser.getUid()).removeValue();
                                firebaseAuth.signOut();
                                Intent intent = new Intent(ForumActivity.this, ForumActivity.class);
                                intent.putExtra("userUid", userUid);
                                startActivities(new Intent[]{intent});
                                finish();
                                Toast.makeText(getApplicationContext(), "Sign Out Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();

                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.GREEN);
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.RED);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        new OnlineStatusUtil().onDisc(firebaseUser, databaseReference, registeredUid, activitySessionUid, activityDate);
//        if (firebaseRecyclerAdapter != null) {
//            firebaseRecyclerAdapter.stopListening();
//            firebaseRecyclerAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        listID();
        setButtonSignIn();
        setButtonSignUp();
        setTextViewSignOut();
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
                    messageInternetMessage(ForumActivity.this);
                }

            }
        }, 5000);
    }


    //OnBackPressed
    @Override
    public void onBackPressed() {
        new OnlineStatusUtil().updateUserOnlineStatus("Offline", registeredUid, firebaseUser, databaseReference, activitySessionUid, activityDate);
        new OnlineStatusUtil().onDisc(firebaseUser, databaseReference, registeredUid, activitySessionUid, activityDate);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
