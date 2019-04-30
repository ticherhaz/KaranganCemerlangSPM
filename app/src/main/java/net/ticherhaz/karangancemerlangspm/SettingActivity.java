package net.ticherhaz.karangancemerlangspm;

import android.os.Bundle;

import com.zxy.skin.sdk.SkinActivity;

public class SettingActivity extends SkinActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    //OnBackPressed
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
