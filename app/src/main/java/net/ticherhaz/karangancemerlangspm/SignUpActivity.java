package net.ticherhaz.karangancemerlangspm;

import static net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasa;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.model.Phone;
import net.ticherhaz.karangancemerlangspm.model.RegisteredUser;

public class SignUpActivity extends SkinActivity implements View.OnClickListener {

    private Button buttonSignUp;

    private String initialUid;
    private EditText editTextEmail;
    private EditText editTextUsername;
    private EditText editTextSekolah;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    //Birthday
    private EditText editTextDay;
    private EditText editTextMonth;
    private EditText editTextYear;
    private Spinner spinnerState;
    private Spinner spinnerMode;
    private Spinner spinnerGender;
    private ProgressDialog progressDialog;
    //Database
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    //Linear layout kena panggil balik kat forum activity untuk tukar nanti
    private TextView textViewRules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextUsername = findViewById(R.id.edit_text_username);
        editTextSekolah = findViewById(R.id.edit_text_sekolah);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        spinnerState = findViewById(R.id.spinner_state);
        spinnerMode = findViewById(R.id.spinner_mode);
        spinnerGender = findViewById(R.id.spinner_gender);

        textViewRules = findViewById(R.id.text_view_rules);
        //This is to enable the scroll view at textview
        textViewRules.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // Disallow the touch request for parent scroll on touch of child view
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        textViewRules.setText(Html.fromHtml(getString(R.string.rules_terms)));

        editTextDay = findViewById(R.id.edit_text_day);
        editTextMonth = findViewById(R.id.edit_text_month);
        editTextYear = findViewById(R.id.edit_text_year);

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Memuatkan...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        ArrayAdapter<CharSequence> adapterState = ArrayAdapter.createFromResource(SignUpActivity.this,
                R.array.state_array, R.layout.spinner_item);
        adapterState.setDropDownViewResource(R.layout.spinner_item);
        spinnerState.setAdapter(adapterState);

        ArrayAdapter<CharSequence> adapterMode = ArrayAdapter.createFromResource(SignUpActivity.this,
                R.array.mode_array, R.layout.spinner_item);
        adapterMode.setDropDownViewResource(R.layout.spinner_item);
        spinnerMode.setAdapter(adapterMode);

        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(SignUpActivity.this,
                R.array.gender_array, R.layout.spinner_item);
        adapterGender.setDropDownViewResource(R.layout.spinner_item);
        spinnerGender.setAdapter(adapterGender);

