package net.ticherhaz.karangancemerlangspm;

import static net.ticherhaz.karangancemerlangspm.util.ProgressDialogCustom.dismissProgressDialog;
import static net.ticherhaz.karangancemerlangspm.util.ProgressDialogCustom.showProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.model.RegisteredUser;

public class SignInActivity extends SkinActivity implements View.OnClickListener {

    //Edit text
    private EditText editTextEmailOrUsername;
    private EditText editTextPassword;
    //Button
    private Button buttonSignIn;
    //Database
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    //Linear layout kena panggil balik kat forum activity untuk tukar nanti
//    private LinearLayout linearLayoutNewUser;
//    private LinearLayout linearLayoutOldUser;
//    private TextView textViewUsername;
//    private TextView textViewSekolah;
//    private TextView textViewReputation;
    private String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextEmailOrUsername = findViewById(R.id.edit_text_email_username);
        editTextPassword = findViewById(R.id.edit_text_password);
        buttonSignIn = findViewById(R.id.button_sign_in);
        buttonSignIn.setOnClickListener(this);

        //Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("registeredUser");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_sign_in) {
            checkSignIn();
        }
    }

    //Method check signin
    private void checkSignIn() {
        //Display the progress dialog
        showProgressDialog(SignInActivity.this);
        //Check if user fill in the blank or not
        if (TextUtils.isEmpty(editTextEmailOrUsername.getText().toString()) || TextUtils.isEmpty(editTextPassword.getText().toString())) {
            Toast.makeText(SignInActivity.this, "Sila isikan tempat kosong", Toast.LENGTH_SHORT).show();
            //Dismiss the progress dialog
            dismissProgressDialog();
        } else if (editTextPassword.getText().toString().length() <= 8) {
            Toast.makeText(SignInActivity.this, "Kata Laluan Salah. Sila Cuba Lagi.", Toast.LENGTH_SHORT).show();
            //Dismiss the progress dialog
            dismissProgressDialog();
        } else {
            //When the condition is met.
            //We check if the email or username is exist or not
            final String usernameOrEmailUpperCase = editTextEmailOrUsername.getText().toString().toUpperCase();
            final String password = editTextPassword.getText().toString();

            //OK, change plan, first we check if its valid email or not
            if (isEmailValid(editTextEmailOrUsername.getText().toString())) {
                //If its valid we compare using the email
                Query query = databaseReference.orderByChild("emailUpperCase").equalTo(usernameOrEmailUpperCase);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Check the email is exist or not
                        if (dataSnapshot.exists()) {
                            //If yes, we get the email from the database
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                //Call the class of model
                                RegisteredUser registeredUser = child.getValue(RegisteredUser.class);
                                if (registeredUser != null) {
                                    final String emailUser = registeredUser.getEmail();
                                    final boolean isActive = registeredUser.isActive();

                                    //Here we check if the user is get ban or not
                                    if (isActive) {
                                        signInAuth(emailUser, password);
                                    } else {
                                        Toast.makeText(SignInActivity.this, "Maaf, akaun anda kena ban, sila contact ticherhaz@gmail.com untuk lebih lanjut", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        } else {
                            dismissProgressDialog();
                            Toast.makeText(SignInActivity.this, "Email/Nama Samaran yang anda masukkan tidak sama untuk semua akaun", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                //If its valid we compare using the email
                Query query = databaseReference.orderByChild("usernameUpperCase").equalTo(usernameOrEmailUpperCase);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Check the username is exist or not
                        if (dataSnapshot.exists()) {
                            //If yes, we get the email from the database
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                //Call the class of model
                                RegisteredUser registeredUser = child.getValue(RegisteredUser.class);
                                if (registeredUser != null) {
                                    final String emailUser = registeredUser.getEmail();
                                    final boolean isActive = registeredUser.isActive();

                                    //Here we check if the user is get ban or not
                                    if (isActive) {
                                        signInAuth(emailUser, password);
                                    } else {
                                        Toast.makeText(SignInActivity.this, "Maaf, akaun anda kena ban, sila contact ticherhaz@gmail.com untuk lebih lanjut", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } else {
                            dismissProgressDialog();
                            Toast.makeText(SignInActivity.this, "Email/Nama Samaran yang anda masukkan tidak sama untuk semua akaun", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

    }

    //Method Sign In
    private void signInAuth(final String email, final String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {

                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            final String token = task1.getResult();
                            if (token != null) {
                                FirebaseDatabase.getInstance().getReference().child("registeredUserTokenUid").child(firebaseUser.getUid()).child(token).setValue(true);
                                Toast.makeText(SignInActivity.this, "Selamat Kembali " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                                dismissProgressDialog();
                                startActivity(new Intent(SignInActivity.this, ForumActivity.class));
                                finish();
                            }
                        }
                    });
                }
            } else {
                dismissProgressDialog();
                editTextPassword.setError("Kata Laluan Salah, Sila Cuba Lagi");
            }
        });
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignInActivity.this, ForumActivity.class));
        finish();
    }
}
