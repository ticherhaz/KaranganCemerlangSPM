package net.ticherhaz.karangancemerlangspm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.ticherhaz.karangancemerlangspm.Model.Karangan;
import net.ticherhaz.karangancemerlangspm.ViewHolder.KaranganViewHolder;

import java.util.Calendar;

public class SenaraiKaranganActivity extends AppCompatActivity {


    //Database
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //FirebaseUi
    //Array: Model
    private FirebaseRecyclerOptions<Karangan> firebaseRecyclerOptions;
    //Array: Model and ViewHolder
    private FirebaseRecyclerAdapter<Karangan, KaranganViewHolder> firebaseRecyclerAdapter;

    //RecyclerView
    private RecyclerView recyclerView;

    //ProgressBar
    private ProgressBar progressBar;

    //Swipe
    private SwipeRefreshLayout swipeRefreshLayout;

    //Variable
    private String userUid;

    //Method listID
    private void listID() {
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressbar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        setSwipeRefreshLayout();
        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

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

    //Method firebaseUI
    private void setFirebaseRecyclerAdapter() {
        //FirebaseUI
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Karangan>()
                .setQuery(databaseReference.child("karangan").child("main"), Karangan.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Karangan, KaranganViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull KaranganViewHolder holder, int position, @NonNull final Karangan model) {
                //Display the data
                holder.getTextViewTajuk().setText(model.getTajukPenuh());
                holder.getTextViewDeskripsi().setText(model.getDeskripsiPenuh());
                holder.getTextViewViewer().setText(String.valueOf(model.getMostVisited()));
                holder.getTextViewFav().setText(String.valueOf(model.getVote()));

                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Display the progress bar1
                        progressBar.setVisibility(View.VISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        //This part we will update the database when user click the specific karangan
                        //1. We need to update the last visited karangan
                        databaseReference.child("user").child(userUid).child("lastVisitedKarangan").setValue(model.getTajukPenuh());
                        //2. So about the mostvisited karangan.
                        //So we read back the previous data
                        databaseReference.child("user").child(userUid).child("karangan").child(model.getTajukPenuh()).child("click").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //We declare the click = 0 because we don't know if the click is available or not
                                int click = 0;
                                if (dataSnapshot.exists()) {
                                    click = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                                }
                                //Then we set the new data of the user
                                databaseReference.child("user").child(userUid).child("karangan").child(model.getTajukPenuh()).child("click").setValue(click + 1);

                                //This part for the karangan, we will do the same thing as the user
                                databaseReference.child("karangan").child("main").child(model.getUid()).child("mostVisited").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        //Hide the progressbar
                                        progressBar.setVisibility(View.INVISIBLE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                        int clickKarangan = 0;
                                        if (dataSnapshot.exists()) {
                                            clickKarangan = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                                        }
                                        //Then we set the data for the karangan
                                        databaseReference.child("karangan").child("main").child(model.getUid()).child("mostVisited").setValue(clickKarangan + 1);

                                        //After that we need to update this karangan about the lastuservisited
                                        String tarikh = Calendar.getInstance().getTime().toString();
                                        databaseReference.child("karangan").child("main").child(model.getUid()).child("userLastVisitedDate").setValue(tarikh);

                                        //This part we continue to the next activity
                                        Intent intent = new Intent(SenaraiKaranganActivity.this, KaranganDetailActivity.class);
                                        intent.putExtra("userUid", userUid);
                                        intent.putExtra("uidKarangan", model.getUid());
                                        intent.putExtra("tajukPenuh", model.getTajukPenuh());
                                        intent.putExtra("deskripsiPenuh", model.getDeskripsiPenuh());
                                        intent.putExtra("tarikh", model.getTarikh());
                                        intent.putExtra("karangan", model.getKarangan());
                                        intent.putExtra("vote", model.getVote());
                                        intent.putExtra("mostVisited", model.getMostVisited());
                                        intent.putExtra("userLastVisitedDate", model.getUserLastVisitedDate());
                                        startActivities(new Intent[]{intent});
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public KaranganViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                //At this part, we are creating the view,
                //I mean we are referring to what layout.
                //So, we are making a new view and assign to the karangan_item
                //Then we return the view for the firebase recycler adapter
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.karangan_item, viewGroup, false);
                return new KaranganViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                //When dataChanged mean, after finished load the data

                //Hide the progressbar
                progressBar.setVisibility(View.GONE);
            }
        };
        //Display
        //1. Set the recycler view
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        //2. FirebaseUI
        firebaseRecyclerAdapter.notifyDataSetChanged();
        firebaseRecyclerAdapter.startListening();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senarai_karangan);
        listID();
    }

    //Method swipe
    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firebaseRecyclerAdapter.notifyDataSetChanged();
                firebaseRecyclerAdapter.startListening();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
