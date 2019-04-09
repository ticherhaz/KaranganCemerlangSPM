package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.Util.DoubleClickListener;
import net.ticherhaz.karangancemerlangspm.Util.RunTransaction;

public class KaranganDetailActivity extends SkinActivity {

    //We need firebase to check like or not and update if the user like
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //TextView
    private TextView textViewTajuk;
    private TextView textViewKarangan;
    private TextView textViewTarikh;
    private TextView textViewFav;
    private TextView textViewViewer;

    private Button buttonIncreaseSize;
    private Button buttonDecreaseSize;
    private Button buttonFont;
    private Button buttonBlack;

    private String karanganJenis;
    private String userUid;
    private String uidKarangan;
    private String tajuk;
    private String karangan;
    private String tarikh;
    private int vote;
    private int mostVisited;
    private boolean colorChange = true;

    //Method listID
    private void listID() {
        textViewTajuk = findViewById(R.id.text_view_tajuk);
        textViewKarangan = findViewById(R.id.text_view_karangan);
        textViewTarikh = findViewById(R.id.text_view_tarikh);
        textViewFav = findViewById(R.id.text_view_fav);
        textViewViewer = findViewById(R.id.text_view_viewer);

        buttonIncreaseSize = findViewById(R.id.button_increase_size);
        buttonDecreaseSize = findViewById(R.id.button_decrease_size);
        buttonFont = findViewById(R.id.button_font);
        buttonBlack = findViewById(R.id.button_black);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        retrieveData();
        displayData();
        checkLike();


        setButtonIncreaseSize();
        setButtonDecreaseSizeSize();
        setButtonFont();
        setButtonBlack();
    }

    //Method set black
    private void setButtonBlack() {
        buttonBlack.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override

            public void onClick(View v) {
                if (colorChange) {
                    colorChange = false;
                    buttonBlack.setText("Mod Putih");
                    textViewKarangan.setTextColor(Color.WHITE);
                    textViewKarangan.setBackgroundColor(Color.DKGRAY);
                } else {
                    colorChange = true;
                    textViewKarangan.setTextColor(Color.DKGRAY);
                    buttonBlack.setText("Mod Hitam");
                    textViewKarangan.setBackgroundColor(Color.WHITE);
                }

            }
        });
    }

    //Method increase size text
    private void setButtonIncreaseSize() {
        buttonIncreaseSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewKarangan.setTextSize(0, textViewKarangan.getTextSize() + 2.0f);
            }
        });
    }

    //Method increase size text
    private void setButtonDecreaseSizeSize() {
        buttonDecreaseSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewKarangan.setTextSize(0, textViewKarangan.getTextSize() - 2.0f);
            }
        });
    }

    //Set button font
    private void setButtonFont() {
        buttonFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FontDialog fontDialog = new FontDialog(KaranganDetailActivity.this);
                fontDialog.setTextViewKarangan(textViewKarangan);
                fontDialog.show();
            }
        });
    }


    //Method retrieve the data
    private void retrieveData() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            uidKarangan = intent.getExtras().getString("uidKarangan");
            karanganJenis = intent.getExtras().getString("karanganJenis");
            userUid = intent.getExtras().getString("userUid");
            tajuk = intent.getExtras().getString("tajukPenuh");
            karangan = intent.getExtras().getString("karangan");
            tarikh = intent.getExtras().getString("tarikh");
            vote = intent.getExtras().getInt("vote");
            mostVisited = intent.getExtras().getInt("mostVisited");
        }
    }

    //Method display the data
    private void displayData() {
        textViewTajuk.setText(tajuk);
        textViewKarangan.setText(karangan);
        textViewTarikh.setText(tarikh);
        textViewFav.setText(String.valueOf(vote));
        textViewViewer.setText(String.valueOf(mostVisited));
    }

    //Method check the user if user already like or not
    private void checkLike() {
        //We are using the value event listener instead single value because we want to database always online
        databaseReference.child("userAlphaKaranganClick").child(userUid).child(tajuk).child("like").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    vote = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                    //So kalau dia dah like tukar ini kepada warna merah
                    textViewFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favourite_red, 0, 0, 0);
                    textViewFav.setCompoundDrawablePadding(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Method click the like text view
    private void setTextViewLike() {
        textViewFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the text already become liked
                if (textViewFav.getCompoundDrawablePadding() == 1) {
                    Toast.makeText(getApplicationContext(), "Anda Sudah Suka Karangan Ini", Toast.LENGTH_SHORT).show();
                } else {
                    //1st. we read and update at karangan
                    new RunTransaction().runTransactionUserVoteKarangan(databaseReference, karanganJenis, uidKarangan);
                    databaseReference.child("userAlphaKaranganClick").child(userUid).child(tajuk).child("like").setValue(1);
                    textViewFav.setText(String.valueOf(vote + 1));
                    Toast.makeText(getApplicationContext(), "Anda Suka Karangan Ini", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setTextViewKarangan() {
        textViewKarangan.setOnClickListener(new DoubleClickListener() {

            //So we use the double click from the custom at the util.
            @Override
            public void onSingleClick(View v) {
            }

            @Override
            public void onDoubleClick(View v) {
                //If the text already become liked
                if (textViewFav.getCompoundDrawablePadding() == 1) {
                    Toast.makeText(getApplicationContext(), "Anda Sudah Suka Karangan Ini", Toast.LENGTH_SHORT).show();
                } else {
                    //1st. we read and update at karangan
                    new RunTransaction().runTransactionUserVoteKarangan(databaseReference, karanganJenis, uidKarangan);
                    databaseReference.child("userAlphaKaranganClick").child(userUid).child(tajuk).child("like").setValue(1);
                    textViewFav.setText(String.valueOf(vote + 1));
                    Toast.makeText(getApplicationContext(), "Anda Suka Karangan Ini", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karangan_detail);
        listID();
        setTextViewLike();
        setTextViewKarangan();
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
