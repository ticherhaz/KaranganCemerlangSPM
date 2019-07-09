package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zxy.skin.sdk.SkinActivity;
import com.zxy.skin.sdk.SkinEngine;

import net.ticherhaz.karangancemerlangspm.Model.AboutClicked;
import net.ticherhaz.karangancemerlangspm.Model.Karangan;
import net.ticherhaz.karangancemerlangspm.Util.Others;
import net.ticherhaz.karangancemerlangspm.Util.RunTransaction;
import net.ticherhaz.karangancemerlangspm.ViewHolder.KaranganViewHolder;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasa;

public class MainActivity extends SkinActivity {

    private static final String SHARED_PREFERENCES_MOD = "myPreferenceMod";
    private static final String SAVED_MOD = "mySavedMod";
    private static final String SHARED_PREFERENCES = "myPreference";
    boolean isDisplaying = false;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<Karangan> firebaseRecyclerOptions;
    private FirebaseRecyclerOptions<Karangan> firebaseRecyclerOptions2;
    private FirebaseRecyclerOptions<Karangan> firebaseRecyclerOptions3;
    private FirebaseRecyclerOptions<Karangan> firebaseRecyclerOptions4;
    private FirebaseRecyclerOptions<Karangan> firebaseRecyclerOptions5;
    private FirebaseRecyclerOptions<Karangan> firebaseRecyclerOptions6;
    private FirebaseRecyclerAdapter<Karangan, KaranganViewHolder> firebaseRecyclerAdapter;
    private FirebaseRecyclerAdapter<Karangan, KaranganViewHolder> firebaseRecyclerAdapter2;
    private FirebaseRecyclerAdapter<Karangan, KaranganViewHolder> firebaseRecyclerAdapter3;
    private FirebaseRecyclerAdapter<Karangan, KaranganViewHolder> firebaseRecyclerAdapter4;
    private FirebaseRecyclerAdapter<Karangan, KaranganViewHolder> firebaseRecyclerAdapter5;
    private FirebaseRecyclerAdapter<Karangan, KaranganViewHolder> firebaseRecyclerAdapter6;
    //Variable
    private String userUid;
    private String phoneModel;
    //Edit Text
    private EditText editTextSearch;
    //Progressbar
    private ProgressBar progressBar;
    //RecyclerView
    private RecyclerView recyclerViewTag;
    private RecyclerView recyclerViewTag2;
    private RecyclerView recyclerViewTag3;
    private RecyclerView recyclerViewTag4;
    private RecyclerView recyclerViewTag5;
    private RecyclerView recyclerViewTag6;
    private LinearLayout linearLayout;
    private LinearLayout linearLayoutHow;
    private LinearLayout scrollViewUtama;
    //Button
    private Button buttonSenaraiKarangan;
    private Button buttonTipsKarangan;
    private Button buttonHantarKarangan;
    private Button buttonForum;
    private Button buttonPeribahasa;
    private TextView textViewAnnouncement;
    private TextView textViewCountdownSPM;
    private SharedPreferences sharedPreferences;
    private String mod;
    // private BillingClient billingClient;

