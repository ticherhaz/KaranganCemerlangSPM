package net.ticherhaz.karangancemerlangspm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.ticherhaz.karangancemerlangspm.Model.RegisteredUser;

import static net.ticherhaz.karangancemerlangspm.Util.ProgressDialogCustom.dismissProgressDialog;
import static net.ticherhaz.karangancemerlangspm.Util.ProgressDialogCustom.showProgressDialog;

public class SignInDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private Activity activity;
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
    private LinearLayout linearLayoutNewUser;
    private LinearLayout linearLayoutOldUser;
    private TextView textViewUsername;
    private TextView textViewSekolah;
    private TextView textViewReputation;
    private String userUid;

    public SignInDialog(Context context) {
        super(context);
        this.context = context;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
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

    public TextView getTextViewUsername() {
        return textViewUsername;
    }

    public void setTextViewUsername(TextView textViewUsername) {
        this.textViewUsername = textViewUsername;
    }

    public TextView getTextViewSekolah() {
        return textViewSekolah;
    }

    public void setTextViewSekolah(TextView textViewSekolah) {
        this.textViewSekolah = textViewSekolah;
    }

    public TextView getTextViewReputation() {
        return textViewReputation;
    }

    public void setTextViewReputation(TextView textViewReputation) {
        this.textViewReputation = textViewReputation;
    }

    //Method listID
    private void listID() {
        editTextEmailOrUsername = findViewById(R.id.edit_text_email_username);
        editTextPassword = findViewById(R.id.edit_text_password);
        buttonSignIn = findViewById(R.id.button_sign_in);
        buttonSignIn.setOnClickListener(this);

        //Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("registeredUser");
        firebaseAuth = FirebaseAuth.getInstance();
    }


    //Method check signin
    private void checkSignIn() {
        //Display the progress dialog
        showProgressDialog(activity);
        //Check if user fill in the blank or not
        if (TextUtils.isEmpty(editTextEmailOrUsername.getText().toString()) || TextUtils.isEmpty(editTextPassword.getText().toString())) {
            Toast.makeText(context, "Sila isikan tempat kosong", Toast.LENGTH_SHORT).show();
            //Dismiss the progress dialog
            dismissProgressDialog();
        } else if (editTextPassword.getText().toString().length() <= 8) {
            Toast.makeText(context, "Kata Laluan Salah. Sila Cuba Lagi.", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(context, "Maaf, akaun anda kena ban, sila contact ticherhaz@gmail.com untuk lebih lanjut", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        } else {
                            dismissProgressDialog();
                            Toast.makeText(context, "Email/Nama Samaran yang anda masukkan tidak sama untuk semua akaun", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(context, "Maaf, akaun anda kena ban, sila contact ticherhaz@gmail.com untuk lebih lanjut", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } else {
                            dismissProgressDialog();
                            Toast.makeText(context, "Email/Nama Samaran yang anda masukkan tidak sama untuk semua akaun", Toast.LENGTH_SHORT).show();
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
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        Intent intent = new Intent(context, ForumActivity.class);
                        intent.putExtra("userUid", userUid);
                        context.startActivity(intent);
                        Toast.makeText(context, "Selamat Kembali " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                        dismiss();
                        ((ForumActivity) context).finish();
                    }
                } else {
                    dismissProgressDialog();
                    editTextPassword.setError("Kata Laluan Salah, Sila Cuba Lagi");
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_sign_in) {
            checkSignIn();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_in_dialog);
        listID();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
