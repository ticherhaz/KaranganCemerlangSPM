package net.ticherhaz.karangancemerlangspm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.ticherhaz.karangancemerlangspm.Model.System;
import net.ticherhaz.karangancemerlangspm.Model.UserAlpha;
import net.ticherhaz.karangancemerlangspm.Util.InternetMessage;
import net.ticherhaz.karangancemerlangspm.Util.Others;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

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
    private String lastSeen;
    private String ipAddress;

    //Method to get IP Address
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {

        } // for now eat exceptions
        return "";
    }


    //Method Initialize the list ID
    private void listID() {
        //Initialize the Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //Assign the phoneModel
        // phoneModel = getDeviceName();
        //Assign the IP Address
        ipAddress = getIPAddress(true);
        //Assign the lastSeen
        lastSeen = Calendar.getInstance().getTime().toString();


        //Call back the shared preference
        //Shared Preference
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        //If already created the uid, then we just need to call back the shared preference
        if (sharedPreferences.contains(SHARED_PREFERENCES_UID) && sharedPreferences.contains(SHARED_PREFERENCES_MOD)) {
            uid = sharedPreferences.getString(SHARED_PREFERENCES_UID, "");
            //At this part, we called back the mod because we want to transfer the value of the mod and then change the tick at the menu item
            mod = sharedPreferences.getString(SHARED_PREFERENCES_MOD, "");
        } else {
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
//        if (mod != null) {
//            if (mod.equals("PUTIH")) {
//
//                SkinEngine.changeSkin(R.style.AppTheme);
//            } else {
//                SkinEngine.changeSkin(R.style.AppNightTheme);
//            }
//        }
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
        }, 500);   //So we are making for 0.9 seconds of the splash screen.
    }

    //Method store data of the user into the Firebase
    private void storeUserInfo(final String userUid) {
        //So the new user enter the system, then we collect the new information for the user
        String brand = Build.BRAND;
        String model = Build.MODEL;

        //then we call the model class
        final UserAlpha userAlpha = new UserAlpha(userUid, brand, model, ipAddress, lastSeen);

        //at this part, we check if the user is already created or not for once.
        databaseReference.child("userAlpha").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    databaseReference.child("userAlpha").child(userUid).setValue(userAlpha);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final String activityLogUid = databaseReference.push().getKey();
        if (activityLogUid != null) {
            //Call the model class
            UserAlpha userAlphaAcitivity = new UserAlpha(lastSeen, ipAddress);
            databaseReference.child("userAlphaActivity").child(userUid).child(activityLogUid).setValue(userAlphaAcitivity);
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
                    if (system != null && !system.isMod()) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Di Bawah Penyelenggaran", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        //Then we return to out of this databaseReferenceSystem.
                        return;
                    }
                    //After that, we chat the value
                    if (system != null && system.getVersi() != 28) {
                        //TODO: Version right now is 28. Please update when the new version is released.
                        Toast toast = Toast.makeText(getApplicationContext(), "Sila mengemas kini versi baharu", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        //put the delay
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Then we proceed to the playStore for user to download the lastest version
                                final String appPackageName = "net.ticherhaz.karangancemerlangspm"; // Can also use getPackageName(), as below
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            }
                        }, 3000); //3 seconds
                        return;
                    }

                    //If all the condition above is met, it will NOT GOING THIS PART INSTEAD THEY WILL GO OUTSIDE FROM THE onDataChange
                    //It not met, then it will proceed here.
                    storeUserInfo(userUid);
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

            Toast.makeText(getApplicationContext(), new InternetMessage().getMessage(), Toast.LENGTH_SHORT).show();

            //If there is connection, then it will check the system
            //So, we using the async task to check the internet, this is the best way to check the internet connection
            //TODO: Internet connection

            //note changes: 6.4.2019 change to old internet check to new one

            if (new Others().isNetworkAvailable(getApplicationContext())) {
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
    //TODO: Internet connection AsyncTask, but we transfer. we make a class for the AsyncTask to check the internet [1.2.2019]

}
