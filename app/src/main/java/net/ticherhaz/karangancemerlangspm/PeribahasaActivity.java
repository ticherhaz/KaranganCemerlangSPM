package net.ticherhaz.karangancemerlangspm;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
            protected void onBindViewHolder(@NonNull final PeribahasaViewHolder holder, int position, @NonNull final Peribahasa model) {
                holder.getTextViewTitle().setText(model.getTitle());
                holder.getTextViewDescription().setText(model.getDescription());

                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //When user press it, it will save to clipboard
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("title", model.getTitle());
                        if (clipboard != null)
                            clipboard.setPrimaryClip(clip);

                        Toast.makeText(getApplicationContext(), "Disalin ke Papan Klip", Toast.LENGTH_SHORT).show();

                        holder.getView().setEnabled(false);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                holder.getView().setEnabled(true);
                            }
                        }, 2300);
                    }
                });


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


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    messageInternetMessage(PeribahasaActivity.this);
                }

            }
        }, 5000);
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