    //Method listID
    private void listID() {
        //Toolbar
//        Toolbar toolbar = findViewById(R.id.app_bar);
//        toolbar.setTitleTextColor(Color.WHITE);
//
//        setSupportActionBar(toolbar);

        //8.4.2019: we dont use the toolbar because it is duplicated and not supported for the night mode

        editTextSearch = findViewById(R.id.edit_text_search);
        TextView textViewHow = findViewById(R.id.text_view_how);
        textViewAnnouncement = findViewById(R.id.text_view_announcement);
        textViewCountdownSPM = findViewById(R.id.text_view_countdown_spm);
        progressBar = findViewById(R.id.progressbar);

        recyclerViewTag = findViewById(R.id.recycler_view_tag);
        recyclerViewTag2 = findViewById(R.id.recycler_view_tag_2);
        recyclerViewTag3 = findViewById(R.id.recycler_view_tag_3);
        recyclerViewTag4 = findViewById(R.id.recycler_view_tag_4);
        recyclerViewTag5 = findViewById(R.id.recycler_view_tag_5);
        recyclerViewTag6 = findViewById(R.id.recycler_view_tag_6);

        linearLayout = findViewById(R.id.linear_layout);
        linearLayoutHow = findViewById(R.id.linear_layout_how);
        scrollViewUtama = findViewById(R.id.linear_layout_utama);

        //Button
        buttonSenaraiKarangan = findViewById(R.id.button_senarai_karangan);
        buttonTipsKarangan = findViewById(R.id.button_tips_karangan);
        buttonHantarKarangan = findViewById(R.id.button_hantar_karangan);
        buttonForum = findViewById(R.id.button_forum);
        buttonPeribahasa = findViewById(R.id.button_peribahasa);

        textViewHow.setText(Html.fromHtml(getString(R.string.how)));

        //Set billing
        //setBillingClient();

        //Get the value of the userUid
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            userUid = intent.getExtras().getString("userUid");
            phoneModel = intent.getExtras().getString("phoneModel");
            mod = intent.getExtras().getString("mod");
        }


        //Firebase Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        setEditTextSearch();
        setTextViewAnnouncement();

