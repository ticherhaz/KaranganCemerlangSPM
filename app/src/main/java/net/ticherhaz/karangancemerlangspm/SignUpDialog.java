package net.ticherhaz.karangancemerlangspm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.ticherhaz.karangancemerlangspm.Model.RegisteredUser;

import java.util.Calendar;

public class SignUpDialog extends Dialog implements View.OnClickListener {

    public Context context;
    //   public Dialog dialog;
    private Button buttonSignUp;

    private String initialUid;
    private EditText editTextEmail;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    //Database
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //Linear layout kena panggil balik kat forum activity untuk tukar nanti
    private LinearLayout linearLayoutNewUser;
    private LinearLayout linearLayoutOldUser;

    SignUpDialog(Context context) {
        super(context);
        this.context = context;
    }

    public String getInitialUid() {
        return initialUid;
    }

    public void setInitialUid(String initialUid) {
        this.initialUid = initialUid;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Button getButtonSignUp() {
        return buttonSignUp;
    }

    public void setButtonSignUp(Button buttonSignUp) {
        this.buttonSignUp = buttonSignUp;
    }

    public EditText getEditTextEmail() {
        return editTextEmail;
    }

    public void setEditTextEmail(EditText editTextEmail) {
        this.editTextEmail = editTextEmail;
    }

    public EditText getEditTextUsername() {
        return editTextUsername;
    }

    public void setEditTextUsername(EditText editTextUsername) {
        this.editTextUsername = editTextUsername;
    }

    public EditText getEditTextPassword() {
        return editTextPassword;
    }

    public void setEditTextPassword(EditText editTextPassword) {
        this.editTextPassword = editTextPassword;
    }

    public EditText getEditTextConfirmPassword() {
        return editTextConfirmPassword;
    }

    public void setEditTextConfirmPassword(EditText editTextConfirmPassword) {
        this.editTextConfirmPassword = editTextConfirmPassword;
    }

    private void checkEmpty() {
        if (TextUtils.isEmpty(editTextUsername.getText().toString()) || TextUtils.isEmpty(editTextEmail.getText().toString()) || TextUtils.isEmpty(editTextPassword.getText().toString()) || TextUtils.isEmpty(editTextConfirmPassword.getText().toString())) {
            Toast.makeText(context, "Please fill in the blank", Toast.LENGTH_SHORT).show();
        } else if (!editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
            Toast.makeText(context, "Password not match", Toast.LENGTH_SHORT).show();
        } else if (editTextPassword.length() <= 8) {
            Toast.makeText(context, "Password need exceed 8 digits", Toast.LENGTH_SHORT).show();
        } else if (!isEmailValid(editTextEmail.getText().toString())) {
            Toast.makeText(context, "Not email valid", Toast.LENGTH_SHORT).show();
        } else {
            //Correct

            //1. We have to check if the username is already taken or not.
            DatabaseReference databaseReferenceUsernameCheck = FirebaseDatabase.getInstance().getReference().child("UsernameCheck");
            databaseReferenceUsernameCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //String registeredUserUid, String userUid, String typeUser, String profileUrl, String email, String username, String titleType, String customTitle, String bio, String state, String birthday, String mode, int postCount, int reputation, int reputationPower, String onlineStatus, String lastCreatedThread, String onDateCreated, String onlineStatusLogUid, String lastSeenLogUid, String onClickedLogUid, String onCreatedThreadLogUid, String profileUrlLogUid, String emailLogUid, String usernameLogUid, String titleTypeLogUid, String customTitleLogUid, String bioLogUid, String stateLogUid, String birthdayLogUid, String modeLogUid, String emailUpperCase, String usernameUpperCase, String bioUpperCase, String stateUpperCase
                    String email = editTextEmail.getText().toString();
                    String username = editTextUsername.getText().toString();
                    String onDateCreated = Calendar.getInstance().getTime().toString();

                    if (dataSnapshot.exists()) ;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void createAccount(final String userUid, final String typeUser, final String profileUrl, final String email, String password, final String username, final String titleType, final String customTitle, final String bio, final String state, final String birthday, final String mode, final int postCount, final int reputation, final int reputationPower, final String onlineStatus, final String lastCreatedThread, final String onDateCreated, final String onlineStatusLogUid, final String lastSeenLogUid, final String onClickedLogUid, final String onCreatedThreadLogUid, final String profileUrlLogUid, final String emailLogUid, final String usernameLogUid, final String titleTypeLogUid, final String customTitleLogUid, final String bioLogUid, final String stateLogUid, final String birthdayLogUid, final String modeLogUid, final String emailUpperCase, final String usernameUpperCase, final String bioUpperCase, final String stateUpperCase) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        String registeredUserUid = firebaseUser.getUid();

                        RegisteredUser registeredUser = new RegisteredUser(registeredUserUid, userUid, typeUser, profileUrl, email, username, titleType, customTitle,
                                bio, state, birthday, mode, postCount, reputation, reputationPower, onlineStatus, lastCreatedThread,
                                onDateCreated, onlineStatusLogUid, lastSeenLogUid, onClickedLogUid, onCreatedThreadLogUid, profileUrlLogUid,
                                emailLogUid, usernameLogUid, titleTypeLogUid, customTitleLogUid, bioLogUid, stateLogUid, birthdayLogUid,
                                modeLogUid, emailUpperCase, usernameUpperCase, bioUpperCase, stateUpperCase);

                        databaseReference.child(registeredUserUid).setValue(registeredUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Successfully registered", Toast.LENGTH_SHORT).show();
                                    //Then close the dialog
                                    linearLayoutNewUser.setVisibility(View.GONE);
                                    linearLayoutOldUser.setVisibility(View.VISIBLE);


                                    dismiss();
                                } else {
                                    if (task.getException() != null)
                                        Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }


                } else {
                    if (task.getException() != null)
                        Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public LinearLayout getLinearLayoutNewUser() {
        return linearLayoutNewUser;
    }

    public void setLinearLayoutNewUser(LinearLayout linearLayoutNewUser) {
        this.linearLayoutNewUser = linearLayoutNewUser;
    }

    public LinearLayout getLinearLayoutOldUser() {
        return linearLayoutOldUser;
    }

    public void setLinearLayoutOldUser(LinearLayout linearLayoutOldUser) {
        this.linearLayoutOldUser = linearLayoutOldUser;
    }

    private void listID() {
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextUsername = findViewById(R.id.edit_text_username);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);

        buttonSignUp = findViewById(R.id.button_sign_up);
        buttonSignUp.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("registeredUser").child("main");

        firebaseAuth = FirebaseAuth.getInstance();
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_up_dialog);
        listID();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_up:
                checkEmpty();
                break;
            default:
                break;
        }

    }
}
