package net.ticherhaz.karangancemerlangspm;

import static net.ticherhaz.karangancemerlangspm.utils.Others.getIPAddress;
import static net.ticherhaz.karangancemerlangspm.utils.Others.isNetworkAvailable;
import static net.ticherhaz.karangancemerlangspm.utils.Others.messageInternetMessage;
import static net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasa;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.ticherhaz.karangancemerlangspm.model.System;
import net.ticherhaz.karangancemerlangspm.model.UserFirst;

public class SplashActivity extends AppCompatActivity {

    //Variable
    private static final String SHARED_PREFERENCES = "myPreference";
    private static final String SHARED_PREFERENCES_UID = "myPreferenceUid";
    private static final String SHARED_PREFERENCES_MOD = "myPreferenceMod";
    public SharedPreferences sharedPreferences;
    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String uid;
    private String mod;
    private String onLoginDate;
    private String ipAddress;

    //Method Initialize the list ID
    private void listID() {
        //Initialize the Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //Assign the phoneModel
        //phoneModel = getDeviceName();
        //Assign the IP Address
        ipAddress = getIPAddress(true);
        //Assign the lastSeen
        onLoginDate = GetTarikhMasa();


        //Call back the shared preference
        //Shared Preference
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        //If already created the uid, then we just need to call back the shared preference
        if (sharedPreferences.contains(SHARED_PREFERENCES_UID)) {
            uid = sharedPreferences.getString(SHARED_PREFERENCES_UID, "");
        }
        if (sharedPreferences.contains(SHARED_PREFERENCES_MOD)) {
            //At this part, we called back the mod because we want to transfer the value of the mod and then change the tick at the menu item
            mod = sharedPreferences.getString(SHARED_PREFERENCES_MOD, "");
        }
        if (!sharedPreferences.contains(SHARED_PREFERENCES_MOD)) {
            mod = "PUTIH";
            //At this part, we called back the mod because we want to transfer the value of the mod and then change the tick at the menu item
            //Then store the uid in the shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SHARED_PREFERENCES_MOD, mod);
            editor.apply();
        }

        if (!sharedPreferences.contains(SHARED_PREFERENCES_UID)) {
            //If not created yet the shared preference mean, then we create new uid from the database and then we store in the shared preference.
            //So, next time we open the apps, it will the load that already created one.

            //After that we create the pushID for the user
            uid = databaseReference.push().getKey();
            //Then store the uid in the shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SHARED_PREFERENCES_UID, uid);
            // editor.putString(SHARED_PREFERENCES_MOD, mod);
            editor.apply();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove the top of the info, that date, notifications stuff
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //Hide the appbar
        //We still need to put this one, because it is appbar not included for the full screen stuff
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        //List all the ID
        listID();
        //Make a countdown for the splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Ok, 1st, we have check the status of the user because with that, the system firebase can detect
                //if the user status login or not, then proceed to the next part WITHOUT INTERNET.
                checkStatusLogin(uid);
            }
        }, 300);   //So we are making for 0.9 seconds of the splash screen.
    }

    //Method store data of the user into the Firebase
    private void storeUserInfo(final String userUid) {
        //So the new user enter the system, then we collect the new information for the user
        String brand = Build.BRAND;
        String model = Build.MODEL;
        String onCreatedDate = GetTarikhMasa();

        //then we call the model class
        final UserFirst userFirst = new UserFirst(userUid, brand, model, ipAddress, onCreatedDate);

        //at this part, we check if the user is already created or not for once.
        databaseReference.child("userFirst").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    databaseReference.child("userFirst").child(userUid).setValue(userFirst);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final String activityLogUid = databaseReference.push().getKey();
        if (activityLogUid != null) {
            //Call the model class
            UserFirst UserFirstAcitivity = new UserFirst(onLoginDate, ipAddress);
            databaseReference.child("userFirstActivity").child(userUid).child(activityLogUid).setValue(UserFirstAcitivity);
        }
        //Then start the main activity and transfer the userUID
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("userUid", userUid);
        intent.putExtra("phoneModel", model);
        intent.putExtra("mod", mod);
        startActivities(new Intent[]{intent});
        //Then close this activity
        finish();
    }


    //Method check the system about the version and mode
    private void checkSystem(final String userUid) {
        //Database Reference to check
        //If the connection is fine then it will proceed this part.
        final DatabaseReference databaseReferenceSystem = firebaseDatabase.getReference().child("system");
        databaseReferenceSystem.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //This type of code is to retrieve all the value from a child.
                //We are using the model class to be assigned from the database.
                //Make sure it is the same variables here and in the database.
                if (dataSnapshot.exists()) {
                    System system = dataSnapshot.getValue(System.class);
                    if (system != null) {


                        if (!system.isMod()) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Di Bawah Penyelenggaran", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        //After that, we chat the value
                        else if (system.getVersi() == 348) {
                            //TODO: Version right now is 348. Please update when the new version is released. testing for beta forum (44)
                            //If all the condition above is met, it will NOT GOING THIS PART INSTEAD THEY WILL GO OUTSIDE FROM THE onDataChange
                            //It not met, then it will proceed here.
                            storeUserInfo(userUid);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Sila mengemas kini versi baharu", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            //put the delay
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Then we proceed to the playStore for user to download the lastest version
                                    final String appPackageName = "net.ticherhaz.karangancemerlangspm"; // Can also use getPackageName(), as below
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (ActivityNotFoundException ex) {
                                        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=net.ticherhaz.karangancemerlangspm&hl=en");
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }

                                }
                            }, 3000); //3 seconds
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //Method check the admin is already login or not
    private void checkStatusLogin(final String userUid) {
        //If the user already signed in
        if (userUid != null) {
            //Show to user to use stable connection
            //Making special toast to center the toast


            messageInternetMessage(SplashActivity.this);

            //If there is connection, then it will check the system
            //So, we using the async task to check the internet, this is the best way to check the internet connection

            //note changes: 6.4.2019 change to old internet check to new one

            if (isNetworkAvailable(SplashActivity.this)) {
                //if yes, there is no connection
                checkSystem(userUid);
            } else {
                //when no connection
                storeUserInfo(userUid);
            }

//           //This old one
//            new InternetCheck(new InternetCheck.Consumer() {    //We called the task here (execute here)
//                @Override
//                public void accept(Boolean internet) {  //After it met the condition about the internet, it will proceed.
//                    //Then we go to check the system is it ok or not
//                    if (internet) {
//
//                    } else {
//                        //If no connection, then we proceed to store admin info
//                        storeUserInfo(userUid);
//                    }
//
//                }
//            });
        }
    }


    //AsyncTask to check the internet connection-------------------------------------

}