        setButtonPeribahasa();
    }

    private void setButtonPeribahasa() {
        buttonPeribahasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PeribahasaActivity.class));
            }
        });
    }

    private void setTextViewAnnouncement() {
        databaseReference.child("announcement").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String textAnnouncement = dataSnapshot.getValue(String.class);
                    textViewAnnouncement.setText(textAnnouncement);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Method Edit Text Changed
    private void setEditTextSearch() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextSearch.getText().toString().equals("")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    scrollViewUtama.setVisibility(View.VISIBLE);
                    //  linearLayoutUtama.setVisibility(View.VISIBLE);
                    linearLayoutHow.setVisibility(View.GONE);
                    if (firebaseRecyclerAdapter != null && firebaseRecyclerAdapter2 != null && firebaseRecyclerAdapter3 != null && firebaseRecyclerAdapter4 != null && firebaseRecyclerAdapter5 != null && firebaseRecyclerAdapter6 != null) {
                        firebaseRecyclerAdapter.stopListening();
                        firebaseRecyclerAdapter2.stopListening();
                        firebaseRecyclerAdapter3.stopListening();
                        firebaseRecyclerAdapter4.stopListening();
                        firebaseRecyclerAdapter5.stopListening();
                        firebaseRecyclerAdapter6.stopListening();
                    }

                } else {
                    setFirebaseRecyclerAdapter(editTextSearch.getText().toString());
                }
            }
        });
    }

    //Set the firebaseUI
    private void setFirebaseRecyclerAdapter(String search) {
        setRecyclerView(search, recyclerViewTag); //usaha
        setRecyclerView2(search, recyclerViewTag2); //punca
        setRecyclerView3(search, recyclerViewTag3); //peranan
        setRecyclerView4(search, recyclerViewTag4); //kesan
        setRecyclerView5(search, recyclerViewTag5); //faktor
        setRecyclerView6(search, recyclerViewTag6); //cabaran
    }

    //Method tag6
    private void setRecyclerView6(final String search, final RecyclerView recyclerViewNumber) {
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        linearLayoutHow.setVisibility(View.GONE);
        scrollViewUtama.setVisibility(View.GONE);

        //Making query
        Query query = databaseReference.child("karangan").child("CABARAN").orderByChild("karanganTag").startAt(search.toUpperCase()).endAt(search.toUpperCase() + "\uf8ff");
        //FirebaseUI
        firebaseRecyclerOptions6 = new FirebaseRecyclerOptions.Builder<Karangan>()
                .setQuery(query, Karangan.class)
                .build();
        firebaseRecyclerAdapter6 = new FirebaseRecyclerAdapter<Karangan, KaranganViewHolder>(firebaseRecyclerOptions6) {
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
                        //The window cannot editable, takleh nak pergi mana2
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        //This part we will update the database when user click the specific karangan
                        //1. We need to update the last visited karangan
                        new Others().lastVisitedKarangan(databaseReference, userUid, model);
                        //2. So about the mostvisited karangan.
                        //So we read back the previous data

                        //26.3.2019 : I'm making the new class because the main activity need to use to, so the share the method
                        new RunTransaction().runTransactionUserClick(databaseReference, userUid, model.getUid());
                        new RunTransaction().runTransactionKaranganMostVisited(progressBar, databaseReference, MainActivity.this, userUid, model.getUid(), model.getTajukPenuh(), model.getDeskripsiPenuh(), model.getTarikh(), model.getKarangan(), model.getVote(), model.getMostVisited(), model.getUserLastVisitedDate(), "PUNCA");

                    }
                });
            }


            @NonNull
            @Override
            public KaranganViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.karangan_item, viewGroup, false);
                return new KaranganViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.INVISIBLE);
                // if there are no chat messages, show a view that invites the user to add a message
                linearLayoutHow.setVisibility(firebaseRecyclerAdapter.getItemCount() == 0 && firebaseRecyclerAdapter2.getItemCount() == 0 && firebaseRecyclerAdapter3.getItemCount() == 0
                        && firebaseRecyclerAdapter4.getItemCount() == 0 && firebaseRecyclerAdapter5.getItemCount() == 0 && firebaseRecyclerAdapter6.getItemCount() == 0 ? View.VISIBLE : View.GONE);

            }
        };
        //Display
        //1. Set the recycler view
        recyclerViewNumber.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewNumber.setAdapter(firebaseRecyclerAdapter5);
        //2. FirebaseUI
        firebaseRecyclerAdapter6.notifyDataSetChanged();
        firebaseRecyclerAdapter6.startListening();
    }

    //Method tag5
    private void setRecyclerView5(final String search, final RecyclerView recyclerViewNumber) {
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        linearLayoutHow.setVisibility(View.GONE);
        scrollViewUtama.setVisibility(View.GONE);

        //Making query
        Query query = databaseReference.child("karangan").child("FAKTOR").orderByChild("karanganTag").startAt(search.toUpperCase()).endAt(search.toUpperCase() + "\uf8ff");
        //FirebaseUI
        firebaseRecyclerOptions5 = new FirebaseRecyclerOptions.Builder<Karangan>()
                .setQuery(query, Karangan.class)
                .build();
        firebaseRecyclerAdapter5 = new FirebaseRecyclerAdapter<Karangan, KaranganViewHolder>(firebaseRecyclerOptions5) {
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
                        //The window cannot editable, takleh nak pergi mana2
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        //This part we will update the database when user click the specific karangan
                        //1. We need to update the last visited karangan
                        new Others().lastVisitedKarangan(databaseReference, userUid, model);
                        //2. So about the mostvisited karangan.
                        //So we read back the previous data

                        //26.3.2019 : I'm making the new class because the main activity need to use to, so the share the method
                        new RunTransaction().runTransactionUserClick(databaseReference, userUid, model.getUid());
                        new RunTransaction().runTransactionKaranganMostVisited(progressBar, databaseReference, MainActivity.this, userUid, model.getUid(), model.getTajukPenuh(), model.getDeskripsiPenuh(), model.getTarikh(), model.getKarangan(), model.getVote(), model.getMostVisited(), model.getUserLastVisitedDate(), "PUNCA");

                    }
                });
            }


            @NonNull
            @Override
            public KaranganViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.karangan_item, viewGroup, false);
                return new KaranganViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.INVISIBLE);
                // if there are no chat messages, show a view that invites the user to add a message
                linearLayoutHow.setVisibility(firebaseRecyclerAdapter.getItemCount() == 0 && firebaseRecyclerAdapter2.getItemCount() == 0 && firebaseRecyclerAdapter3.getItemCount() == 0
                        && firebaseRecyclerAdapter4.getItemCount() == 0 && firebaseRecyclerAdapter5.getItemCount() == 0 && firebaseRecyclerAdapter6.getItemCount() == 0 ? View.VISIBLE : View.GONE);

            }
        };
        //Display
        //1. Set the recycler view
        recyclerViewNumber.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewNumber.setAdapter(firebaseRecyclerAdapter5);
        //2. FirebaseUI
        //firebaseRecyclerAdapter5.notifyDataSetChanged();
        firebaseRecyclerAdapter5.startListening();
    }

    //Method tag4
    private void setRecyclerView4(final String search, final RecyclerView recyclerViewNumber) {
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        linearLayoutHow.setVisibility(View.GONE);
        scrollViewUtama.setVisibility(View.GONE);

        //Making query
        Query query = databaseReference.child("karangan").child("KESAN").orderByChild("karanganTag").startAt(search.toUpperCase()).endAt(search.toUpperCase() + "\uf8ff");
        //FirebaseUI
        firebaseRecyclerOptions4 = new FirebaseRecyclerOptions.Builder<Karangan>()
                .setQuery(query, Karangan.class)
                .build();
        firebaseRecyclerAdapter4 = new FirebaseRecyclerAdapter<Karangan, KaranganViewHolder>(firebaseRecyclerOptions4) {
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
                        //The window cannot editable, takleh nak pergi mana2
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        //This part we will update the database when user click the specific karangan
                        //1. We need to update the last visited karangan
                        new Others().lastVisitedKarangan(databaseReference, userUid, model);
                        //2. So about the mostvisited karangan.
                        //So we read back the previous data

                        //26.3.2019 : I'm making the new class because the main activity need to use to, so the share the method
                        new RunTransaction().runTransactionUserClick(databaseReference, userUid, model.getUid());
                        new RunTransaction().runTransactionKaranganMostVisited(progressBar, databaseReference, MainActivity.this, userUid, model.getUid(), model.getTajukPenuh(), model.getDeskripsiPenuh(), model.getTarikh(), model.getKarangan(), model.getVote(), model.getMostVisited(), model.getUserLastVisitedDate(), "PUNCA");

                    }
                });
            }


            @NonNull
            @Override
            public KaranganViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.karangan_item, viewGroup, false);
                return new KaranganViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.INVISIBLE);
                // if there are no chat messages, show a view that invites the user to add a message
                linearLayoutHow.setVisibility(firebaseRecyclerAdapter.getItemCount() == 0 && firebaseRecyclerAdapter2.getItemCount() == 0 && firebaseRecyclerAdapter3.getItemCount() == 0
                        && firebaseRecyclerAdapter4.getItemCount() == 0 && firebaseRecyclerAdapter5.getItemCount() == 0 && firebaseRecyclerAdapter6.getItemCount() == 0 ? View.VISIBLE : View.GONE);

            }
        };
        //Display
        //1. Set the recycler view
        recyclerViewNumber.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewNumber.setAdapter(firebaseRecyclerAdapter4);
        //2. FirebaseUI
        // firebaseRecyclerAdapter4.notifyDataSetChanged();
        firebaseRecyclerAdapter4.startListening();
    }

    //Method tag3
    private void setRecyclerView3(final String search, final RecyclerView recyclerViewNumber) {
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        linearLayoutHow.setVisibility(View.GONE);
        scrollViewUtama.setVisibility(View.GONE);

        //Making query
        Query query = databaseReference.child("karangan").child("PERANAN").orderByChild("karanganTag").startAt(search.toUpperCase()).endAt(search.toUpperCase() + "\uf8ff");
        //FirebaseUI
        firebaseRecyclerOptions3 = new FirebaseRecyclerOptions.Builder<Karangan>()
                .setQuery(query, Karangan.class)
                .build();
        firebaseRecyclerAdapter3 = new FirebaseRecyclerAdapter<Karangan, KaranganViewHolder>(firebaseRecyclerOptions3) {
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
                        //The window cannot editable, takleh nak pergi mana2
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        //This part we will update the database when user click the specific karangan
                        //1. We need to update the last visited karangan
                        new Others().lastVisitedKarangan(databaseReference, userUid, model);
                        //2. So about the mostvisited karangan.
                        //So we read back the previous data

                        //26.3.2019 : I'm making the new class because the main activity need to use to, so the share the method
                        new RunTransaction().runTransactionUserClick(databaseReference, userUid, model.getUid());
                        new RunTransaction().runTransactionKaranganMostVisited(progressBar, databaseReference, MainActivity.this, userUid, model.getUid(), model.getTajukPenuh(), model.getDeskripsiPenuh(), model.getTarikh(), model.getKarangan(), model.getVote(), model.getMostVisited(), model.getUserLastVisitedDate(), "PUNCA");

                    }
                });
            }


            @NonNull
            @Override
            public KaranganViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.karangan_item, viewGroup, false);
                return new KaranganViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.INVISIBLE);
                // if there are no chat messages, show a view that invites the user to add a message
                linearLayoutHow.setVisibility(firebaseRecyclerAdapter.getItemCount() == 0 && firebaseRecyclerAdapter2.getItemCount() == 0 && firebaseRecyclerAdapter3.getItemCount() == 0
                        && firebaseRecyclerAdapter4.getItemCount() == 0 && firebaseRecyclerAdapter5.getItemCount() == 0 && firebaseRecyclerAdapter6.getItemCount() == 0 ? View.VISIBLE : View.GONE);

            }
        };
        //Display
        //1. Set the recycler view
        recyclerViewNumber.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewNumber.setAdapter(firebaseRecyclerAdapter3);
        //2. FirebaseUI
        // firebaseRecyclerAdapter3.notifyDataSetChanged();
        firebaseRecyclerAdapter3.startListening();
    }

    //Method tag2
    private void setRecyclerView2(final String search, final RecyclerView recyclerViewNumber) {
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        linearLayoutHow.setVisibility(View.GONE);
        scrollViewUtama.setVisibility(View.GONE);

        //Making query
        Query query = databaseReference.child("karangan").child("PUNCA").orderByChild("karanganTag").startAt(search.toUpperCase()).endAt(search.toUpperCase() + "\uf8ff");
        //FirebaseUI
        firebaseRecyclerOptions2 = new FirebaseRecyclerOptions.Builder<Karangan>()
                .setQuery(query, Karangan.class)
                .build();
        firebaseRecyclerAdapter2 = new FirebaseRecyclerAdapter<Karangan, KaranganViewHolder>(firebaseRecyclerOptions2) {
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
                        //The window cannot editable, takleh nak pergi mana2
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        //This part we will update the database when user click the specific karangan
                        //1. We need to update the last visited karangan
                        new Others().lastVisitedKarangan(databaseReference, userUid, model);
                        //2. So about the mostvisited karangan.
                        //So we read back the previous data

                        //26.3.2019 : I'm making the new class because the main activity need to use to, so the share the method
                        //18.5.2019 : We change all from modelgettjukname to modelgetuid
                        new RunTransaction().runTransactionUserClick(databaseReference, userUid, model.getUid());
                        new RunTransaction().runTransactionKaranganMostVisited(progressBar, databaseReference, MainActivity.this, userUid, model.getUid(), model.getTajukPenuh(), model.getDeskripsiPenuh(), model.getTarikh(), model.getKarangan(), model.getVote(), model.getMostVisited(), model.getUserLastVisitedDate(), "PUNCA");

                    }
                });
            }


            @NonNull
            @Override
            public KaranganViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.karangan_item, viewGroup, false);
                return new KaranganViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.INVISIBLE);
                // if there are no chat messages, show a view that invites the user to add a message
                linearLayoutHow.setVisibility(firebaseRecyclerAdapter.getItemCount() == 0 && firebaseRecyclerAdapter2.getItemCount() == 0 && firebaseRecyclerAdapter3.getItemCount() == 0
                        && firebaseRecyclerAdapter4.getItemCount() == 0 && firebaseRecyclerAdapter5.getItemCount() == 0 && firebaseRecyclerAdapter6.getItemCount() == 0 ? View.VISIBLE : View.GONE);

            }
        };
        //Display
        //1. Set the recycler view
        recyclerViewNumber.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewNumber.setAdapter(firebaseRecyclerAdapter2);
        //2. FirebaseUI
        // firebaseRecyclerAdapter2.notifyDataSetChanged();
        firebaseRecyclerAdapter2.startListening();
    }

    //Method tag
    private void setRecyclerView(final String search, final RecyclerView recyclerViewNumber) {
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        linearLayoutHow.setVisibility(View.GONE);
        scrollViewUtama.setVisibility(View.GONE);

        //Making query
        Query query = databaseReference.child("karangan").child("USAHA").orderByChild("karanganTag").startAt(search.toUpperCase()).endAt(search.toUpperCase() + "\uf8ff");
        //FirebaseUI
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Karangan>()
                .setQuery(query, Karangan.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Karangan, KaranganViewHolder>(firebaseRecyclerOptions) {
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
                        //The window cannot editable, takleh nak pergi mana2
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        //This part we will update the database when user click the specific karangan
                        //1. We need to update the last visited karangan
                        new Others().lastVisitedKarangan(databaseReference, userUid, model);
                        //2. So about the mostvisited karangan.
                        //So we read back the previous data

                        //26.3.2019 : I'm making the new class because the main activity need to use to, so the share the method
                        new RunTransaction().runTransactionUserClick(databaseReference, userUid, model.getUid());
                        new RunTransaction().runTransactionKaranganMostVisited(progressBar, databaseReference, MainActivity.this, userUid, model.getUid(), model.getTajukPenuh(), model.getDeskripsiPenuh(), model.getTarikh(), model.getKarangan(), model.getVote(), model.getMostVisited(), model.getUserLastVisitedDate(), "USAHA");

                    }
                });
            }


            @NonNull
            @Override
            public KaranganViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.karangan_item, viewGroup, false);
                return new KaranganViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.INVISIBLE);
                // if there are no chat messages, show a view that invites the user to add a message
                linearLayoutHow.setVisibility(firebaseRecyclerAdapter.getItemCount() == 0 && firebaseRecyclerAdapter2.getItemCount() == 0 && firebaseRecyclerAdapter3.getItemCount() == 0
                        && firebaseRecyclerAdapter4.getItemCount() == 0 && firebaseRecyclerAdapter5.getItemCount() == 0 && firebaseRecyclerAdapter6.getItemCount() == 0 ? View.VISIBLE : View.GONE);

            }
        };
        //Display
        //1. Set the recycler view
        recyclerViewNumber.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewNumber.setAdapter(firebaseRecyclerAdapter);
        //2. FirebaseUI
        //  firebaseRecyclerAdapter.notifyDataSetChanged();
        firebaseRecyclerAdapter.startListening();
    }

    //This is method for the soft keyboard search button entered
    private void setEditTextSearchEditor() {
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    setFirebaseRecyclerAdapter(editTextSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setEditTextSearchDrawableRight() {
        editTextSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //  final int DRAWABLE_LEFT = 0;
                //     final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                //  final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editTextSearch.getRight() - editTextSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (!TextUtils.isEmpty(editTextSearch.getText().toString())) {
                            setFirebaseRecyclerAdapter(editTextSearch.getText().toString());
                        }

                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listID();
        if (savedInstanceState != null) {
            mod = savedInstanceState.getString(SAVED_MOD);
//        } else {
//            sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,
//                    Context.MODE_PRIVATE);
//            if (sharedPreferences.contains(SHARED_PREFERENCES_MOD)) {
//                //At this part, we called back the mod because we want to transfer the value of the mod and then change the tick at the menu item
//                mod = sharedPreferences.getString(SHARED_PREFERENCES_MOD, "");
//            }
        }

        setmCountDownTimer();
        setEditTextSearchEditor();
        setEditTextSearchDrawableRight();
        allButton();
    }

    //This is for toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString(SAVED_MOD, mod);
        super.onSaveInstanceState(outState, outPersistentState);
    }

