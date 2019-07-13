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

import java.util.Locale;

public class ProfileActivity extends SkinActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private String registeredUid, username;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private TextView tvUsername, tvSekolah, tvTitleType, tvCustomTitle, tvBio, tvGender,
            tvState, tvBirthday, tvMode, tvPostCount, tvReputation, tvReputationPower,
            tvOnlineStatus, tvLastOnline, tvOnCreatedDate;
    private ImageView imageViewProfile;

    private void listID() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        setSwipeRefreshLayout();
        tvUsername = findViewById(R.id.tv_username);
        tvSekolah = findViewById(R.id.tv_sekolah);
        tvGender = findViewById(R.id.tv_gender);
        tvTitleType = findViewById(R.id.tv_title_type);
        tvOnlineStatus = findViewById(R.id.tv_online_status);
        tvCustomTitle = findViewById(R.id.tv_custom_title);
        tvBio = findViewById(R.id.tv_bio);
        tvState = findViewById(R.id.tv_state);
        tvBirthday = findViewById(R.id.tv_birthday);
        tvMode = findViewById(R.id.tv_mode);
        tvPostCount = findViewById(R.id.tv_post_count);
        tvReputation = findViewById(R.id.tv_reputation);
        tvReputationPower = findViewById(R.id.tv_reputation_power);
        tvLastOnline = findViewById(R.id.tv_last_online);
        tvOnCreatedDate = findViewById(R.id.tv_created_date);
        imageViewProfile = findViewById(R.id.iv_profile);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("registeredUser").child(registeredUid);
    }

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
                        tvUsername.setText(registeredUser.getUsername());
                        tvSekolah.setText(registeredUser.getSekolah());
                        tvGender.setText(registeredUser.getGender());
                        tvTitleType.setText(registeredUser.getTitleType());
                        tvOnlineStatus.setText(registeredUser.getOnlineStatus());
                        tvCustomTitle.setText(registeredUser.getCustomTitle());
                        tvBio.setText(registeredUser.getBio());
                        tvState.setText(registeredUser.getState());
                        tvBirthday.setText(registeredUser.getBirthday());
                        tvMode.setText(registeredUser.getMode());
                        tvPostCount.setText(String.format(Locale.getDefault(), "%d", registeredUser.getPostCount()));
                        tvReputation.setText(String.format(Locale.getDefault(), "%d", registeredUser.getReputation()));
                        tvReputationPower.setText(String.format(Locale.getDefault(), "%d", registeredUser.getReputationPower()));
                        tvLastOnline.setText(String.format(Locale.getDefault(), "%d", registeredUser.getLastOnline()));
                        tvOnCreatedDate.setText(registeredUser.getOnDateCreated());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
