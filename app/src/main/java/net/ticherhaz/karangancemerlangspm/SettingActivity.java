package net.ticherhaz.karangancemerlangspm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.ticherhaz.karangancemerlangspm.model.RegisteredUser;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        TextView tvProfile = findViewById(R.id.tv_profile);
        TextView tvChangePassword = findViewById(R.id.tv_change_password);
        final TextView tvHubungiKami = findViewById(R.id.tv_contact_us);
        final TextView tvSenaraiPengguna = findViewById(R.id.tv_admin_contact);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
            FirebaseDatabase.getInstance().getReference().child("registeredUser").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        RegisteredUser registeredUser = dataSnapshot.getValue(RegisteredUser.class);
                        if (registeredUser != null) {
                            if (registeredUser.getTypeUser().equals("admin") || registeredUser.getTypeUser().equals("moderator")) {
                                tvSenaraiPengguna.setVisibility(View.VISIBLE);
                                tvHubungiKami.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        tvProfile.setOnClickListener(view -> startActivity(new Intent(SettingActivity.this, ProfileEditActivity.class)));
        tvChangePassword.setOnClickListener(view -> startActivity(new Intent(SettingActivity.this, ChangePasswordActivity.class)));
        tvHubungiKami.setOnClickListener(view -> startActivity(new Intent(SettingActivity.this, HubungiKamiActivity.class)));
        tvSenaraiPengguna.setOnClickListener(view -> startActivity(new Intent(SettingActivity.this, HubungiKamiUserActivity.class)));
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