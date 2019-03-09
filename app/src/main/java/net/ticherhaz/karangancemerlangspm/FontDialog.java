package net.ticherhaz.karangancemerlangspm;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FontDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private Button buttonDefault;
    private Button buttonMerriweather;
    private Button buttonRoboto;
    private Button buttonTimesnewroman;
    private Button buttonArial;

    private TextView textViewKarangan;

    public FontDialog(Context context) {
        super(context);
        this.context = context;
    }

    public TextView getTextViewKarangan() {
        return textViewKarangan;
    }

    public void setTextViewKarangan(TextView textViewKarangan) {
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
        switch (v.getId()) {
            case R.id.button_default:
                setButtonDefault();
                break;
            case R.id.button_merriweather:
                setButtonMerriweather();
                break;
            case R.id.button_roboto:
                setButtonRoboto();
                break;
            case R.id.button_timesnewroman:
                setButtonTimesnewroman();
                break;
            case R.id.button_arial:
                setButtonArial();
                break;
            default:
                break;
        }
    }

    private void setButtonArial() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.arial);
        textViewKarangan.setTypeface(typeface);
        Toast.makeText(context, "Success changed font to Arial", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void setButtonTimesnewroman() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.timesnewroman);
        textViewKarangan.setTypeface(typeface);
        Toast.makeText(context, "Success changed font to Times New Roman", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void setButtonDefault() {
        textViewKarangan.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        Toast.makeText(context, "Success changed font to Default", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void setButtonMerriweather() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.merriweather);
        textViewKarangan.setTypeface(typeface);
        Toast.makeText(context, "Success changed font to Merriweather", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void setButtonRoboto() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto);
        textViewKarangan.setTypeface(typeface);
        Toast.makeText(context, "Success changed font to Roboto", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
