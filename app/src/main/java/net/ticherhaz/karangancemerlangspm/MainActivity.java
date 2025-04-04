package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import net.ticherhaz.karangancemerlangspm.model.Karangan;
import net.ticherhaz.karangancemerlangspm.utils.Others;
import net.ticherhaz.karangancemerlangspm.utils.PermissionUtils;
import net.ticherhaz.karangancemerlangspm.utils.RunTransaction;
import net.ticherhaz.karangancemerlangspm.viewHolder.KaranganViewHolder;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //private static final String AD_UNIT_ID_BANNER = "ca-app-pub-3940256099942544/9214589741";
    //private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712";

    private static final String SHARED_PREFERENCES_MOD = "myPreferenceMod";
    private static final String SAVED_MOD = "mySavedMod";
    private static final String SHARED_PREFERENCES = "myPreference";
    //boolean isDisplaying = false;
    //---BANNER START ----
    // private FrameLayout adContainerView;
    //private AdView adView;
    //---BANNER END ----
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
    // private Button buttonHantarKarangan;
    private Button buttonForum;
    private Button buttonPeribahasa;
    private Button buttonSumbangan;
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
        //buttonHantarKarangan = findViewById(R.id.button_hantar_karangan);
        buttonForum = findViewById(R.id.button_forum);
        buttonPeribahasa = findViewById(R.id.button_peribahasa);
        buttonSumbangan = findViewById(R.id.button_sumbangan);

        findViewById(R.id.button_kcpm_lite).setOnClickListener(v -> {
            //Then we proceed to the playStore for user to download the lastest version
            final String appPackageName = "net.ticherhaz.karangancemerlangspmlite"; // Can also use getPackageName(), as below
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (ActivityNotFoundException ex) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=net.ticherhaz.karangancemerlangspmlite");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

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

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//        adContainerView = findViewById(R.id.ad_view_container);
//        // Since we're loading the banner based on the adContainerView size, we need to wait until this
//        // view is laid out before we can get the width.
//        adContainerView.post(new Runnable() {
//            @Override
//            public void run() {
//                loadBanner();
//            }
//        });


        //Firebase Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        setEditTextSearch();
        setTextViewAnnouncement();
        setButtonPeribahasa();
        setButtonSumbangan();

        setPermissions();
    }

    private void setPermissions() {
        PermissionUtils.INSTANCE.storagePermissions(MainActivity.this, new PermissionUtils.IStoragePermission() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure() {

            }
        });

    }

    private void setButtonSumbangan() {
        buttonSumbangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TipsActivity.class));
            }
        });
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
                    textViewAnnouncement.setSelected(true);
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
                            // editTextSearch.getText().clear();
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
    public void onSaveInstanceState(Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putString(SAVED_MOD, mod);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
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
                item.setChecked(false);

                //Shared Preference
                sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                mod = "PUTIH";

                editor.putString(SHARED_PREFERENCES_MOD, mod);
                editor.apply();

                //we clear the text search to avoid the changes of the color night mode
            } else {
                item.setChecked(true);

                //Shared Preference
                sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                mod = "HITAM";
                editor.putString(SHARED_PREFERENCES_MOD, mod);
                editor.apply();

            }
            editTextSearch.setText("");
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
        end_calendar.set(Calendar.DAY_OF_MONTH, 22);
        end_calendar.set(Calendar.MONTH, 1); // 2 - 1 (need minus 1)
        end_calendar.set(Calendar.YEAR, 2021);
        end_calendar.set(Calendar.HOUR_OF_DAY, 0);
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

                String timerSPM = days + " Hari " + hours + " Jam " + minutes + " Minit " + seconds + " Saat" + "(22 Februari 2021)";
                textViewCountdownSPM.setText(timerSPM); //You can compute the millisUntilFinished on hours/minutes/seconds
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                textViewCountdownSPM.setText("Finish!");
            }
        };
        cdt.start();


        textViewCountdownSPM.setText("");
    }
}