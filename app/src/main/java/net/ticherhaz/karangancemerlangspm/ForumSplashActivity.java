package net.ticherhaz.karangancemerlangspm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class ForumSplashActivity extends AppCompatActivity {

    private String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove the top of the info, that date, notifications stuff
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forum_splash);

        //We still need to put this one, because it is appbar not included for the full screen stuff
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        //Get the value of the userUid
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            userUid = intent.getExtras().getString("userUid");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ForumSplashActivity.this, ForumActivity.class);
                intent.putExtra("userUid", userUid);
                startActivities(new Intent[]{intent});
                finish();
            }
        }, 1100);
    }
}
