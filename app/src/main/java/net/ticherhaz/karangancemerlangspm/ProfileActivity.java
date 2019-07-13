package net.ticherhaz.karangancemerlangspm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.Model.RegisteredUser;

public class ProfileActivity extends SkinActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private String registeredUid, username;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private TextView textViewUsername;
    private TextView textViewSekolah;
    private TextView textViewGender;
    private TextView textViewTitleType;
    private TextView textViewStatus;
    private ImageView imageViewProfile;

    private void retrieveIntent() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            registeredUid = intent.getExtras().getString("registeredUid");
            username = intent.getExtras().getString("username");
            //Change title of the toolbar
            setTitle(username);
        }
    }

    private void retrieveFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);
                    if (registeredUser != null) {
                        final String profileUrl = registeredUser.getProfileUrl();

                        //Check for image if null or not (profileUrl)
                        if (profileUrl != null) {
                            Glide.with(ProfileActivity.this)
                                    .load(profileUrl)
                                    .into(imageViewProfile);
                        }
                        textViewUsername.setText(registeredUser.getUsername());
                        textViewSekolah.setText(registeredUser.getSekolah());
                        textViewGender.setText(registeredUser.getGender());
                        textViewTitleType.setText(registeredUser.getTitleType());
                        textViewStatus.setText(registeredUser.getOnlineStatus());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void listID() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        setSwipeRefreshLayout();
        textViewUsername = findViewById(R.id.text_view_username);
        textViewSekolah = findViewById(R.id.text_view_sekolah);
        textViewGender = findViewById(R.id.text_view_gender);
        textViewTitleType = findViewById(R.id.text_view_user_title);
        textViewStatus = findViewById(R.id.text_view_status);
        imageViewProfile = findViewById(R.id.image_view_profile);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("registeredUser").child(registeredUid);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        retrieveIntent();
        listID();
        retrieveFirebase();
    }

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

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