        buttonSignUp = findViewById(R.id.button_sign_up);
        buttonSignUp.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        setEditTextDay();
    }

    private void setEditTextDay() {
        editTextDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    editTextMonth.requestFocus();
                }
            }
        });
        editTextMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    editTextYear.requestFocus();
                }
            }
        });
    }

    private void checkEmpty() {
        if (TextUtils.isEmpty(editTextUsername.getText().toString()) || TextUtils.isEmpty(editTextEmail.getText().toString())
                || TextUtils.isEmpty(editTextPassword.getText().toString()) || TextUtils.isEmpty(editTextConfirmPassword.getText().toString())
                || TextUtils.isEmpty(editTextSekolah.getText().toString()) || TextUtils.isEmpty(editTextDay.getText().toString()) ||
                TextUtils.isEmpty(editTextMonth.getText().toString()) || TextUtils.isEmpty(editTextYear.getText().toString())) {
            setToast("Sila isi tempat kosong");
            editTextUsername.requestFocus();
        } else if (editTextUsername.getText().toString().length() <= 3) {
            setToast("Nama Samaran Hendaklah Melebihi 3 Digit");
            editTextUsername.requestFocus();
        } else if (editTextUsername.getText().toString().length() >= 13) {
            setToast("Nama Samaran Hendaklah Kurang 13 Digit");
            editTextUsername.requestFocus();
        } else if (!isEmailValid(editTextEmail.getText().toString())) {
            setToast("Email tidak sah");
            editTextEmail.setText("");
            editTextEmail.requestFocus();
        } else if (!editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
            setToast("Kata laluan tidak serasi");
            editTextConfirmPassword.setText("");
            editTextConfirmPassword.requestFocus();
        } else if (editTextPassword.length() <= 8) {
            setToast("Kata laluan hendaklah melebihi 8 digit");
            editTextPassword.getText().clear();
            editTextConfirmPassword.getText().clear();
            editTextPassword.requestFocus();
        } else if (Integer.parseInt(editTextDay.getText().toString()) == 0 || Integer.parseInt(editTextDay.getText().toString()) >= 32 ||
                Integer.parseInt(editTextMonth.getText().toString()) == 0 || Integer.parseInt(editTextMonth.getText().toString()) >= 13 ||
                Integer.parseInt(editTextYear.getText().toString()) <= 1911 || Integer.parseInt(editTextMonth.getText().toString()) >= 2017) {
            setToast("Sila isi tarikh lahir format yang betul");
            editTextDay.setText("");
            editTextMonth.setText("");
            editTextYear.setText("");
            editTextDay.requestFocus();
        } else {
            progressDialog.show();
            //Correct
            /*
            So I just noticed that we can compare straight to the database at the
            registered user instead creating the new database
            to compare the username.
             */
            //1. We have to check if the username is already taken or not.
            Query query = databaseReference.child("registeredUser").orderByChild("usernameUpperCase").equalTo(editTextUsername.getText().toString().toUpperCase());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(SignUpActivity.this, "Nama Samaran Sudah Diambil", Toast.LENGTH_SHORT).show();
                        editTextUsername.getText().clear();
                        editTextUsername.requestFocus();
                        progressDialog.dismiss();
                    } else {
                        //If there is no username yet, then we go compare the email
                        //2. We have to check if the email is already taken or not.
                        Query queryEmail = databaseReference.child("registeredUser").orderByChild("emailUpperCase").equalTo(editTextEmail.getText().toString().toUpperCase());
                        queryEmail.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    //If got
                                    setToast("Email Sudah Diambil");
                                    editTextEmail.getText().clear();
                                    editTextEmail.requestFocus();
                                    progressDialog.dismiss();
                                } else {
                                    //Create new
                                    //Ok right now we have to transfer the value of the useruid into this part.
                                    final String userUid = initialUid;
                                    final String typeUser = "ahli";
                                    final String profileUrl = null;
                                    final String email = editTextEmail.getText().toString();
                                    final String password = editTextPassword.getText().toString();
                                    final String username = editTextUsername.getText().toString();
                                    final String sekolah = editTextSekolah.getText().toString();
                                    final String titleType = "Ahli";
                                    final String customTitle = null;
                                    final String bio = null;
                                    final String state = spinnerState.getSelectedItem().toString();
                                    final String gender = spinnerGender.getSelectedItem().toString();
                                    final String birthday = editTextDay.getText().toString() + "/" + editTextMonth.getText().toString() + "/" + editTextYear.getText().toString();
                                    final String mode = spinnerMode.getSelectedItem().toString();
                                    final int postCount = 0;
                                    final int reputation = 10;
                                    final int reputationPower = 0;
                                    final String onlineStatus = "Online";
                                    final String lastCreatedThread = null;
                                    final String onDateCreated = GetTarikhMasa();

                                    //UpperCase
                                    final String emailUpperCase = email.toUpperCase();
                                    final String usernameUpperCase = username.toUpperCase();
                                    final String bioUpperCase = null;
                                    final String stateUpperCase = state.toUpperCase();

                                    //Now we go the method to create the new account
                                    createAccount(userUid, typeUser, profileUrl, email, password, username, sekolah, titleType, customTitle, bio, gender, state, birthday, mode, postCount, reputation, reputationPower, onlineStatus, lastCreatedThread, onDateCreated, emailUpperCase, usernameUpperCase, bioUpperCase, stateUpperCase);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                setToast("Error: " + databaseError.getDetails());
                                progressDialog.dismiss();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    setToast("Error: " + databaseError.getDetails());
                    progressDialog.dismiss();
                }
            });
        }
    }

    //Method create Account
    private void createAccount(final String userUid, final String typeUser, final String profileUrl, final String email, final String password, final String username,
                               final String sekolah, final String titleType, final String customTitle, final String bio, final String gender, final String state, final String birthday,
                               final String mode, final int postCount, final int reputation, final int reputationPower, final String onlineStatus, final String lastCreatedThread,
                               final String onDateCreated, final String emailUpperCase, final String usernameUpperCase, final String bioUpperCase, final String stateUpperCase) {

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
                                onDateCreated, emailUpperCase, usernameUpperCase, bioUpperCase, stateUpperCase, true);

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
                                    final String id = Build.ID;
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

                                                //After that store in the firebase user profile request
                                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(username)
                                                        //  .setPhotoUri(Uri.parse(profileUrl))
                                                        .build();

                                                firebaseUser.updateProfile(userProfileChangeRequest);

//
//                                                Intent intent = new Intent(context, ForumActivity.class);
//                                                intent.putExtra("userUid", userUid);
//                                                context.startActivity(intent);
                                                Toast.makeText(SignUpActivity.this, "Berjaya Daftar " + username, Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();

                                                startActivity(new Intent(SignUpActivity.this, ForumActivity.class));
                                                finish();
                                            } else {
                                                //Exception if there is problem when to store the value in the database
                                                if (task.getException() != null)
                                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }
                                    });


                                } else {
                                    //Exception if there is problem when to store the value in the database
                                    if (task.getException() != null)
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }
                } else {
                    //Exception for the Create Account
                    if (task.getException() != null)
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpActivity.this, ForumActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        //Button Sign up
        if (v.getId() == R.id.button_sign_up) {
            checkEmpty();
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void setToast(String message) {
        @SuppressLint("ShowToast") Toast toast = Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