//    private void setBillingClient() {
//        billingClient = BillingClient.newBuilder(this)
//                .setListener(this)
//                .enablePendingPurchases()
//                .build();
//
//        billingClient.startConnection(new BillingClientStateListener() {
//            @Override
//            public void onBillingSetupFinished(BillingResult billingResult) {
////                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
////                    Toast.makeText(getApplicationContext(), "Success connect billing", Toast.LENGTH_SHORT).show();
////                } else {
////                    Toast.makeText(getApplicationContext(), "Result: " + billingResult, Toast.LENGTH_SHORT).show();
////                }
//
//            }
//
//            @Override
//            public void onBillingServiceDisconnected() {
//                // Toast.makeText(getApplicationContext(), "Disconnected from Billing...", Toast.LENGTH_SHORT).show();
//            }
//
//        });
//    }
//
//    private void loadProduct(List<SkuDetails> skuDetails, RecyclerView recyclerView) {
//        MyTipsAdapter myTipsAdapter = new MyTipsAdapter(this, skuDetails, billingClient);
//        recyclerView.setAdapter(myTipsAdapter);
//    }


//    //23.6.2019
//    @Override
//    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
//        if (purchases != null) {
//            Toast.makeText(getApplicationContext(), "Terima kasih atas tips anda RM" + purchases.size() + ", sangat membantu :)", Toast.LENGTH_SHORT).show();
//            //After that, we store the information that the user made the payment.
//            final String itemUid = FirebaseDatabase.getInstance().getReference().push().getKey();
//            Donation donation = new Donation(userUid, itemUid, GetTarikhMasa(), String.valueOf(purchases.size()));
//            FirebaseDatabase.getInstance().getReference().child("donation").child(userUid).setValue(donation);
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {

            //27.7.2019 so we store the information if the user press the about.
            final String uid = FirebaseDatabase.getInstance().getReference().push().getKey();
            AboutClicked aboutClicked = new AboutClicked(userUid, uid, GetTarikhMasa());
            if (uid != null)
                FirebaseDatabase.getInstance().getReference().child("aboutClicked").child(userUid).setValue(aboutClicked);


            //23.6.2019: We will use new custom alert dialog.
            //According to this tutor: https://stackoverflow.com/questions/23669296/create-a-alertdialog-in-android-with-custom-xml-view
            final Dialog dialogAbout = new Dialog(MainActivity.this);
            dialogAbout.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogAbout.setContentView(R.layout.dialog_about);
            TextView textViewTitle = dialogAbout.findViewById(R.id.text_view_about_title);
            final TextView textViewCredit = dialogAbout.findViewById(R.id.text_view_about_credit);
            textViewTitle.setText(Html.fromHtml(getString(R.string.about_title)));
//
//          / final RecyclerView recyclerViewProduct = dialogAbout.findViewById(R.id.recycler_view_product);
//
//            // recyclerViewProduct.setHasFixedSize(true);
//            recyclerViewProduct.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            textViewCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //textViewCredit.setText(R.string.about_credit);
                    if (!isDisplaying) {
                        textViewCredit.setText(R.string.about_credit); //TODO: Update version 2.42
                        isDisplaying = true;
                    } else {
                        textViewCredit.setText(R.string.kredit);
                        isDisplaying = false;
                    }
                }
            });


            //TODO: Payment we will hold on. 9.7.2019.
