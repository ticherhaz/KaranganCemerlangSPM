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
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.ticherhaz.karangancemerlangspm.Model.UmumDetail;
import net.ticherhaz.karangancemerlangspm.ViewHolder.UmumDetailHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UmumDetailActivity extends AppCompatActivity {


    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //FirebaseAuth
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //FirebaseUi
    private FirebaseRecyclerOptions<UmumDetail> firebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<UmumDetail, UmumDetailHolder> firebaseRecyclerAdapter;

    //ProgressBar
    private ProgressBar progressBar;

    //RecyclerView
    private RecyclerView recyclerView;

    private String umumUid;

    private void setFirebaseRecyclerAdapter() {
        progressBar.setVisibility(View.VISIBLE);
        Query query = databaseReference.child("umum").child("detail").child(umumUid);
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<UmumDetail>()
                .setQuery(query, UmumDetail.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UmumDetail, UmumDetailHolder>(firebaseRecyclerOptions) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull UmumDetailHolder holder, int position, @NonNull UmumDetail model) {
                @SuppressLint("SimpleDateFormat") final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                //Change the date to ago
                try {
                    Date date = inputFormat.parse(model.getPostCreatedDate());
                    @SuppressLint({"NewApi", "LocalSuppress"}) String niceDateStr = String.valueOf(DateUtils.getRelativeTimeSpanString(date.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS));

                    holder.getTextViewUsername().setText(model.getUsername());
                    holder.getTextViewUserTitle().setText(model.getUserTitle());
                    holder.getTextViewSekolah().setText(model.getSekolah());
                    holder.getTextViewUserJoinDate().setText("Masa Menyertai: " + model.getOnAccountCreatedDate());
                    holder.getTextViewGender().setText("Jantina: " + model.getGender());
                    holder.getTextViewPos().setText("Pos: " + String.valueOf(model.getPos()));
                    holder.getTextViewReputation().setText(String.valueOf(model.getReputation()));
                    holder.getTextViewDeskripsi().setText(model.getDeskripsi());

//                    holder.getTextViewUmumTitle().setText(model.getTajuk());
//                    holder.getTextViewKedudukan().setText(String.valueOf(model.getKedudukan()));
//                    holder.getTextViewUmumViews().setText(String.valueOf(model.getViewed()));
//                    holder.getTextViewJumlahBalas().setText(String.valueOf(model.getJumlahBalas()));
//                    holder.getTextViewDimulaiOleh().setText("Dimulai Oleh " + model.getDimulaiOleh() + ", " + niceDateStr);
//                    holder.getTextViewDibalasOleh().setText("Dibalas Oleh " + model.getDibalasOleh());
                    holder.getTextViewMasaDibalasOleh().setText(String.valueOf(niceDateStr));


                    holder.getView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });


                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }

            @NonNull
            @Override
            public UmumDetailHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.umum_detail_item, viewGroup, false);
                return new UmumDetailHolder(view);
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
        recyclerView = findViewById(R.id.recycler_view_umum_detail);

        Intent intent = getIntent();
        if (intent != null) {
            umumUid = intent.getExtras().getString("umumUid");
        }

        setFirebaseRecyclerAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umum_detail);

        listID();

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
