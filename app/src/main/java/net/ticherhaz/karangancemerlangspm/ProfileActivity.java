package net.ticherhaz.karangancemerlangspm;

import static net.ticherhaz.karangancemerlangspm.utils.ProgressDialogCustom.dismissProgressDialog;
import static net.ticherhaz.karangancemerlangspm.utils.ProgressDialogCustom.showProgressDialog;
import static net.ticherhaz.tarikhmasa.TarikhMasa.ConvertTarikhMasa2LocalTimePattern;
import static net.ticherhaz.tarikhmasa.TarikhMasa.ConvertTimeStamp2TarikhMasa;
import static net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasaTimeAgo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.utils.Others;

import java.util.Locale;

public class ProfileActivity extends SkinActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private String registeredUid;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private TextView tvUsername, tvSekolah, tvTitleType, tvCustomTitle, tvBio, tvGender,
            tvState, tvBirthday, tvMode, tvPostCount, tvReputation, tvReputationPower,
            tvOnlineStatus, tvOnCreatedDate;
    private ImageView ivProfile;
    private Button bDisable;

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;

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
        tvOnCreatedDate = findViewById(R.id.tv_created_date);
        ivProfile = findViewById(R.id.iv_profile);
        bDisable = findViewById(R.id.b_disable);
        setbDisable();

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("registeredUser").child(registeredUid);
    }

    private void enablingButton() {
        FirebaseDatabase.getInstance().getReference().child("registeredUser").child(fUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);
                    if (registeredUser != null) {
                        final String typeUser = registeredUser.getTypeUser();
                        if (typeUser.equals("admin") || typeUser.equals("moderator")) {
                            bDisable.setVisibility(View.VISIBLE);
                            ivProfile.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    AlertDialog ag = new AlertDialog.Builder(ProfileActivity.this)
                                            .setMessage("Are you sure you want to delete this profile image?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    showProgressDialog(ProfileActivity.this);
                                                    //Database
                                                    FirebaseDatabase.getInstance().getReference().child("registeredUser").child(registeredUid).child("profileUrl").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                //After that we remove value in the profile request
                                                                //After that store in the firebase user profile request
                                                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                                                        .setPhotoUri(null)
                                                                        .build();
                                                                fUser.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            dismissProgressDialog();
                                                                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });

                                                            } else {
                                                                dismissProgressDialog();
                                                                if (task.getException() != null)
                                                                    Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(), "Err: %s", task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .create();

                                    ag.show();
                                    ag.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.GREEN);
                                    ag.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.RED);
                                    return true;
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void retrieveIntent() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            registeredUid = intent.getExtras().getString("registeredUid");
        }
    }

    private void retrieveFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);
                    if (registeredUser != null) {
                        final String profileUrl = registeredUser.getProfileUrl();
                        setTitle(registeredUser.getUsername());
                        //Check for image if null or not (profileUrl)
                        if (profileUrl != null) {
                            Glide.with(ProfileActivity.this)
                                    .load(profileUrl)
                                    .into(ivProfile);
                        } else {
                            ivProfile.setImageResource(R.drawable.emblem);
                        }
                        tvUsername.setText(registeredUser.getUsername());

                        switch (registeredUser.getTypeUser()) {
                            case "admin":
                                tvUsername.setTextColor(getResources().getColor(R.color.colorAdmin));
                                break;
                            case "moderator":
                                tvUsername.setTextColor(getResources().getColor(R.color.colorModerator));
                                if (fUser != null) {
                                    enablingButton();
                                }
                                break;
                            case "cikgu":
                                tvUsername.setTextColor(getResources().getColor(R.color.colorCikgu));
                                if (fUser != null) {
                                    enablingButton();
                                }
                                break;
                            case "ahli":
                                //  tvUsername.setTextColor(getResources().getColor(R.color.colorah));
                                if (fUser != null) {
                                    enablingButton();
                                }
                                break;
                            case "ahliPremium":
                                tvUsername.setTextColor(getResources().getColor(R.color.colorAhliPremium));
                                if (fUser != null) {
                                    enablingButton();
                                }
                                break;
                        }

                        tvSekolah.setText(registeredUser.getSekolah());
                        tvGender.setText(registeredUser.getGender());
                        tvTitleType.setText(registeredUser.getTitleType());

                        tvOnlineStatus.setText(registeredUser.getOnlineStatus());
                        //Check the online status, if online or not
                        if (registeredUser.getOnlineStatus().equals("Online")) {
                            tvOnlineStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sign_online_green, 0, 0, 0);
                            tvOnlineStatus.setCompoundDrawablePadding(1);
                            tvOnlineStatus.setText("Dalam Talian");
                        } else {
                            tvOnlineStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sign_online, 0, 0, 0);
                            tvOnlineStatus.setCompoundDrawablePadding(1);
                        }
                        if (registeredUser.getCustomTitle() != null)
                            tvCustomTitle.setText(registeredUser.getCustomTitle());

                        tvCustomTitle.setVisibility(View.GONE);

                        String bioHere;
                        if (registeredUser.getBio() == null) {
                            bioHere = "";
                        } else {
                            bioHere = registeredUser.getBio();
                        }
                        tvBio.setText(String.format(Locale.getDefault(), "Bio: %s", bioHere));
                        tvState.setText(registeredUser.getState());
                        tvBirthday.setText(registeredUser.getBirthday());

                        tvMode.setText(registeredUser.getMode());
                        new Others().setStatus(registeredUser.getMode(), tvMode);

                        tvPostCount.setText(String.format(Locale.getDefault(), "Jumlah Pos: %d", registeredUser.getPostCount()));
                        tvReputation.setText(String.format(Locale.getDefault(), "Reputasi: %d", registeredUser.getReputation()));
                        tvReputationPower.setText(String.format(Locale.getDefault(), "Reputasi Kuasa: %d", registeredUser.getReputationPower()));

                        //Check if online or not
                        if (registeredUser.getOnlineStatus().equals("Offline")) {
                            final String lastOnline = ConvertTimeStamp2TarikhMasa(registeredUser.getLastOnline());
                            final String getAgo = GetTarikhMasaTimeAgo(lastOnline, "MY", true, false);
                            tvOnlineStatus.setText(String.format(Locale.getDefault(), "%s", getAgo));
                        }

                        tvOnCreatedDate.setText(String.format(Locale.getDefault(), "Tarikh Sertai: %s", ConvertTarikhMasa2LocalTimePattern(registeredUser.getOnDateCreated(), "MMM yyyy")));
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

    private void setbDisable() {
        bDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog ag = new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Disable Account")
                        .setMessage("Are you sure you want to disable this account?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                showProgressDialog(ProfileActivity.this);
                                FirebaseDatabase.getInstance().getReference().child("registeredUser").child(registeredUid).child("isActive").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            dismissProgressDialog();
                                            Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(), "Disabled user: %s", registeredUid), Toast.LENGTH_LONG).show();
                                            dialogInterface.dismiss();
                                        } else {
                                            dismissProgressDialog();
                                            if (task.getException() != null)
                                                Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(), "Error: %s", task.getException().getMessage()), Toast.LENGTH_LONG).show();
                                            dialogInterface.dismiss();
                                        }
                                    }
                                });

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();

                ag.show();
                ag.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.GREEN);
                ag.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.RED);
            }
        });
    }

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        retrieveFirebase();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);

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
