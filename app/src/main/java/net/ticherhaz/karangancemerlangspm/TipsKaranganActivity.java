package net.ticherhaz.karangancemerlangspm;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.zxy.skin.sdk.SkinActivity;

public class TipsKaranganActivity extends SkinActivity {

    private Button buttonIncreaseSize;
    private Button buttonDecreaseSize;
    private Button buttonFont;
    private TextView textViewTips;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_karangan);
        buttonIncreaseSize = findViewById(R.id.button_increase_size);
        buttonDecreaseSize = findViewById(R.id.button_decrease_size);
        buttonFont = findViewById(R.id.button_font);
        textViewTips = findViewById(R.id.text_view_tips);
        textViewTips.setText(Html.fromHtml(getString(R.string.tips_karangan_explain)));

        adsInterstitalAd();
        setButtonIncreaseSize();
        setButtonDecreaseSizeSize();
        setButtonFont();
    }

    private void adsInterstitalAd() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        //ca-app-pub-4598038295422798/4843892710
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.inter_ad_unit_id_tips));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd.isLoaded())
                    mInterstitialAd.show();
            }
        }, 3000);
    }

    //Method increase size text
    private void setButtonIncreaseSize() {
        buttonIncreaseSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewTips.setTextSize(0, textViewTips.getTextSize() + 2.0f);
            }
        });
    }

    //Method increase size text
    private void setButtonDecreaseSizeSize() {
        buttonDecreaseSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewTips.setTextSize(0, textViewTips.getTextSize() - 2.0f);
            }
        });
    }

    //Set button font
    private void setButtonFont() {
        buttonFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FontDialog fontDialog = new FontDialog(TipsKaranganActivity.this);
                fontDialog.setTextViewKarangan(textViewTips);
                fontDialog.show();
            }
        });
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
