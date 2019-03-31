package net.ticherhaz.karangancemerlangspm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.firebase.database.FirebaseDatabase;

public class StatusDialog extends Dialog {

    Context context;
    private String registeredUid;
    private CheckBox checkBoxGembira;
    private CheckBox checkBoxBiasa;
    private CheckBox checkBoxSedih;

    StatusDialog(Context context) {
        super(context);
        this.context = context;
    }

    public String getRegisteredUid() {
        return registeredUid;
    }

    public void setRegisteredUid(String registeredUid) {
        this.registeredUid = registeredUid;
    }

    private void listID() {
        checkBoxGembira = findViewById(R.id.checkbox_gembira);
        checkBoxBiasa = findViewById(R.id.checkbox_biasa);
        checkBoxSedih = findViewById(R.id.checkbox_sedih);

        setCheckBox();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.status_dialog);
        listID();
    }

    private void setCheckBox() {
        checkBoxGembira.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //then we store the data in the database
                    FirebaseDatabase.getInstance().getReference().child("registeredUser").child(registeredUid).child("mode").setValue("Gembira");
                    dismiss();
                }
            }
        });
        checkBoxBiasa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //then we store the data in the database
                    FirebaseDatabase.getInstance().getReference().child("registeredUser").child(registeredUid).child("mode").setValue("Biasa");
                    dismiss();
                }
            }
        });
        checkBoxSedih.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //then we store the data in the database
                    FirebaseDatabase.getInstance().getReference().child("registeredUser").child(registeredUid).child("mode").setValue("Sedih");
                    dismiss();
                }
            }
        });
    }
}
