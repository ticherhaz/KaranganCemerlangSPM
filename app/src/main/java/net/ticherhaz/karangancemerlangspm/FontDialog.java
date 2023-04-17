package net.ticherhaz.karangancemerlangspm;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

public class FontDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private Button buttonDefault;
    private Button buttonMerriweather;
    private Button buttonRoboto;
    private Button buttonTimesnewroman;
    private Button buttonArial;

    private TextView textViewKarangan;

    FontDialog(Context context) {
        super(context);
        this.context = context;
    }

    public TextView getTextViewKarangan() {
        return textViewKarangan;
    }

    void setTextViewKarangan(TextView textViewKarangan) {
        this.textViewKarangan = textViewKarangan;
    }

    private void listID() {
        buttonDefault = findViewById(R.id.button_default);
        buttonMerriweather = findViewById(R.id.button_merriweather);
        buttonRoboto = findViewById(R.id.button_roboto);
        buttonTimesnewroman = findViewById(R.id.button_timesnewroman);
        buttonArial = findViewById(R.id.button_arial);
        buttonDefault.setOnClickListener(this);
        buttonMerriweather.setOnClickListener(this);
        buttonRoboto.setOnClickListener(this);
        buttonTimesnewroman.setOnClickListener(this);
        buttonArial.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.font_dialog);
        listID();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_default) {
            setButtonDefault();
        } else if (v.getId() == R.id.button_merriweather) {
            setButtonMerriweather();
        } else if (v.getId() == R.id.button_roboto) {
            setButtonRoboto();
        } else if (v.getId() == R.id.button_timesnewroman) {
            setButtonTimesnewroman();
        } else if (v.getId() == R.id.button_arial) {
            setButtonArial();
        }
    }

    private void setButtonArial() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.arial);
        textViewKarangan.setTypeface(typeface);
        dismiss();
    }

    private void setButtonTimesnewroman() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.timesnewroman);
        textViewKarangan.setTypeface(typeface);
        dismiss();
    }

    private void setButtonDefault() {
        textViewKarangan.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        dismiss();
    }

    private void setButtonMerriweather() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.merriweather);
        textViewKarangan.setTypeface(typeface);
        dismiss();
    }

    private void setButtonRoboto() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto);
        textViewKarangan.setTypeface(typeface);
        dismiss();
    }
}
