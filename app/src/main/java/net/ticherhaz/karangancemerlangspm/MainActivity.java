package net.ticherhaz.karangancemerlangspm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
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

    private DatabaseReference databaseReference;

    //Variable
    private String userUid;
    private String phoneModel;

    //Edit Text
    private EditText editTextSearch;

    //Progressbar
    private ProgressBar progressBar;

    //RecyclerView
    private RecyclerView recyclerViewTajuk1;
    private RecyclerView recyclerViewTajuk2;
    private RecyclerView recyclerViewTajuk7;
    private RecyclerView recyclerViewTag;
    private LinearLayout linearLayout;
    private LinearLayout linearLayoutHow;

    //Method listID
    private void listID() {
        //Toolbar
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        editTextSearch = findViewById(R.id.edit_text_search);
        TextView textViewHow = findViewById(R.id.text_view_how);
        progressBar = findViewById(R.id.progressbar);
        recyclerViewTajuk1 = findViewById(R.id.recycler_view_tajuk1);
        recyclerViewTajuk2 = findViewById(R.id.recycler_view_tajuk2);
        recyclerViewTajuk7 = findViewById(R.id.recycler_view_tajuk7);
        recyclerViewTag = findViewById(R.id.recycler_view_tag);
        linearLayout = findViewById(R.id.linear_layout);
        linearLayoutHow = findViewById(R.id.linear_layout_how);

        textViewHow.setText(Html.fromHtml(getString(R.string.how)));

        //Get the value of the userUid
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            userUid = intent.getExtras().getString("userUid");
            phoneModel = intent.getExtras().getString("phoneModel");
        }


        //Database
        //Firebase Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
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
                linearLayoutHow.setVisibility(View.GONE);
                setFirebaseRecyclerAdapter(editTextSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(editTextSearch.getText().toString())) {
                    progressBar.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.INVISIBLE);
                    linearLayoutHow.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //Set the firebaseUI
    private void setFirebaseRecyclerAdapter(String search) {
        //  setRecyclerView(search, "tajuk1UpperCase", recyclerViewTajuk1);
        // setRecyclerView(search, "tajuk2UpperCase", recyclerViewTajuk2);
        // setRecyclerView(search, "tajuk7UpperCase", recyclerViewTajuk7);
        setRecyclerViewTag(search, recyclerViewTag);
    }

    //Method tajuk
    private void setRecyclerViewTag(String search, final RecyclerView recyclerViewNumber) {
        //Making query
        Query query = databaseReference.child("karangan").child("main").orderByChild("karanganTag").startAt(search.toUpperCase()).endAt(search.toUpperCase() + "\uf8ff");
        //FirebaseUI
        FirebaseRecyclerOptions<Karangan> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Karangan>()
                .setQuery(query, Karangan.class)
                .build();
        FirebaseRecyclerAdapter<Karangan, KaranganViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Karangan, KaranganViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final KaranganViewHolder holder, int position, @NonNull final Karangan model) {
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


    //Method tajuk
    private void setRecyclerView(String search, String tajukNumberUpperCase, final RecyclerView recyclerViewNumber) {
        //Making query
        Query query = databaseReference.child("karangan").child("main").orderByChild(tajukNumberUpperCase).startAt(search.toUpperCase()).endAt(search.toUpperCase() + "\uf8ff");
        //FirebaseUI
        FirebaseRecyclerOptions<Karangan> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Karangan>()
                .setQuery(query, Karangan.class)
                .build();
        FirebaseRecyclerAdapter<Karangan, KaranganViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Karangan, KaranganViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final KaranganViewHolder holder, int position, @NonNull final Karangan model) {
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
            //TODO: Update the version at About
            //builder.setMessage("Karangan Cemerlang SPM\nversion 1.14\n\n\nDon't forget to share with your friends :)\n\n--Donate--\nHAZMAN BADRUNSHAM\n7614543761\nCIMB BANK\n\n\n\nhazman45.blogspot.com\nTicherhaz©2019");
            builder.setMessage("Karangan Cemerlang SPM\nversion 1.14\n\n\nDon't forget to share with your friends :)\n\n\n\nCredited to:\nCikgu Mariani\nCikgu Hamidah\nCikgu Rohani\nCikgu Harum Awang\nCikgu Samat\nCikgu Che Noranuwi\nNabil Fikri\n\nhazman45.blogspot.com\nTicherhaz©2019");

            builder.setCancelable(true);
            builder.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.show();
            return true;
        }
        if (id == R.id.action_tips_karangan) {
            startActivity(new Intent(MainActivity.this, TipsKaranganActivity.class));
            return true;
        }
        if (id == R.id.action_hantar_karangan) {
            Intent intent = new Intent(MainActivity.this, HantarKaranganActivity.class);
            intent.putExtra("userUid", userUid);
            startActivities(new Intent[]{intent});
            return true;
        }
        if (id == R.id.action_feedback) {
            Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
            intent.putExtra("userUid", userUid);
            intent.putExtra("phoneModel", phoneModel);
            startActivities(new Intent[]{intent});
            return true;
        }
        if (id == R.id.action_forum) {
            Intent intent = new Intent(MainActivity.this, ForumSplashActivity.class);
            intent.putExtra("userUid", userUid);
            intent.putExtra("phoneModel", phoneModel);
            startActivities(new Intent[]{intent});
            return true;
        }
        if (id == R.id.action_senarai_karangan) {
//            Intent intent = new Intent(MainActivity.this, SenaraiKaranganActivity.class);
//            intent.putExtra("userUid", userUid);
//            startActivities(new Intent[]{intent});

            Intent intent = new Intent(MainActivity.this, JenisKaranganActivity.class);
            intent.putExtra("userUid", userUid);
            startActivities(new Intent[]{intent});
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
