package net.ticherhaz.karangancemerlangspm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HantarKaranganActivity extends AppCompatActivity {

    //Database
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    private Button buttonMuatNaik;

    private void listID() {
        buttonMuatNaik = findViewById(R.id.button_muat_naik);

        //Initialize
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hantar_karangan);
        listID();
        setButtonMuatNaik();

    }

    //Method button
    private void setButtonMuatNaik() {
        buttonMuatNaik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
