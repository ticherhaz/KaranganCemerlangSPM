package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.ticherhaz.karangancemerlangspm.Model.Forum;
import net.ticherhaz.karangancemerlangspm.Model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.Util.OnlineStatusUtil;
import net.ticherhaz.karangancemerlangspm.ViewHolder.ForumViewHolder;

public class ForumActivity extends AppCompatActivity {

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

    //Progressbar
    private ProgressBar progressBar;

    //Method setFirebaseRecycler
    private void setFirebaseRecyclerAdapter() {
        //We need to make the query for the firebase recycler adapter
        DatabaseReference databaseReferenceForum = FirebaseDatabase.getInstance().getReference().child("forum").child("main");
        //databaseReference.child("forum").child("main").child("1");
        //Setup for the recycler adapter option
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Forum>()
                .setQuery(databaseReferenceForum, Forum.class)
                .build();

        //After that apply in the firebase recycler adapter
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Forum, ForumViewHolder>(firebaseRecyclerOptions) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ForumViewHolder holder, int position, @NonNull Forum model) {
                holder.getTextViewForumTitle().setText(model.getForumTitle());
                holder.getTextViewUserViewing().setText("(" + String.valueOf(model.getForumUserViewing()) + " Viewing)");
                holder.getTextViewForumDescrption().setText(model.getForumDescription());
                holder.getTextViewForumViews().setText(String.valueOf(model.getForumViews()));
                holder.getTextViewThreads().setText(String.valueOf(model.getThreads()));
                holder.getTextViewPostThreadsCount().setText("Post: " + String.valueOf(model.getPostThreadsCount()));
                holder.getTextViewLastThreadPost().setText(String.valueOf(model.getLastThreadPost()));
                holder.getTextViewLastThreadByUser().setText(model.getLastThreadByUser());

            }

            @NonNull
            @Override
            public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.forum_item, viewGroup, false);
                return new ForumViewHolder(view);
            }

        };

        //After that we apply into the recycler adapter
        //Display
        //1. Set the recycler view
        recyclerViewForum.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewForum.setAdapter(firebaseRecyclerAdapter);
        //2. FirebaseUI
        firebaseRecyclerAdapter.notifyDataSetChanged();
        firebaseRecyclerAdapter.startListening();

    }

    //Method people online right now
    private void setTextViewOnlineRightNow() {
        databaseReference.child("OnlineStatus").child("totalOnline").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long totalOnline = dataSnapshot.getValue(Long.class);
                    textViewOnlineRightNow.setText(String.valueOf(totalOnline + " Online(s)"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void listID() {
        textViewUsername = findViewById(R.id.text_view_username);
        //This is for the textview auto gerak kalau nama dia panjang sangat
        textViewUsername.setSelected(true);
        textViewSekolah = findViewById(R.id.text_view_sekolah);
        textViewReputation = findViewById(R.id.text_view_reputation);
        textViewSignOut = findViewById(R.id.text_view_sign_out);
        textViewOnlineRightNow = findViewById(R.id.text_view_online_right_now);

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


        //Get the value of the userUid
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            userUid = intent.getExtras().getString("userUid");
        }
        //Check if the user online or not
        checkIfUserOnline();
        //Display all the online user number
        setTextViewOnlineRightNow();
        setTextViewOnlineRightNowClick();
    }

    private void checkIfUserOnline() {
        //Check if already login or not
        if (firebaseUser != null) {
            registeredUid = firebaseUser.getUid();
            //already sign in
            linearLayoutOlderUser.setVisibility(View.VISIBLE);
            linearLayoutNewUser.setVisibility(View.GONE);

            //Add the info in the userOnlineStatus
            //   updateUserOnlineStatus("Online");
            new OnlineStatusUtil().updateUserOnlineStatus("Online", registeredUid, firebaseUser, databaseReference);

            //Get the data from the database
            databaseReference.child("registeredUser").child("main").child(registeredUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String sekolah = dataSnapshot.getValue(RegisteredUser.class).getSekolah();
                        String reputation = String.valueOf(dataSnapshot.getValue(RegisteredUser.class).getReputation());
                        String username = dataSnapshot.getValue(RegisteredUser.class).getUsername();
                        //Check the online status, if online or not
                        if (dataSnapshot.getValue(RegisteredUser.class).getOnlineStatus().equals("Online")) {
                            textViewUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sign_online_green, 0, 0, 0);
                            textViewUsername.setCompoundDrawablePadding(1);
                        } else {
                            textViewUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sign_online, 0, 0, 0);
                            textViewUsername.setCompoundDrawablePadding(1);
                        }
                        textViewReputation.setText(reputation);
                        textViewSekolah.setText(sekolah);
                        textViewUsername.setText(username);
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

    //Method set text view online right now
    private void setTextViewOnlineRightNowClick() {
        textViewOnlineRightNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                // new OnlineStatusUtil().updateUserOnlineStatus("Online", registeredUid, firebaseUser, databaseReference);

                //We put delay
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(ForumActivity.this, OnlineUserActivity.class));
                    }
                }, 300);

                //  finish();
            }
        });
    }

    //Method sign in
    private void setButtonSignIn() {
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInDialog signInDialog = new SignInDialog(ForumActivity.this);
                //    signInDialog.setInitialUid(userUid);
                signInDialog.setLinearLayoutNewUser(linearLayoutNewUser);
                signInDialog.setLinearLayoutOldUser(linearLayoutOlderUser);
                signInDialog.setTextViewUsername(textViewUsername);
                signInDialog.setTextViewSekolah(textViewSekolah);
                signInDialog.setTextViewReputation(textViewReputation);
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
                firebaseAuth.signOut();
                // new OnlineStatusUtil().updateUserOnlineStatus("Offline", registeredUid, firebaseUser, databaseReference);
                startActivity(new Intent(ForumActivity.this, ForumActivity.class));
                finish();
                Toast.makeText(getApplicationContext(), "Sign Out Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        listID();
        setFirebaseRecyclerAdapter();
        setButtonSignIn();
        setButtonSignUp();
        setTextViewSignOut();
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

//    @Override
//    protected void onPause() {
//        new OnlineStatusUtil().updateUserOnlineStatus("Offline", registeredUid, firebaseUser, databaseReference);
//        super.onPause();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        // new OnlineStatusUtil().updateUserOnlineStatus("Online", registeredUid, firebaseUser, databaseReference);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        new OnlineStatusUtil().updateUserOnlineStatus("Offline", registeredUid, firebaseUser, databaseReference);
//        super.onStop();
//    }
//
//    @Override
//    protected void onRestart() {
//        new OnlineStatusUtil().updateUserOnlineStatus("Offline", registeredUid, firebaseUser, databaseReference);
//        super.onRestart();
//    }

    @Override
    protected void onDestroy() {
        new OnlineStatusUtil().updateUserOnlineStatus("Offline", registeredUid, firebaseUser, databaseReference);
        super.onDestroy();
    }
}
