package net.ticherhaz.karangancemerlangspm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.ticherhaz.karangancemerlangspm.Model.RegisteredUser;

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

    //Button
    private Button buttonSignIn;
    private Button buttonSignUp;


    //Linear Layout
    private LinearLayout linearLayoutNewUser;
    private LinearLayout linearLayoutOlderUser;

    private String userUid;
    private String phoneModel;


    private void listID() {
        textViewUsername = findViewById(R.id.text_view_username);
        //This is for the textview auto gerak kalau nama dia panjang sangat
        textViewUsername.setSelected(true);
        textViewSekolah = findViewById(R.id.text_view_sekolah);
        textViewReputation = findViewById(R.id.text_view_reputation);
        textViewSignOut = findViewById(R.id.text_view_sign_out);

        //Button Initialize
        buttonSignIn = findViewById(R.id.button_sign_in);
        buttonSignUp = findViewById(R.id.button_sign_up);

        linearLayoutNewUser = findViewById(R.id.linear_layout_new_user);
        linearLayoutOlderUser = findViewById(R.id.linear_layout_signed_in_user);

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
            phoneModel = intent.getExtras().getString("phoneModel");
        }

        checkIfUserOnline();
    }


    private void checkIfUserOnline() {
        //Check if already login or not
        if (firebaseUser != null) {
            //already sign in
            linearLayoutOlderUser.setVisibility(View.VISIBLE);
            linearLayoutNewUser.setVisibility(View.GONE);
            textViewUsername.setText(firebaseUser.getDisplayName());

            //Get the data from the database
            databaseReference.child("registeredUser").child("main").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String sekolah = dataSnapshot.getValue(RegisteredUser.class).getSekolah();
                        String reputation = String.valueOf(dataSnapshot.getValue(RegisteredUser.class).getReputation());
                        //Check the online status, if online or not
                        if (dataSnapshot.getValue(RegisteredUser.class).getOnlineStatus().equals("Online")) {
                            textViewUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sign_online_green, 0, 0, 0);
                            textViewUsername.setCompoundDrawablePadding(1);
                        } else {
                            textViewUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sign_online, 0, 0, 0);
                            textViewUsername.setCompoundDrawablePadding(1);
                        }
                        textViewReputation.setText(sekolah);
                        textViewSekolah.setText(reputation);
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
                //Then hide
                linearLayoutNewUser.setVisibility(View.VISIBLE);
                linearLayoutOlderUser.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Sign Out Successfully", Toast.LENGTH_SHORT).show();
            }
        });
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
