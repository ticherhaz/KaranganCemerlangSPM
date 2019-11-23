package net.ticherhaz.karangancemerlangspm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.util.Others;
import net.ticherhaz.karangancemerlangspm.viewHolder.OnlineStatusViewHolder;

import static net.ticherhaz.karangancemerlangspm.util.UserTypeColor.setTextColorUserOnlineUserActivity;

public class OnlineUserActivity extends SkinActivity {

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
            protected void onBindViewHolder(@NonNull OnlineStatusViewHolder holder, int position, @NonNull final RegisteredUser model) {
                holder.getTextViewUsername().setText(model.getUsername());
                holder.getTextViewUsername().setSelected(true);
                holder.getTextViewSekolah().setText(model.getSekolah());
                holder.getTextViewSekolah().setSelected(true);
                holder.getTextViewReputation().setText(String.valueOf(model.getReputation()));
                new Others().setStatus(model.getMode(), holder.getTextViewStatus());

                //change the color of the user
                if (model.getTitleType() != null) {
                    setTextColorUserOnlineUserActivity(model, holder, OnlineUserActivity.this);
                }

                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(OnlineUserActivity.this, ProfileActivity.class);
                        intent.putExtra("registeredUid", model.getRegisteredUserUid());
                        startActivities(new Intent[]{intent});
                    }
                });
            }

            @NonNull
            @Override
            public OnlineStatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.online_user_item, viewGroup, false);
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
