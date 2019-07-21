package net.ticherhaz.karangancemerlangspm;

import android.os.Bundle;

import com.zxy.skin.sdk.SkinActivity;

public class ChangePasswordActivity extends SkinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

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
