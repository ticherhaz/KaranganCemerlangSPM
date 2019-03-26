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
import net.ticherhaz.karangancemerlangspm.Util.InternetCheck;
import net.ticherhaz.karangancemerlangspm.Util.InternetMessage;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    //Variable
    private static final String SHARED_PREFERENCES = "myPreference";
    private static final String SHARED_PREFERENCES_UID = "myPreferenceUid";
    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String uid;
    private String lastSeen;
    private String ipAddress;
    private String phoneModel;

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
        databaseReference = firebaseDatabase.getReference().child("user");

        //Assign the phoneModel
        phoneModel = getDeviceName();
        //Assign the IP Address
        ipAddress = getIPAddress(true);
        //Assign the lastSeen
        lastSeen = Calendar.getInstance().getTime().toString();


        //Call back the shared preference
        //Shared Preference
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        //If already created the uid, then we just need to call back the shared preference
        if (sharedPreferences.contains(SHARED_PREFERENCES_UID)) {
            uid = sharedPreferences.getString(SHARED_PREFERENCES_UID, "");
        } else {
            //If not created yet the shared preference mean, then we create new uid from the database and then we store in the shared preference.
            //So, next time we open the apps, it will the load that already created one.

            //After that we create the pushID for the user
            uid = databaseReference.push().getKey();
            //Then store the uid in the shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SHARED_PREFERENCES_UID, uid);
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
        }, 500);   //So we are making for 0.9 seconds of the splash screen.
    }

    //Method store data of the user into the Firebase
    private void storeUserInfo(final String userUid) {
        //So the new user enter the system, then we collect the new information for the user
        databaseReference.child(uid).child("uid").setValue(uid);
        databaseReference.child(uid).child("phoneModel").setValue(phoneModel);
        databaseReference.child(uid).child("ipAddress").setValue(ipAddress);
        databaseReference.child(uid).child("lastSeen").setValue(lastSeen);

        final String activityLogUid = databaseReference.push().getKey();
        if (activityLogUid != null) {
            databaseReference.child(userUid).child("activityLog").child(activityLogUid).child("loginLog").setValue(lastSeen);
            databaseReference.child(userUid).child("activityLog").child(activityLogUid).child("ipAddressLog").setValue(ipAddress);
        }
        //Then start the main activity and transfer the userUID
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("userUid", userUid);
        intent.putExtra("phoneModel", phoneModel);
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
                    if (system != null && system.getVersi() != 23) {
                        //TODO: Version right now is 23. Please update when the new version is released.
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
            new InternetCheck(new InternetCheck.Consumer() {    //We called the task here (execute here)
                @Override
                public void accept(Boolean internet) {  //After it met the condition about the internet, it will proceed.
                    //Then we go to check the system is it ok or not
                    if (internet) {
                        checkSystem(userUid);
                    } else {
                        //If no connection, then we proceed to store admin info
                        storeUserInfo(userUid);
                    }

                }
            });
        }
    }

    //Get the device name
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    //----------------------------------------------------------------------------

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    //AsyncTask to check the internet connection-------------------------------------
    //TODO: Internet connection AsyncTask, but we transfer. we make a class for the AsyncTask to check the internet [1.2.2019]

}
