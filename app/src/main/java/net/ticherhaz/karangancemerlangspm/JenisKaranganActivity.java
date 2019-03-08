package net.ticherhaz.karangancemerlangspm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.ticherhaz.karangancemerlangspm.Model.Jenis;
import net.ticherhaz.karangancemerlangspm.ViewHolder.JenisViewHolder;

public class JenisKaranganActivity extends AppCompatActivity {

    //RecyclerView
    private RecyclerView recyclerView;

    //FirebaseUI
    private FirebaseRecyclerOptions<Jenis> firebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<Jenis, JenisViewHolder> firebaseRecyclerAdapter;

    //Firebase Database
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ProgressBar progressBar;

    private String userUid;

    //List id
    private void listID() {
        recyclerView = findViewById(R.id.recycler_view_jenis);
        progressBar = findViewById(R.id.progressbar);

        //Firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("jenis").child("main");

        retrieveData();

        setFirebaseRecyclerAdapter();
    }

    //Method retrieve the data
    private void retrieveData() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            userUid = intent.getExtras().getString("userUid");
        }
    }

    //Set firebaseAdapter
    private void setFirebaseRecyclerAdapter() {
        progressBar.setVisibility(View.VISIBLE);
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Jenis>()
                .setQuery(databaseReference, Jenis.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Jenis, JenisViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull JenisViewHolder holder, int position, @NonNull final Jenis model) {
                holder.getTextViewTitle().setText(model.getTitle());
                holder.getTextViewDescription().setText(model.getDescription());

                //Onclick the view
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //This part we continue to the next activity
                        Intent intent = new Intent(JenisKaranganActivity.this, SenaraiKaranganActivity.class);
                        intent.putExtra("userUid", userUid);
                        intent.putExtra("karanganJenis", model.getJenisUid());
                        startActivities(new Intent[]{intent});
                    }
                });
            }

            @NonNull
            @Override
            public JenisViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.jenis_item, viewGroup, false);
                return new JenisViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
        //Display
        //1. Set the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        //2. FirebaseUI
        firebaseRecyclerAdapter.notifyDataSetChanged();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jenis_karangan);
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
