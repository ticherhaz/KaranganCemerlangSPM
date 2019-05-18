package net.ticherhaz.karangancemerlangspm;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.google.firebase.database.Query;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.Model.Peribahasa;
import net.ticherhaz.karangancemerlangspm.ViewHolder.PeribahasaViewHolder;

import static net.ticherhaz.karangancemerlangspm.Util.Others.isNetworkAvailable;
import static net.ticherhaz.karangancemerlangspm.Util.Others.messageInternetMessage;

public class PeribahasaActivity extends SkinActivity {

    //Declare variables
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseRecyclerOptions<Peribahasa> firebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<Peribahasa, PeribahasaViewHolder> firebaseRecyclerAdapter;

    private void setFirebaseRecyclerAdapter() {
        Query query = databaseReference.orderByChild("title");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Peribahasa>()
                .setQuery(query, Peribahasa.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Peribahasa, PeribahasaViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull PeribahasaViewHolder holder, int position, @NonNull Peribahasa model) {
                holder.getTextViewTitle().setText(model.getTitle());
                holder.getTextViewDescription().setText(model.getDescription());
            }

            @NonNull
            @Override
            public PeribahasaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.peribahasa_item, viewGroup, false);
                return new PeribahasaViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        };

        //Display
        firebaseRecyclerAdapter.startListening();
        recyclerView.setLayoutManager(new LinearLayoutManager(PeribahasaActivity.this));
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void listID() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        setSwipeRefreshLayout();
        progressBar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.recycler_view);

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("peribahasa");

        setFirebaseRecyclerAdapter();

    }

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //If there is connection
                        if (isNetworkAvailable(PeribahasaActivity.this)) {
                            setFirebaseRecyclerAdapter();
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            messageInternetMessage(PeribahasaActivity.this);
                        }
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peribahasa);
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
