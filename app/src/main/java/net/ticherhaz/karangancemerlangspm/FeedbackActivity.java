package net.ticherhaz.karangancemerlangspm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.Model.Feedback;
import net.ticherhaz.karangancemerlangspm.Util.InternetCheck;
import net.ticherhaz.karangancemerlangspm.Util.InternetMessage;

import java.util.Calendar;

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
                R.array.feedback_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                //Check the internet connection
                new InternetCheck(new InternetCheck.Consumer() {
                    @Override
                    public void accept(Boolean internet) {
                        if (internet) {
                            if (!TextUtils.isEmpty(editTextEmail.getText().toString()) && !TextUtils.isEmpty(editTextDescription.getText().toString()) && isEmailValid(editTextEmail.getText().toString())) {
                                String typeProblem = spinner.getSelectedItem().toString();
                                String email = editTextEmail.getText().toString();
                                String description = editTextDescription.getText().toString();
                                String date = Calendar.getInstance().getTime().toString();

                                String uid = databaseReference.push().getKey();

                                Feedback feedback = new Feedback(uid, userUid, phoneModel, typeProblem, email, description, date);
                                //Then store the data into firebase
                                if (uid != null)
                                    databaseReference.child(uid).setValue(feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                editTextEmail.setText("");
                                                editTextDescription.setText("");
                                                Toast.makeText(getApplicationContext(), "Terima kasih atas maklum balas anda\nKami akan hubungi anda dalam masa terdekat", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            } else {
                                Toast.makeText(getApplicationContext(), "Sila isi email dan deskripsi anda", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), new InternetMessage().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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