//            //Make billing, check if ready or not (this part we creating the billing)
//            if (billingClient.isReady()) {
//                SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder()
//                        .setSkusList(Arrays.asList("tips_1", "tips_5"))
//                        .setType(BillingClient.SkuType.INAPP)
//                        .build();
//
//                billingClient.querySkuDetailsAsync(skuDetailsParams, new SkuDetailsResponseListener() {
//                    @Override
//                    public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
//                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                            loadProduct(skuDetailsList, recyclerViewProduct);
//                        }
//                    }
//                });
//            }


            dialogAbout.show();
            return true;
        }
        if (id == R.id.action_feedback) {
            Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
            intent.putExtra("userUid", userUid);
            intent.putExtra("phoneModel", phoneModel);
            startActivities(new Intent[]{intent});
            return true;
        }


        if (id == R.id.action_skin) {
            if (item.isChecked()) {
                SkinEngine.changeSkin(R.style.AppTheme);
                item.setChecked(false);

                //Shared Preference
                sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                mod = "PUTIH";

                editor.putString(SHARED_PREFERENCES_MOD, mod);
                editor.apply();

                // we clear the text search to avoid the changes of the color night mode
                editTextSearch.setText("");
            } else {
                SkinEngine.changeSkin(R.style.AppNightTheme);
                item.setChecked(true);

                //Shared Preference
                sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                mod = "HITAM";
                editor.putString(SHARED_PREFERENCES_MOD, mod);
                editor.apply();

                editTextSearch.setText("");

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkMod(String modHere, MenuItem menuItem) {
        switch (modHere) {
            case "PUTIH":
                menuItem.setChecked(false);
                break;
            case "HITAM":
                menuItem.setChecked(true);
                break;
            default:
                menuItem.setTitle("Yooo");
                break;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_skin);
        if (mod != null) {
            checkMod(mod, menuItem);
        } else {

            //To avoid the unreachable value of mod, we called back once more time at here (onprepareoptionsmenu)
            sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,
                    Context.MODE_PRIVATE);
            if (sharedPreferences.contains(SHARED_PREFERENCES_MOD)) {
                //At this part, we called back the mod because we want to transfer the value of the mod and then change the tick at the menu item
                mod = sharedPreferences.getString(SHARED_PREFERENCES_MOD, "");
            }

            //Then we check back the value
            if (mod != null) {
                checkMod(mod, menuItem);
            } else {
                menuItem.setEnabled(false);
            }

        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void allButton() {
        buttonSenaraiKarangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JenisKaranganActivity.class);
                intent.putExtra("userUid", userUid);
                startActivities(new Intent[]{intent});
            }
        });
        buttonTipsKarangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TipsKaranganActivity.class));
            }
        });
        buttonHantarKarangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HantarKaranganActivity.class);
                intent.putExtra("userUid", userUid);
                startActivities(new Intent[]{intent});
            }
        });
        buttonForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForumSplashActivity.class);
                intent.putExtra("userUid", userUid);
                intent.putExtra("phoneModel", phoneModel);
                startActivities(new Intent[]{intent});
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().goOnline();
    }

    private void setmCountDownTimer() {
        Calendar start_calendar = Calendar.getInstance();
        Calendar end_calendar = Calendar.getInstance();
        end_calendar.set(Calendar.DAY_OF_MONTH, 4);
        end_calendar.set(Calendar.MONTH, 11 - 1);
        end_calendar.set(Calendar.YEAR, 2019);
        end_calendar.set(Calendar.HOUR_OF_DAY, 8);
        end_calendar.set(Calendar.SECOND, 0);
        end_calendar.set(Calendar.MINUTE, 0);

        long start_millis = start_calendar.getTimeInMillis(); //get the start time in milliseconds
        long end_millis = end_calendar.getTimeInMillis(); //get the end time in milliseconds
        long total_millis = (end_millis - start_millis); //total time in milliseconds

        //1000 = 1 second interval
        CountDownTimer cdt = new CountDownTimer(total_millis, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                String timerSPM = days + " Hari " + hours + " Jam " + minutes + " Minit " + seconds + " Saat";
                textViewCountdownSPM.setText(timerSPM); //You can compute the millisUntilFinished on hours/minutes/seconds
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                textViewCountdownSPM.setText("Finish!");
            }
        };
        cdt.start();
    }


}
