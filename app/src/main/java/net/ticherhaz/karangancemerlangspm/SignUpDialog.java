package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private Activity activity;
    private Button buttonSignUp;


    private String initialUid;
    private EditText editTextEmail;
    private EditText editTextUsername;
    private EditText editTextSekolah;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    //FrameLayout Upload Profile Picture
    //private FrameLayout frameLayoutUploadPicture;
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
    private LinearLayout linearLayoutNewUser;
    private LinearLayout linearLayoutOldUser;
    private TextView textViewUsername;
    private TextView textViewSekolah;
    private TextView textViewReputation;
    //  private ImageView imageViewUploadProfilePicture;

    SignUpDialog(Context context) {
        super(context);
        this.context = context;
    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private void setToast(String message) {
        @SuppressLint("ShowToast") Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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
            editTextPassword.setText("");
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
            // Query query = databaseReferenceUsernameCheck.orderByChild("usernameUpperCase").equalTo(username.toUpperCase());
            Query query = databaseReference.child("registeredUser").orderByChild("usernameUpperCase").equalTo(editTextUsername.getText().toString().toUpperCase());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(context, "Nama Samaran Sudah Diambil", Toast.LENGTH_SHORT).show();
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
                        final String birthday = editTextDay.getText().toString() + "/" + editTextMonth.getText().toString() + "/" + editTextYear.getText().toString();
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
                                                        .setPhotoUri(Uri.parse(profileUrl))
                                                        .build();

                                                firebaseUser.updateProfile(userProfileChangeRequest);


                                                Intent intent = new Intent(context, ForumActivity.class);
                                                intent.putExtra("userUid", userUid);
                                                context.startActivity(intent);
                                                Toast.makeText(context, "Berjaya Daftar " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
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

        //  imageViewUploadProfilePicture = findViewById(R.id.image_view_upload_profile);
        // frameLayoutUploadPicture = findViewById(R.id.frame_layout_upload_picture);

        editTextDay = findViewById(R.id.edit_text_day);
        editTextMonth = findViewById(R.id.edit_text_month);
        editTextYear = findViewById(R.id.edit_text_year);

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

        setEditTextDay();
        //  setFrameLayoutUploadPicture();
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
            default:
                break;
        }
    }

//    private void setFrameLayoutUploadPicture() {
//        frameLayoutUploadPicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickImage();
//            }
//        });
//    }
//
//    @SuppressLint("IntentReset")
//    private void pickImage() {
//        Intent intent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("scale", true);
//        intent.putExtra("outputX", 256);
//        intent.putExtra("outputY", 256);
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("return-data", true);
//        activity.startActivityForResult(intent, 1);
//
//    }


//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.setOwnerActivity(activity);
//        onActivityResult(requestCode, resultCode, data);
//    }


//    public void setonActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//        if (requestCode == 1) {
//            final Bundle extras = data.getExtras();
//            if (extras != null) {
//                //Get image
//                Bitmap newProfilePic = extras.getParcelable("data");
//                Toast.makeText(activity, "SSSS", Toast.LENGTH_LONG).show();
//                Glide.with(activity).load(newProfilePic).into(imageViewUploadProfilePicture);
//            }
//        }
//    }

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
