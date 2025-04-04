package net.ticherhaz.karangancemerlangspm;

import static net.ticherhaz.karangancemerlangspm.utils.Others.isNetworkAvailable;
import static net.ticherhaz.karangancemerlangspm.utils.Others.messageInternetMessage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.Crop;

import net.ticherhaz.karangancemerlangspm.model.RegisteredUser;

import java.io.File;

public class ProfileEditActivity extends AppCompatActivity {

    private FirebaseDatabase fDe;
    private DatabaseReference dRe;
    private FirebaseStorage fSe;
    private StorageReference sRe;
    private FirebaseAuth fA;
    private FirebaseUser fU;
    private ImageView iVProfile, iVUploadProfile;
    private Uri pUrl;
    private Button bUpdate;
    private TextInputEditText tietBio, tietSekolah;
    private String registeredUid;

    private void listID() {
        iVProfile = findViewById(R.id.iv_profile);
        iVUploadProfile = findViewById(R.id.iv_upload_profile);
        bUpdate = findViewById(R.id.b_update);
        tietBio = findViewById(R.id.tiet_bio);
        tietSekolah = findViewById(R.id.tiet_sekolah);
        fDe = FirebaseDatabase.getInstance();
        dRe = fDe.getReference();
        fSe = FirebaseStorage.getInstance();
        sRe = fSe.getReference();
        fA = FirebaseAuth.getInstance();
        fU = fA.getCurrentUser();
        if (fU != null)
            registeredUid = fU.getUid();
        retrieveFirebase();
        setbUpload();
        setiVUploadProfile();
    }

    private void retrieveFirebase() {
        if (fU != null) {
            dRe.child("registeredUser").child(registeredUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        RegisteredUser user = dataSnapshot.getValue(RegisteredUser.class);
                        if (user != null) {
                            final String profileUrl = user.getProfileUrl();
                            //Check for image if null or not (profileUrl)
                            if (profileUrl != null) {
                                Glide.with(getApplicationContext())
                                        .load(profileUrl)
                                        .into(iVProfile);
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void setTietSekolah() {
        if (tietSekolah.getText() != null)
            if (!TextUtils.isEmpty(tietSekolah.getText().toString())) {
                final String bio = tietSekolah.getText().toString();
                //And then update data in database
                dRe.child("registeredUser").child(registeredUid).child("sekolah").setValue(bio).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Berjaya mengemas kini sekolah", Toast.LENGTH_SHORT).show();
                            tietSekolah.getText().clear();
                        }
                    }
                });
            }
    }

    private void setTietBio() {
        if (tietBio.getText() != null)
            if (!TextUtils.isEmpty(tietBio.getText().toString())) {
                final String bio = tietBio.getText().toString();
                //And then update data in database
                dRe.child("registeredUser").child(registeredUid).child("bio").setValue(bio).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Berjaya mengemas kini bio", Toast.LENGTH_SHORT).show();
                            tietBio.getText().clear();
                        }
                    }
                });
            }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        // progressDialog.setTitle("Memuat Naik Gambar Profil...");
        progressDialog.show();
        if (pUrl != null) {
            final String uploadUid = dRe.push().getKey();
            final StorageReference ref = sRe.child("profileImage/" + registeredUid + "/" + uploadUid);
            ref.putFile(pUrl)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            // dismissProgressDialog();
                            Toast.makeText(ProfileEditActivity.this, "Gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        if (uri != null) {
                                            String profileStoredUrl = String.valueOf(uri);

                                            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                                    .setPhotoUri(Uri.parse(profileStoredUrl))
                                                    .build();

                                            fU.updateProfile(userProfileChangeRequest);

                                            //Store in registeredUser
                                            dRe.child("registeredUser").child(registeredUid).child("profileUrl").setValue(profileStoredUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        progressDialog.dismiss();
                                                        recreate();
                                                    }
                                                }
                                            });

                                        }

                                    }
                                });

                            }

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Memuat Naik Gambar: " + (int) progress + "%");
                        }
                    });
        } else {
            progressDialog.dismiss();
        }
    }

    private void setiVUploadProfile() {
        iVUploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Crop.pickImage(ProfileEditActivity.this);
            }
        });
    }

    private void setbUpload() {
        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(ProfileEditActivity.this)) {
                    setTietBio();
                    setTietSekolah();
                    uploadImage();
                } else {
                    //No connection
                    messageInternetMessage(ProfileEditActivity.this);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        listID();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            assert data != null;
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            pUrl = Crop.getOutput(result);
            iVProfile.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
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
