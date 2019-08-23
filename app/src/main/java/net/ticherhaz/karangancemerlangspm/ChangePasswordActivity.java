package net.ticherhaz.karangancemerlangspm;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.util.Others;

import java.util.Locale;

import static net.ticherhaz.karangancemerlangspm.util.ProgressDialogCustom.dismissProgressDialog;
import static net.ticherhaz.karangancemerlangspm.util.ProgressDialogCustom.showProgressDialog;

public class ChangePasswordActivity extends SkinActivity {

    private FirebaseAuth fAh;
    private FirebaseUser fUr;

    private TextInputEditText tietOldPsw, tietNewPsw, tietNewPswConfirm;
    private TextInputLayout tilOldPsw, tilNewPsw, tilNewPswConfirm;
    private Button btnUpdate;

    private void listID() {
        tietOldPsw = findViewById(R.id.tiet_old_psw);
        tietNewPsw = findViewById(R.id.tiet_new_psw);
        tietNewPswConfirm = findViewById(R.id.tiet_new_psw_confirm);
        btnUpdate = findViewById(R.id.btn_confirm);
        tilOldPsw = findViewById(R.id.til_old_psw);
        tilNewPsw = findViewById(R.id.til_new_psw);
        tilNewPswConfirm = findViewById(R.id.til_new_psw_confirm);

        fAh = FirebaseAuth.getInstance();
        fUr = fAh.getCurrentUser();
        if (fUr != null) {
            setBtnUpdate();
        }
    }

    private void setBtnUpdate() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tietOldPsw.getText() != null && tietNewPsw.getText() != null && tietNewPswConfirm.getText() != null)
                    if (!TextUtils.isEmpty(tietOldPsw.getText().toString()) && !TextUtils.isEmpty(tietNewPsw.getText().toString()) && !TextUtils.isEmpty(tietNewPswConfirm.getText().toString())) {
                        //Show progress dialog
                        showProgressDialog(ChangePasswordActivity.this);

                        if (fUr.getEmail() != null) {
                            //1st we check the old password is same or not
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(fUr.getEmail(), tietOldPsw.getText().toString());

                            fUr.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //If psw correct
                                                if (tietNewPsw.getText().toString().length() <= 8) {
                                                    dismissProgressDialog();
                                                    tilOldPsw.setError(null);
                                                    tilNewPsw.setError("Kata Laluan Hendaklah Melebihi 8 digit");
                                                    tietNewPsw.requestFocus();
                                                    tietNewPsw.getText().clear();
                                                } else {
                                                    //and then we check strength of the password
                                                    if (Others.PasswordStrength.calculateStrength(tietNewPsw.getText().toString()).getValue() < Others.PasswordStrength.STRONG.getValue()) {
                                                        dismissProgressDialog();
                                                        tilOldPsw.setError(null);
                                                        tilNewPsw.setError("Kata Laluan Perlu Ada Sekurangnya 1 Huruf Kecil, 1 Huruf Besar dan 1 Nombor Angka");
                                                        tietNewPsw.requestFocus();
                                                        tietNewPsw.getText().clear();
                                                    } else {
                                                        if (tietNewPsw.getText().toString().equals(tietNewPswConfirm.getText().toString())) {
                                                            //If match, we update
                                                            fUr.updatePassword(tietNewPswConfirm.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        dismissProgressDialog();
                                                                        //Success update password
                                                                        Toast.makeText(getApplicationContext(), "Berjaya Menukar Kata Laluan Baru!", Toast.LENGTH_LONG).show();
                                                                        tilNewPsw.setError(null);
                                                                        tilNewPswConfirm.setError(null);
                                                                        tietOldPsw.getText().clear();
                                                                        tietNewPsw.getText().clear();
                                                                        tietNewPswConfirm.getText().clear();
                                                                        tietOldPsw.requestFocus();
                                                                    } else {
                                                                        dismissProgressDialog();
                                                                        tilNewPsw.setError(null);
                                                                        tilNewPswConfirm.setError(null);
                                                                        if (task.getException() != null)
                                                                            Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(), "Error: %s", task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });

                                                        } else {
                                                            dismissProgressDialog();
                                                            tilNewPsw.setError(null);
                                                            tilNewPswConfirm.setError("Kata Laluan Tidak Sama");
                                                            tietNewPswConfirm.requestFocus();
                                                        }
                                                    }

                                                }

                                            } else {
                                                dismissProgressDialog();
                                                tilOldPsw.setError("Kata Laluan Tidak Sah");
                                                tietOldPsw.requestFocus();
                                                tietOldPsw.getText().clear();
                                                tietNewPsw.getText().clear();
                                                tietNewPswConfirm.getText().clear();
                                            }
                                        }
                                    });
                        }

                    }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        listID();
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
