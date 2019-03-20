package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.ticherhaz.karangancemerlangspm.Model.Umum;
import net.ticherhaz.karangancemerlangspm.ViewHolder.UmumHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UmumActivity extends AppCompatActivity {

    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //FirebaseAuth
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //FirebaseUi
    private FirebaseRecyclerOptions<Umum> firebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<Umum, UmumHolder> firebaseRecyclerAdapter;

    //ProgressBar
    private ProgressBar progressBar;

    //RecyclerView
    private RecyclerView recyclerView;

    //Button
    private Button buttonTopikBaru;

    private void setFirebaseRecyclerAdapter() {
        progressBar.setVisibility(View.VISIBLE);
        Query query = databaseReference.child("umum").child("main");

        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Umum>()
                .setQuery(query, Umum.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Umum, UmumHolder>(firebaseRecyclerOptions) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull UmumHolder holder, int position, @NonNull final Umum model) {
                @SuppressLint("SimpleDateFormat") final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                //Change the date to ago
                try {
                    Date date = inputFormat.parse(model.getOnCreatedDate());
                    @SuppressLint({"NewApi", "LocalSuppress"}) String niceDateStr = String.valueOf(DateUtils.getRelativeTimeSpanString(date.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS));

                    holder.getTextViewUmumTitle().setText(model.getTajuk());
                    holder.getTextViewKedudukan().setText(String.valueOf(model.getKedudukan()));
                    holder.getTextViewUmumViews().setText(String.valueOf(model.getViewed()));
                    holder.getTextViewJumlahBalas().setText(String.valueOf(model.getJumlahBalas()));
                    holder.getTextViewDimulaiOleh().setText("Dimulai Oleh " + model.getDimulaiOleh() + ", " + niceDateStr);
                    holder.getTextViewDibalasOleh().setText("Dibalas Oleh " + model.getDibalasOleh());
                    holder.getTextViewMasaDibalasOleh().setText(String.valueOf(String.valueOf(model.getMasaDibalasOleh())));


                    holder.getView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(UmumActivity.this, UmumDetailActivity.class);
                            intent.putExtra("umumUid", model.getUmumUid());
                            startActivities(new Intent[]{intent});
                        }
                    });


                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }

            @NonNull
            @Override
            public UmumHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.umum_item, viewGroup, false);
                return new UmumHolder(view);
            }

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.INVISIBLE);

            }
        };

        //Display
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }


    private void listID() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressBar = findViewById(R.id.progressbar);
        buttonTopikBaru = findViewById(R.id.button_topik);
        recyclerView = findViewById(R.id.recycler_view_umum);

        setFirebaseRecyclerAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umum);
        listID();
        setButtonTopikBaru();
    }

    //Button topik
    private void setButtonTopikBaru() {
        buttonTopikBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser != null) {
                    startActivity(new Intent(UmumActivity.this, TopikBaruActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Sila daftar/Log Masuk Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //OnBackPressed
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
