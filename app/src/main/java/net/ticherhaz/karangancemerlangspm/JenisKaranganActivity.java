package net.ticherhaz.karangancemerlangspm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.Model.Jenis;
import net.ticherhaz.karangancemerlangspm.ViewHolder.JenisViewHolder;

import static net.ticherhaz.karangancemerlangspm.Util.Others.isNetworkAvailable;
import static net.ticherhaz.karangancemerlangspm.Util.Others.messageInternetMessage;

public class JenisKaranganActivity extends SkinActivity {

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
    private String karanganJenis;

    private SwipeRefreshLayout swipeRefreshLayout;

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isNetworkAvailable(JenisKaranganActivity.this)) {
                            //if has internet connection
                            setFirebaseRecyclerAdapter();
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            messageInternetMessage(JenisKaranganActivity.this);
                        }

                    }
                }, 500);

            }
        });
    }

    //List id
    private void listID() {
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressbar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        setSwipeRefreshLayout();
        //Firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("jenis");

        retrieveData();
        setFirebaseRecyclerAdapter();
    }

    //Method retrieve the data
    private void retrieveData() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            userUid = intent.getExtras().getString("userUid");
            karanganJenis = intent.getExtras().getString("karanganJenis");
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
            protected void onBindViewHolder(@NonNull final JenisViewHolder holder, int position, @NonNull final Jenis model) {
                holder.getTextViewTitle().setText(model.getTitle());
                holder.getTextViewDescription().setText(model.getDescription());


                //This part is to retrieve the total viewer for specific jenis karangan.
                FirebaseDatabase.getInstance().getReference().child("karangan").child(model.getJenisUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            long sum = 0;
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.exists()) {
                                    long size = dataSnapshot1.child("mostVisited").getValue(Long.class);
                                    sum += size;
                                    holder.getTextViewViewer().setText(String.valueOf(sum));
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.jenis_item, viewGroup, false);
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
        // firebaseRecyclerAdapter.notifyDataSetChanged();
        firebaseRecyclerAdapter.startListening();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    messageInternetMessage(JenisKaranganActivity.this);
                }

            }
        }, 5000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toolbar toolbar = findViewById(R.id.app_bar);
//        setSupportActionBar(toolbar);
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
