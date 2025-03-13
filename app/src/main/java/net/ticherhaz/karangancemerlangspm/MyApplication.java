package net.ticherhaz.karangancemerlangspm;

import static net.ticherhaz.tarikhmasa.TarikhMasa.AndroidThreeTenBP;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDex;


public class MyApplication extends Application {

    //Variable
    private static final String SHARED_PREFERENCES = "myPreference";
    private static final String SHARED_PREFERENCES_MOD = "myPreferenceMod";
    private String mod;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTenBP(this);
        setSharedPreferences();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //At this part we called the shared preference and then we update the themes as soon as user enter the activity
    private void setSharedPreferences() {
        //Call back the shared preference
        //Shared Preference
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        //If already created the uid, then we just need to call back the shared preference
        if (sharedPreferences.contains(SHARED_PREFERENCES_MOD)) {
            mod = sharedPreferences.getString(SHARED_PREFERENCES_MOD, "");
        }

        if (mod != null) {
            if (mod.equals("PUTIH")) {
            } else {
            }
        }
    }
}
