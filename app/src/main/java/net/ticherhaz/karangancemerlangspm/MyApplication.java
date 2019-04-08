package net.ticherhaz.karangancemerlangspm;

import android.app.Application;

import com.zxy.skin.sdk.SkinEngine;

import net.ticherhaz.karangancemerlangspm.skinapplicator.SkinCustomViewApplicator;
import net.ticherhaz.karangancemerlangspm.widget.CustomView;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinEngine.changeSkin(R.style.AppTheme);
        SkinEngine.registerSkinApplicator(CustomView.class, new SkinCustomViewApplicator());
    }
}
