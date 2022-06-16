package net.ticherhaz.karangancemerlangspm;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import com.zxy.skin.sdk.SkinActivity;

public class AboutUsActivity extends SkinActivity {

    private Button btnRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        btnRating = findViewById(R.id.btn_rate);
        btnRating.setOnClickListener(view -> {
            //Then we proceed to the playStore for user to download the lastest version
            final String appPackageName = "net.ticherhaz.karangancemerlangspm"; // Can also use getPackageName(), as below
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (ActivityNotFoundException ex) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=net.ticherhaz.karangancemerlangspm&hl=en");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}