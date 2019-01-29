package net.ticherhaz.karangancemerlangspm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TipsKaranganActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_karangan);

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
