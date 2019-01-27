package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class KaranganDetailActivity extends AppCompatActivity {

    //We need firebase to check like or not and update if the user like
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //TextView
    private TextView textViewTajuk;
    private TextView textViewKarangan;
    private TextView textViewTarikh;
    private TextView textViewVote;
    private TextView textViewLike;

    private String userUid;
    private String uidKarangan;
    private String tajuk;
    private String karangan;
    private String tarikh;
    private String vote;
    private int like;
    private int voteAtKarangan = 0;

    //Method listID
    private void listID() {
        textViewTajuk = findViewById(R.id.text_view_tajuk);
        textViewKarangan = findViewById(R.id.text_view_karangan);
        textViewTarikh = findViewById(R.id.text_view_tarikh);
        textViewVote = findViewById(R.id.text_view_vote);
        textViewLike = findViewById(R.id.text_view_like);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("user");

        retrieveData();
        displayData();
        checkLike();
    }

    //Method retrieve the data
    private void retrieveData() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            uidKarangan = intent.getExtras().getString("uidKarangan");
            userUid = intent.getExtras().getString("userUid");
            tajuk = intent.getExtras().getString("tajukPenuh");
            karangan = intent.getExtras().getString("karangan");
            tarikh = intent.getExtras().getString("tarikh");
            vote = String.valueOf(intent.getExtras().getInt("vote"));
        }
    }

    //Method display the data
    private void displayData() {
        textViewTajuk.append(tajuk);
        textViewKarangan.append(karangan);
        textViewTarikh.append(tarikh);
        textViewVote.append("Vote: " + vote);
    }

    //Method check the user if user already like or not
    private void checkLike() {
        //We are using the value event listener instead single value because we want to database always online
        databaseReference.child(userUid).child("karangan").child(tajuk).child("like").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    like = dataSnapshot.getValue(Integer.class);
                }

                //Then update the view at the like

                //0 == not like
                //1 == like

                if (like == 1) {
                    textViewLike.setText("LIKED");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Method click the like text view
    private void setTextViewLike() {
        textViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the text already become liked
                if (textViewLike.getText().toString().equals("LIKED")) {
                    Toast.makeText(getApplicationContext(), "You already liked this karangan", Toast.LENGTH_SHORT).show();
                } else {
                    //1st. we read and update at karangan
                    final DatabaseReference databaseReferenceKarangan = firebaseDatabase.getReference();
                    databaseReferenceKarangan.child("karangan").child("main").child(uidKarangan).child("vote").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                voteAtKarangan = dataSnapshot.getValue(Integer.class);
                            }
                            //Then we update at karangan
                            databaseReferenceKarangan.child("karangan").child("main").child(uidKarangan).child("vote").setValue(voteAtKarangan + 1);
                            databaseReference.child(userUid).child("karangan").child(tajuk).child("like").setValue(1);
                            Toast.makeText(getApplicationContext(), "You LIKE this Karangan", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
