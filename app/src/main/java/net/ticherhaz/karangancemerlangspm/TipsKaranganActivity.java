package net.ticherhaz.karangancemerlangspm;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.zxy.skin.sdk.SkinActivity;

public class TipsKaranganActivity extends SkinActivity {

    //---INTERSTITIAL END ----
    private InterstitialAd interstitialAd;

    private Button buttonIncreaseSize;
    private Button buttonDecreaseSize;
    private Button buttonFont;
    private TextView textViewTips;
    //private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_karangan);
        buttonIncreaseSize = findViewById(R.id.button_increase_size);
        buttonDecreaseSize = findViewById(R.id.button_decrease_size);
        buttonFont = findViewById(R.id.button_font);
        textViewTips = findViewById(R.id.text_view_tips);
        textViewTips.setText(Html.fromHtml(getString(R.string.tips_karangan_explain)));

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        interstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        interstitialAd.setAdUnitId(getString(R.string.interstitialTipsUid));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {

            }
        });
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        }

        setButtonIncreaseSize();
        setButtonDecreaseSizeSize();
        setButtonFont();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (interstitialAd != null && interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        }, 500);
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
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
