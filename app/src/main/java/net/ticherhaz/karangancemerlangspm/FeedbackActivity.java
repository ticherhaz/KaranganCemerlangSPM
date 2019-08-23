package net.ticherhaz.karangancemerlangspm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.model.Feedback;

import static net.ticherhaz.karangancemerlangspm.util.Others.isNetworkAvailable;
import static net.ticherhaz.karangancemerlangspm.util.Others.messageInternetMessage;
import static net.ticherhaz.karangancemerlangspm.util.ProgressDialogCustom.dismissProgressDialog;
import static net.ticherhaz.karangancemerlangspm.util.ProgressDialogCustom.showProgressDialog;
import static net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasa;

public class FeedbackActivity extends SkinActivity {

    private DatabaseReference databaseReference;

    private EditText editTextEmail;
    private EditText editTextDescription;

    private Button buttonSubmit;

    private Spinner spinner;

    private String userUid;
    private String phoneModel;

    //Method list ID
    private void listID() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("feedback");

        editTextEmail = findViewById(R.id.edit_text_email);
        editTextDescription = findViewById(R.id.text_view_description);

        spinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.feedback_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        buttonSubmit = findViewById(R.id.button_submit);

        retrieveIntent();
    }

    //Retrieve value
    private void retrieveIntent() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            userUid = intent.getExtras().getString("userUid");
            phoneModel = intent.getExtras().getString("phoneModel");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        listID();
        setButtonSubmit();
    }

    //Method Button Submit
    private void setButtonSubmit() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editTextEmail.getText().toString()) && !TextUtils.isEmpty(editTextDescription.getText().toString()) && isEmailValid(editTextEmail.getText().toString())) {
                    showProgressDialog(FeedbackActivity.this);
                    String typeProblem = spinner.getSelectedItem().toString();
                    String email = editTextEmail.getText().toString();
                    String description = editTextDescription.getText().toString();
                    String date = GetTarikhMasa();
                    String uid = databaseReference.push().getKey();

                    Feedback feedback = new Feedback(uid, userUid, phoneModel, typeProblem, email, description, date);

                    if (isNetworkAvailable(FeedbackActivity.this)) {
                        //if yes, then store the data into firebase
                        if (uid != null)
                            databaseReference.child(uid).setValue(feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        editTextEmail.setText("");
                                        editTextDescription.setText("");
                                        Toast.makeText(getApplicationContext(), "Terima kasih atas maklum balas anda\nKami akan hubungi anda dalam masa terdekat", Toast.LENGTH_SHORT).show();
                                        editTextEmail.requestFocus();
                                    }
                                }
                            });
                    } else {
                        //No connection
                        messageInternetMessage(FeedbackActivity.this);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Sila isi email dan deskripsi anda", Toast.LENGTH_SHORT).show();
                }
                dismissProgressDialog();
            }
        });
    }

    //Method check email type
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
