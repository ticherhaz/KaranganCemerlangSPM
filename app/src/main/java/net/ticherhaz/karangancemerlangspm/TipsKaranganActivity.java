package net.ticherhaz.karangancemerlangspm;

import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TipsKaranganActivity extends AppCompatActivity {

    private Button buttonIncreaseSize;
    private Button buttonDecreaseSize;
    private Button buttonFont;
    private TextView textViewTips;

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