package net.ticherhaz.karangancemerlangspm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.zxy.skin.sdk.SkinEngine;

import net.ticherhaz.karangancemerlangspm.skinapplicator.SkinCustomViewApplicator;
import net.ticherhaz.karangancemerlangspm.widget.CustomView;


public class MyApplication extends Application {

    //Variable
    private static final String SHARED_PREFERENCES = "myPreference";
    private static final String SHARED_PREFERENCES_MOD = "myPreferenceMod";
    private String mod;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        SkinEngine.changeSkin(R.style.AppTheme);
        SkinEngine.registerSkinApplicator(CustomView.class, new SkinCustomViewApplicator());
        setSharedPreferences();


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
                SkinEngine.changeSkin(R.style.AppTheme);
                SkinEngine.registerSkinApplicator(CustomView.class, new SkinCustomViewApplicator());
            } else {
                SkinEngine.changeSkin(R.style.AppNightTheme);
                SkinEngine.registerSkinApplicator(CustomView.class, new SkinCustomViewApplicator());
            }
        }
    }
}
