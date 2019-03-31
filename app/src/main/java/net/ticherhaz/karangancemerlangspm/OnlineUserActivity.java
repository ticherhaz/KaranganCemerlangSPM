package net.ticherhaz.karangancemerlangspm;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.ticherhaz.karangancemerlangspm.Model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.Util.Others;
import net.ticherhaz.karangancemerlangspm.ViewHolder.OnlineStatusViewHolder;

public class OnlineUserActivity extends AppCompatActivity {

    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //Firebase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //Firebase Recycler Adapter
    private FirebaseRecyclerOptions<RegisteredUser> firebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<RegisteredUser, OnlineStatusViewHolder> firebaseRecyclerAdapter;

    private RecyclerView recyclerView;

    private String registeredUid;

    private ProgressBar progressBar;

    //Method list id
    private void listID() {
        recyclerView = findViewById(R.id.recycler_view_online_user);
        progressBar = findViewById(R.id.progressbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        if (firebaseUser != null) {
            registeredUid = firebaseUser.getUid();
        }
        setFirebaseRecyclerAdapter();
    }

    //Method set firebase recycler adapter
    private void setFirebaseRecyclerAdapter() {
        progressBar.setVisibility(View.VISIBLE);
        Query query = databaseReference.child("registeredUser").orderByChild("onlineStatus").equalTo("Online");

        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<RegisteredUser>()
                .setQuery(query, RegisteredUser.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RegisteredUser, OnlineStatusViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull OnlineStatusViewHolder holder, int position, @NonNull RegisteredUser model) {
                holder.getTextViewUsername().setText(model.getUsername());
                holder.getTextViewSekolah().setText(model.getSekolah());
                holder.getTextViewReputation().setText(String.valueOf(model.getReputation()));
                new Others().setStatus(model.getMode(), holder.getTextViewStatus());
            }

            @NonNull
            @Override
            public OnlineStatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.online_user_item, viewGroup, false);
                return new OnlineStatusViewHolder(view);
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
        setContentView(R.layout.activity_online_user);
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
