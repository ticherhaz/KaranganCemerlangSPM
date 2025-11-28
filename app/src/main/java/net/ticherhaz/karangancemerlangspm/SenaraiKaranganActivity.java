package net.ticherhaz.karangancemerlangspm;

import static net.ticherhaz.karangancemerlangspm.utils.Others.isNetworkAvailable;
import static net.ticherhaz.karangancemerlangspm.utils.Others.messageInternetMessage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.ticherhaz.karangancemerlangspm.model.Karangan;
import net.ticherhaz.karangancemerlangspm.utils.Others;
import net.ticherhaz.karangancemerlangspm.utils.RunTransaction;
import net.ticherhaz.karangancemerlangspm.viewHolder.KaranganViewHolder;

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

    private String karanganJenis;
    private AdView adView;

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
            karanganJenis = intent.getExtras().getString("karanganJenis");
        }
    }

    //Method firebaseUI
    private void setFirebaseRecyclerAdapter() {
        //Set progressbar
        progressBar.setVisibility(View.VISIBLE);
        //FirebaseUI
        Query query = databaseReference.child("karangan").child(karanganJenis).orderByChild("karanganJenis").equalTo(karanganJenis);
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Karangan>()
                .setQuery(query, Karangan.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Karangan, KaranganViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull KaranganViewHolder holder, int position, @NonNull final Karangan model) {
                //Display the data
                holder.getTextViewTajuk().setText(model.getTajukPenuh());
                holder.getTextViewDeskripsi().setText(model.getDeskripsiPenuh());
                holder.getTextViewViewer().setText(String.valueOf(model.getMostVisited()));
                holder.getTextViewFav().setText(String.valueOf(model.getVote()));

                //When user click specific title
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Display the progress bar1
                        progressBar.setVisibility(View.VISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        //This part we will update the database when user click the specific karangan
                        //1. We need to update the last visited karangan
                        new Others().lastVisitedKarangan(databaseReference, "1", model);
                        //2. So about the mostvisited karangan.

                        //26.3.2019 : I'm making the new class because the main activity need to use to, so the share the method
                        //18.5.2019 : Changing the model.getTajukPenuh to become model.getUid
                        new RunTransaction().runTransactionUserClick(databaseReference, "1", model.getUid());
                        new RunTransaction().runTransactionKaranganMostVisited(progressBar, databaseReference, SenaraiKaranganActivity.this, "1", model.getUid(), model.getTajukPenuh(), model.getDeskripsiPenuh(), model.getTarikh(), model.getKarangan(), model.getVote(), model.getMostVisited(), model.getUserLastVisitedDate(), karanganJenis);

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
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.karangan_item, viewGroup, false);
                return new KaranganViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                //When dataChanged mean, after finished load the data
                //Hide the progressbar
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
        //Display
        //1. Set the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        //2. FirebaseUI
        //firebaseRecyclerAdapter.notifyDataSetChanged();
        firebaseRecyclerAdapter.startListening();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    messageInternetMessage(SenaraiKaranganActivity.this);
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
        setContentView(R.layout.activity_senarai_karangan);
        listID();

        initAdView();
    }

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isNetworkAvailable(SenaraiKaranganActivity.this)) {
                            //if has internet connection
                            setFirebaseRecyclerAdapter();
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            messageInternetMessage(SenaraiKaranganActivity.this);
                        }

                    }
                }, 500);

            }
        });
    }

    private void initAdView() {
        // Create a new ad view.
        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-1320314109772118/6975309751");

        // Request an anchored adaptive banner with a width of 360.
        AdSize adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, 360);
        adView.setAdSize(adSize);

        FrameLayout adViewContainer = findViewById(R.id.ad_view_container);
        adViewContainer.removeAllViews();
        adViewContainer.addView(adView);


        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void destroyBanner() {
        // Remove banner from view hierarchy.
        if (adView != null) {
            ViewGroup parentView = (ViewGroup) adView.getParent();
            if (parentView != null) {
                parentView.removeView(adView);
            }
            // Destroy the banner ad resources.
            adView.destroy();
            // Drop reference to the banner ad.
            adView = null;
        }
    }

    @Override
    protected void onDestroy() {
        destroyBanner();
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
