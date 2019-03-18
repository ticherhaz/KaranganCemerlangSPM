package net.ticherhaz.karangancemerlangspm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.ticherhaz.karangancemerlangspm.Model.Umum;
import net.ticherhaz.karangancemerlangspm.Util.InternetCheck;

import java.util.Date;

public class TopikBaruActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private EditText editTextTajuk;
    private EditText editTextDeskripsi;
    private Button buttonHantar;

    private String userName;


    private void listID() {
        editTextTajuk = findViewById(R.id.edit_text_tajuk);
        editTextDeskripsi = findViewById(R.id.edit_text_deskripsi);
        buttonHantar = findViewById(R.id.button_hantar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            userName = firebaseUser.getDisplayName();
        }
    }

    private void retrieveUser() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topik_baru);
        listID();
        setButtonHantar();
    }

    private void setButtonHantar() {
        buttonHantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternet();
            }
        });
    }

    private void checkInternet() {
        //I think it is better we check the edittext is write down or not then we check the internet connection.
        if (!TextUtils.isEmpty(editTextTajuk.getText().toString()) && !TextUtils.isEmpty(editTextDeskripsi.getText().toString())) {
            new InternetCheck(new InternetCheck.Consumer() {
                @Override
                public void accept(Boolean internet) {
                    //If internet is available
                    //    if (internet) {
                    storeDatabase();
                    //   } else {
                    //       Toast.makeText(getApplicationContext(), new InternetMessage().getMessage(), Toast.LENGTH_SHORT).show();
                    //  }


                }
            });
        }
    }

    private void storeDatabase() {

        //Make the umumUid
        String umumUid = databaseReference.push().getKey();
        final String tajuk = editTextTajuk.getText().toString();
        String deskripsi = editTextDeskripsi.getText().toString();
        long viewed = 0;
        long jumlahBalas = 0;
        long kedudukan = 1;
        String dimulaiOleh = userName;
        long masaDimulaiOleh = System.currentTimeMillis();
        String dibalasOleh = "";
        long masaDibalasOleh = 0;

        String onCreatedDate = String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd'T'HH:mm:ss", new Date()));

        String activityUmumLogUid = databaseReference.push().getKey();
        String activityKududukanLogUid = databaseReference.push().getKey();
        String type = "Umum";
        String lastVisitedUser = userName;


        //Call class to store the value
        Umum umum = new Umum(umumUid, tajuk, deskripsi, viewed, jumlahBalas, kedudukan, dimulaiOleh, masaDimulaiOleh, dibalasOleh,
                masaDibalasOleh, onCreatedDate, activityUmumLogUid, activityKududukanLogUid, type, lastVisitedUser);


        if (umumUid != null) {
            databaseReference.child("umum").child("main").child(umumUid).setValue(umum);
            databaseReference.child("umum").child("detail").child(umumUid).child(umumUid).setValue(umum);
        }
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
