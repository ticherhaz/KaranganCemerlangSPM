package net.ticherhaz.karangancemerlangspm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.ticherhaz.karangancemerlangspm.Model.Phone;
import net.ticherhaz.karangancemerlangspm.Model.RegisteredUser;

import java.util.Calendar;
import java.util.Date;

public class SignUpDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private Button buttonSignUp;

    private String initialUid;
    private EditText editTextEmail;
    private EditText editTextUsername;
    private EditText editTextSekolah;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private TextView textViewBirthdayDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private Spinner spinnerState;
    private Spinner spinnerMode;
    private Spinner spinnerGender;
    private ProgressDialog progressDialog;

    private String birthdayMain;
    private Boolean aBooleanBirthday = false;

    //Database
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    //Linear layout kena panggil balik kat forum activity untuk tukar nanti
    private LinearLayout linearLayoutNewUser;
    private LinearLayout linearLayoutOldUser;
    private TextView textViewUsername;
    private TextView textViewSekolah;
    private TextView textViewReputation;

    SignUpDialog(Context context) {
        super(context);
        this.context = context;
    }

    private void checkEmpty() {
        progressDialog.show();
        if (TextUtils.isEmpty(editTextUsername.getText().toString()) || TextUtils.isEmpty(editTextEmail.getText().toString()) || TextUtils.isEmpty(editTextPassword.getText().toString()) || TextUtils.isEmpty(editTextConfirmPassword.getText().toString()) || TextUtils.isEmpty(editTextSekolah.getText().toString())) {
            Toast.makeText(context, "Sila isi tempat kosong", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (!editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
            Toast.makeText(context, "Kata laluan tidak serasi", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (editTextPassword.length() <= 8) {
            Toast.makeText(context, "Kata laluan hendaklah melebihi 8 digit", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (!isEmailValid(editTextEmail.getText().toString())) {
            Toast.makeText(context, "Email tidak sah", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (!aBooleanBirthday) {
            Toast.makeText(context, "Sila pilih tarikh lahir", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            //Correct
            /*
            So I just noticed that we can compare straight to the database at the
            registered user instead creating the new database
            to compare the username.
             */
            //1. We have to check if the username is already taken or not.
            // Query query = databaseReferenceUsernameCheck.orderByChild("usernameUpperCase").equalTo(username.toUpperCase());
            Query query = databaseReference.child("registeredUser").orderByChild("usernameUpperCase").equalTo(editTextUsername.getText().toString().toUpperCase());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(context, "Username already taken", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        //If there is no username yet, then we create the new one

                        //Ok right now we have to transfer the value of the useruid into this part.
                        final String userUid = initialUid;
                        final String typeUser = "Member";
                        final String profileUrl = "https://firebasestorage.googleapis.com/v0/b/karangan-cemerlang-spm.appspot.com/o/profileUrl%2Fmain%2Flogo.png?alt=media&token=c1a7db1e-81ab-4ade-bce3-e620df0ba2d4";
                        final String email = editTextEmail.getText().toString();
                        final String password = editTextPassword.getText().toString();
                        final String username = editTextUsername.getText().toString();
                        final String sekolah = editTextSekolah.getText().toString();
                        final String titleType = "Member";
                        final String customTitle = "";
                        final String bio = "";
                        final String state = spinnerState.getSelectedItem().toString();
                        final String gender = spinnerGender.getSelectedItem().toString();
                        final String birthday = birthdayMain;
                        final String mode = spinnerMode.getSelectedItem().toString();
                        final int postCount = 0;
                        final int reputation = 10;
                        final int reputationPower = 0;
                        final String onlineStatus = "Online";
                        final String lastCreatedThread = "";
                        final String onDateCreated = Calendar.getInstance().getTime().toString();
                        final String onDateCreatedMonthYear = String.valueOf(android.text.format.DateFormat.format("MMMM yyyy", new Date()));

                        //UpperCase
                        final String emailUpperCase = email.toUpperCase();
                        final String usernameUpperCase = username.toUpperCase();
                        final String bioUpperCase = bio.toUpperCase();
                        final String stateUpperCase = state.toUpperCase();

                        //Now we go the method to create the new account
                        createAccount(userUid, typeUser, profileUrl, email, password, username, sekolah, titleType, customTitle, bio, gender, state, birthday, mode, postCount, reputation, reputationPower
                                , onlineStatus, lastCreatedThread, onDateCreated, onDateCreatedMonthYear, emailUpperCase, usernameUpperCase, bioUpperCase, stateUpperCase);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    //Method create Account
    private void createAccount(final String userUid, final String typeUser, final String profileUrl, final String email, final String password, final String username,
                               final String sekolah, final String titleType, final String customTitle, final String bio, final String gender, final String state, final String birthday,
                               final String mode, final int postCount, final int reputation, final int reputationPower, final String onlineStatus, final String lastCreatedThread,
                               final String onDateCreated, final String onDateCreatedMonthYear, final String emailUpperCase, final String usernameUpperCase, final String bioUpperCase, final String stateUpperCase) {

        firebaseAuth.createUserWithEmailAndPassword(email, String.valueOf(password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Get the current user after register the account
                    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                    if (firebaseUser != null) {
                        //Get the new registeredUid after creating the account.
                        final String registeredUserUid = firebaseUser.getUid();
                        final long lastOnline = System.currentTimeMillis();

                        //Store the value in the object
                        RegisteredUser registeredUser = new RegisteredUser(registeredUserUid, userUid, typeUser, profileUrl, email, username, sekolah, titleType, customTitle,
                                bio, gender, state, birthday, mode, postCount, reputation, reputationPower, onlineStatus, lastOnline, lastCreatedThread,
                                onDateCreated, onDateCreatedMonthYear, emailUpperCase, usernameUpperCase, bioUpperCase, stateUpperCase);

                        //Store the value in the database
                        databaseReference.child("registeredUser").child(registeredUserUid).setValue(registeredUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    String phoneUid = databaseReference.push().getKey();
                                    String board = Build.BOARD;
                                    String brand = Build.BRAND;
                                    String device = Build.DEVICE;
                                    String display = Build.DISPLAY;
                                    String fingerprint = Build.FINGERPRINT;
                                    String hardware = Build.HARDWARE;
                                    String host = Build.HOST;
                                    String id = Build.ID;
                                    String manufacturer = Build.MANUFACTURER;
                                    String model = Build.MODEL;
                                    String product = Build.PRODUCT;
                                    String tags = Build.TAGS;
                                    String time = String.valueOf(Build.TIME);
                                    String type = Build.TYPE;
                                    String unknown = Build.UNKNOWN;
                                    String user = Build.USER;

                                    //So after that we store the value for the phone
                                    Phone phone = new Phone(phoneUid, board, brand, device, display, fingerprint,
                                            hardware, host, id, manufacturer, model, product, tags, time, type, unknown, user);
                                    databaseReference.child("phone").child(registeredUserUid).setValue(phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(context, "Successfully registered", Toast.LENGTH_SHORT).show();
                                                //Then close the dialog

                                                //After that store in the firebase user profile request
                                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(username)
                                                        .setPhotoUri(Uri.parse(profileUrl))
                                                        .build();

                                                firebaseUser.updateProfile(userProfileChangeRequest);


                                                Intent intent = new Intent(context, ForumActivity.class);
                                                context.startActivity(intent);
                                                Toast.makeText(context, "Success register: " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                dismiss();
                                                ((ForumActivity) context).finish();
                                            } else {
                                                //Exception if there is problem when to store the value in the database
                                                if (task.getException() != null)
                                                    Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }
                                    });


                                } else {
                                    //Exception if there is problem when to store the value in the database
                                    if (task.getException() != null)
                                        Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }
                } else {
                    //Exception for the Create Account
                    if (task.getException() != null)
                        Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void listID() {
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextUsername = findViewById(R.id.edit_text_username);
        editTextSekolah = findViewById(R.id.edit_text_sekolah);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        spinnerState = findViewById(R.id.spinner_state);
        spinnerMode = findViewById(R.id.spinner_mode);
        spinnerGender = findViewById(R.id.spinner_gender);
        textViewBirthdayDate = findViewById(R.id.text_view_birthday);
        textViewBirthdayDate.setOnClickListener(this);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        ArrayAdapter<CharSequence> adapterState = ArrayAdapter.createFromResource(context,
                R.array.state_array, android.R.layout.simple_spinner_item);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(adapterState);

        ArrayAdapter<CharSequence> adapterMode = ArrayAdapter.createFromResource(context,
                R.array.mode_array, android.R.layout.simple_spinner_item);
        adapterMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMode.setAdapter(adapterMode);

        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(context,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);

        buttonSignUp = findViewById(R.id.button_sign_up);
        buttonSignUp.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_up_dialog);
        listID();
    }

    //Method button
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Button Sign up
            case R.id.button_sign_up:
                checkEmpty();
                break;
            //Button Birthday
            case R.id.text_view_birthday:
                setTextViewBirthdayDate();
                break;
            default:
                break;
        }
    }

    private void setTextViewBirthdayDate() {
        aBooleanBirthday = true;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(context, android.R.style.Theme_DeviceDefault_Light_Dialog, dateSetListener, year, month, day);
        dialog.show();

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthdayMain = dayOfMonth + "/" + month + "/" + year;
                textViewBirthdayDate.setText(String.valueOf(birthdayMain));
            }
        };
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    void setTextViewUsername(TextView textViewUsername) {
        this.textViewUsername = textViewUsername;
    }

    void setTextViewSekolah(TextView textViewSekolah) {
        this.textViewSekolah = textViewSekolah;
    }

    void setTextViewReputation(TextView textViewReputation) {
        this.textViewReputation = textViewReputation;
    }

    void setInitialUid(String initialUid) {
        this.initialUid = initialUid;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    void setLinearLayoutNewUser(LinearLayout linearLayoutNewUser) {
        this.linearLayoutNewUser = linearLayoutNewUser;
    }

    void setLinearLayoutOldUser(LinearLayout linearLayoutOldUser) {
        this.linearLayoutOldUser = linearLayoutOldUser;
    }

}
