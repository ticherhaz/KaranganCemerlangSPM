package net.ticherhaz.karangancemerlangspm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.ticherhaz.karangancemerlangspm.Model.Karangan;
import net.ticherhaz.karangancemerlangspm.ViewHolder.KaranganViewHolder;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //Firebase Database
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //FirebaseUI
    private FirebaseRecyclerOptions<Karangan> firebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<Karangan, KaranganViewHolder> firebaseRecyclerAdapter;

    //Variable
    private String userUid;

    //Edit Text
    private EditText editTextSearch;

    //TextView
    private TextView textViewData;

    //Progressbar
    private ProgressBar progressBar;

    //Swipe
    private SwipeRefreshLayout swipeRefreshLayout;

    //RecyclerView
    private RecyclerView recyclerViewTajuk1;
    private RecyclerView recyclerViewTajuk2;
    // private RecyclerView recyclerViewTajuk3;
//    private RecyclerView recyclerViewTajuk4;
//    private RecyclerView recyclerViewTajuk5;
//    private RecyclerView recyclerViewTajuk6;
//    private RecyclerView recyclerViewTajuk7;
//    private RecyclerView recyclerViewTajuk8;
//    private RecyclerView recyclerViewTajuk9;
//
//    private RecyclerView recyclerViewDeskripsi1;
//    private RecyclerView recyclerViewDeskripsi2;
//    private RecyclerView recyclerViewDeskripsi3;
//    private RecyclerView recyclerViewDeskripsi4;
//    private RecyclerView recyclerViewDeskripsi5;
//    private RecyclerView recyclerViewDeskripsi6;
//    private RecyclerView recyclerViewDeskripsi7;
//    private RecyclerView recyclerViewDeskripsi8;
//    private RecyclerView recyclerViewDeskripsi9;

    private LinearLayout linearLayout;
    private Toolbar toolbar;

    //Method listID
    private void listID() {
        //Toolbar
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        editTextSearch = findViewById(R.id.edit_text_search);
        textViewData = findViewById(R.id.text_view_data_is_not_found);
        progressBar = findViewById(R.id.progressbar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        setSwipeRefreshLayout();
        recyclerViewTajuk1 = findViewById(R.id.recycler_view_tajuk1);
        recyclerViewTajuk2 = findViewById(R.id.recycler_view_tajuk2);
        // recyclerViewTajuk3 = findViewById(R.id.recycler_view_tajuk3);
//        recyclerViewTajuk4 = findViewById(R.id.recycler_view_tajuk4);
//        recyclerViewTajuk5 = findViewById(R.id.recycler_view_tajuk5);
//        recyclerViewTajuk6 = findViewById(R.id.recycler_view_tajuk6);
//        recyclerViewTajuk7 = findViewById(R.id.recycler_view_tajuk7);
//        recyclerViewTajuk8 = findViewById(R.id.recycler_view_tajuk8);
//        recyclerViewTajuk9 = findViewById(R.id.recycler_view_tajuk9);
//
//        recyclerViewDeskripsi1 = findViewById(R.id.recycler_view_deskripsi1);
//        recyclerViewDeskripsi2 = findViewById(R.id.recycler_view_deskripsi2);
//        recyclerViewDeskripsi3 = findViewById(R.id.recycler_view_deskripsi3);
//        recyclerViewDeskripsi4 = findViewById(R.id.recycler_view_deskripsi4);
//        recyclerViewDeskripsi5 = findViewById(R.id.recycler_view_deskripsi5);
//        recyclerViewDeskripsi6 = findViewById(R.id.recycler_view_deskripsi6);
//        recyclerViewDeskripsi7 = findViewById(R.id.recycler_view_deskripsi7);
//        recyclerViewDeskripsi8 = findViewById(R.id.recycler_view_deskripsi8);
//        recyclerViewDeskripsi9 = findViewById(R.id.recycler_view_deskripsi9);

        linearLayout = findViewById(R.id.linear_layout);

        //Get the value of the userUid
        Intent intent = getIntent();
        if (intent.getExtras() != null)
            userUid = intent.getExtras().getString("userUid");

        //Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        setEditTextSearch();
    }

    //Method Edit Text Changed
    private void setEditTextSearch() {

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                linearLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                setFirebaseRecyclerAdapter(editTextSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(editTextSearch.getText().toString())) {
                    progressBar.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    //Set the firebaseUI
    private void setFirebaseRecyclerAdapter(String search) {
        //This part for the tajukUpperCase
//        if (setRecyclerView(search, "tajuk1UpperCase", recyclerViewTajuk1) || setRecyclerView(search, "tajuk2UpperCase", recyclerViewTajuk2) || setRecyclerView(search, "tajuk3UpperCase", recyclerViewTajuk3) ||
//                setRecyclerView(search, "tajuk4UpperCase", recyclerViewTajuk4) || setRecyclerView(search, "tajuk5UpperCase", recyclerViewTajuk5) || setRecyclerView(search, "tajuk6UpperCase", recyclerViewTajuk6) ||
//                setRecyclerView(search, "tajuk7UpperCase", recyclerViewTajuk7) || setRecyclerView(search, "tajuk8UpperCase", recyclerViewTajuk8) || setRecyclerView(search, "tajuk9UpperCase", recyclerViewTajuk9)) {
        setRecyclerView(search, "tajuk1UpperCase", recyclerViewTajuk1);
        setRecyclerView(search, "tajuk2UpperCase", recyclerViewTajuk2);
        //   setRecyclerView(search, "tajuk3UpperCase", recyclerViewTajuk3);
//            setRecyclerView(search, "tajuk4UpperCase", recyclerViewTajuk4);
//            setRecyclerView(search, "tajuk5UpperCase", recyclerViewTajuk5);
//            setRecyclerView(search, "tajuk6UpperCase", recyclerViewTajuk6);
//            setRecyclerView(search, "tajuk7UpperCase", recyclerViewTajuk7);
//            setRecyclerView(search, "tajuk8UpperCase", recyclerViewTajuk8);
//            setRecyclerView(search, "tajuk9UpperCase", recyclerViewTajuk9);
        //   } else {
        //This part for the deskripsiUpperCase
//            setRecyclerView(search, "deskripsi1UpperCase", recyclerViewDeskripsi1);
//            setRecyclerView(search, "deskripsi2UpperCase", recyclerViewDeskripsi2);
//            setRecyclerView(search, "deskripsi3UpperCase", recyclerViewDeskripsi3);
//            setRecyclerView(search, "deskripsi4UpperCase", recyclerViewDeskripsi4);
//            setRecyclerView(search, "deskripsi5UpperCase", recyclerViewDeskripsi5);
//            setRecyclerView(search, "deskripsi6UpperCase", recyclerViewDeskripsi6);
//            setRecyclerView(search, "deskripsi7UpperCase", recyclerViewDeskripsi7);
//            setRecyclerView(search, "deskripsi8UpperCase", recyclerViewDeskripsi8);
//            setRecyclerView(search, "deskripsi9UpperCase", recyclerViewDeskripsi9);
        // }
    }

    //Method tajuk
    private void setRecyclerView(String search, String tajukNumberUpperCase, final RecyclerView recyclerViewNumber) {

        //Making query
        Query query = databaseReference.child("karangan").child("main").orderByChild(tajukNumberUpperCase).startAt(search.toUpperCase()).endAt(search.toUpperCase() + "\uf8ff");

        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Karangan>()
                .setQuery(query, Karangan.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Karangan, KaranganViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final KaranganViewHolder holder, int position, @NonNull final Karangan model) {
                //Display the data
                holder.getTextViewTajuk().setText(model.getTajukPenuh());
                holder.getTextViewDeskripsi().setText(model.getDeskripsiPenuh());

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
                                    click = dataSnapshot.getValue(Integer.class);
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
                                            clickKarangan = dataSnapshot.getValue(Integer.class);
                                        }
                                        //Then we set the data for the karangan
                                        databaseReference.child("karangan").child("main").child(model.getUid()).child("mostVisited").setValue(clickKarangan + 1);

                                        //After that we need to update this karangan about the lastuservisited
                                        String tarikh = Calendar.getInstance().getTime().toString();
                                        databaseReference.child("karangan").child("main").child(model.getUid()).child("userLastVisitedDate").setValue(tarikh);

                                        //This part we continue to the next activity
                                        Intent intent = new Intent(getApplicationContext(), KaranganDetailActivity.class);
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
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.karangan_item, viewGroup, false);
                return new KaranganViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.INVISIBLE);
//                if (firebaseRecyclerAdapter.getItemCount() < 0) {
//                    textViewData.setVisibility(View.VISIBLE);
//                } else{
//                    textViewData.setVisibility(View.GONE);
//                }

                // If there are no chat messages, show a view that invites the user to add a message.
                //  textViewData.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
        //Display
        //1. Set the recycler view
        recyclerViewNumber.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewNumber.setAdapter(firebaseRecyclerAdapter);
        //2. FirebaseUI
        firebaseRecyclerAdapter.notifyDataSetChanged();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listID();
    }

    //This is for toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            builder.setTitle("About");
            builder.setMessage("Karangan Cemerlang SPM\nversion 1.0\n\ncreated by Ticherhaz\nhazman45.blogspot.com\n\n ©2019");
            builder.setCancelable(true);
            builder.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Method swipe
    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firebaseRecyclerAdapter.startListening();
                firebaseRecyclerAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
