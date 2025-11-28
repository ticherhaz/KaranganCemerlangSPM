package net.ticherhaz.karangancemerlangspm;

import android.os.Bundle;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class TipsKaranganActivity extends AppCompatActivity {

    private Button buttonIncreaseSize;
    private Button buttonDecreaseSize;
    private Button buttonFont;
    private TextView textViewTips;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_karangan);
        buttonIncreaseSize = findViewById(R.id.button_increase_size);
        buttonDecreaseSize = findViewById(R.id.button_decrease_size);
        buttonFont = findViewById(R.id.button_font);
        textViewTips = findViewById(R.id.text_view_tips);
        textViewTips.setText(Html.fromHtml(getString(R.string.tips_karangan_explain)));

        setButtonIncreaseSize();
        setButtonDecreaseSizeSize();
        setButtonFont();

        initAdView();
    }

    private void setButtonIncreaseSize() {
        buttonIncreaseSize.setOnClickListener(v -> textViewTips.setTextSize(0, textViewTips.getTextSize() + 2.0f));
    }

    private void setButtonDecreaseSizeSize() {
        buttonDecreaseSize.setOnClickListener(v -> textViewTips.setTextSize(0, textViewTips.getTextSize() - 2.0f));
    }

    private void setButtonFont() {
        buttonFont.setOnClickListener(v -> {
            FontDialog fontDialog = new FontDialog(TipsKaranganActivity.this);
            fontDialog.setTextViewKarangan(textViewTips);
            fontDialog.show();
        });
    }

    private void initAdView() {
        // Create a new ad view.
        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-1320314109772118/5458213955");

        // Request an anchored adaptive banner with a width of 360.
        AdSize adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, 360);
        adView.setAdSize(adSize);

        FrameLayout adViewContainer = findViewById(R.id.ad_view_container);
        adViewContainer.removeAllViews();
        adViewContainer.addView(adView);


        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void destroyBanner() {
        // Remove banner from view hierarchy.
        if (adView != null) {
            ViewGroup parentView = (ViewGroup) adView.getParent();
            if (parentView != null) {
                parentView.removeView(adView);
            }
            // Destroy the banner ad resources.
            adView.destroy();
            // Drop reference to the banner ad.
            adView = null;
        }
    }

    @Override
    protected void onDestroy() {
        destroyBanner();
        super.onDestroy();
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