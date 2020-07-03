package net.ticherhaz.karangancemerlangspm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.model.Jenis;
import net.ticherhaz.karangancemerlangspm.viewHolder.JenisViewHolder;

import static net.ticherhaz.karangancemerlangspm.util.Others.isNetworkAvailable;
import static net.ticherhaz.karangancemerlangspm.util.Others.messageInternetMessage;

public class JenisKaranganActivity extends SkinActivity {

    //private static final String AD_UNIT_ID_BANNER = "ca-app-pub-3940256099942544/9214589741";
    // private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712";
    //---BANNER START ----
    //private FrameLayout adContainerView;
    //private AdView adView;
    //---BANNER END ----

    //RecyclerView
    private RecyclerView recyclerView;
    //FirebaseUI
    private FirebaseRecyclerOptions<Jenis> firebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<Jenis, JenisViewHolder> firebaseRecyclerAdapter;
    //Firebase Database
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private String userUid;
    private String karanganJenis;
    private SwipeRefreshLayout swipeRefreshLayout;

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isNetworkAvailable(JenisKaranganActivity.this)) {
                            //if has internet connection
                            setFirebaseRecyclerAdapter();
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            messageInternetMessage(JenisKaranganActivity.this);
                        }
                    }
                }, 500);
            }
        });
    }

    //List id
    private void listID() {
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressbar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        setSwipeRefreshLayout();
        //Firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("jenis");

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

        retrieveData();
        setFirebaseRecyclerAdapter();
    }

//    private void loadBanner() {
//        // Create an ad request. Check your logcat output for the hashed device ID to
//        // get test ads on a physical device. e.g.
//        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
//        adView = new AdView(this);
//        adView.setAdUnitId(getString(R.string.bannerJenisUid));
//        //adView.setAdUnitId(AD_UNIT_ID_BANNER);
//        adContainerView.removeAllViews();
//        adContainerView.addView(adView);
//        AdSize adSize = getAdSize();
//        adView.setAdSize(adSize);
//        AdRequest adRequest =
//                new AdRequest.Builder().build();
//        // Start loading the ad in the background.
//        adView.loadAd(adRequest);
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // AdsChecker(dahPremium, imageViewAds, adContainerView, false);
//            }
//
//            @Override
//            public void onAdClosed() {
//            }
//
//            @Override
//            public void onAdLoaded() {
//                // AdsChecker(dahPremium, imageViewAds, adContainerView, true);
//            }
//
//        });
//    }
//
//    private AdSize getAdSize() {
//        // Determine the screen width (less decorations) to use for the ad width.
//        Display display = getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        display.getMetrics(outMetrics);
//
//        float density = outMetrics.density;
//
//        float adWidthPixels = adContainerView.getWidth();
//
//        // If the ad hasn't been laid out, default to the full screen width.
//        if (adWidthPixels == 0) {
//            adWidthPixels = outMetrics.widthPixels;
//        }
//
//        int adWidth = (int) (adWidthPixels / density);
//
//        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
//    }
//
//    /**
//     * Called when leaving the activity
//     */
//    @Override
//    public void onPause() {
//        if (adView != null) {
//            adView.pause();
//        }
//        super.onPause();
//    }
//
//    /**
//     * Called when returning to the activity
//     */
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (adView != null) {
//            adView.resume();
//        }
//    }
//
//    /**
//     * Called before the activity is destroyed
//     */
//    @Override
//    public void onDestroy() {
//        if (adView != null) {
//            adView.destroy();
//        }
//        super.onDestroy();
//    }

    //Method retrieve the data
    private void retrieveData() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            userUid = intent.getExtras().getString("userUid");
            karanganJenis = intent.getExtras().getString("karanganJenis");
        }
    }


    //Set firebaseAdapter
    private void setFirebaseRecyclerAdapter() {
        progressBar.setVisibility(View.VISIBLE);
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Jenis>()
                .setQuery(databaseReference, Jenis.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Jenis, JenisViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final JenisViewHolder holder, int position, @NonNull final Jenis model) {
                holder.getTextViewTitle().setText(model.getTitle());
                holder.getTextViewDescription().setText(model.getDescription());


                //This part is to retrieve the total viewer for specific jenis karangan.
                FirebaseDatabase.getInstance().getReference().child("karangan").child(model.getJenisUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            long sum = 0;
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.exists()) {
                                    final long size = dataSnapshot1.child("mostVisited").getValue(Long.class);
                                    sum += size;
                                    holder.getTextViewViewer().setText(String.valueOf(sum));
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //Onclick the view
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //This part we continue to the next activity
                        Intent intent = new Intent(JenisKaranganActivity.this, SenaraiKaranganActivity.class);
                        intent.putExtra("userUid", userUid);
                        intent.putExtra("karanganJenis", model.getJenisUid());
                        startActivities(new Intent[]{intent});
                    }
                });
            }

            @NonNull
            @Override
            public JenisViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.jenis_item, viewGroup, false);
                return new JenisViewHolder(view);
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
        // firebaseRecyclerAdapter.notifyDataSetChanged();
        firebaseRecyclerAdapter.startListening();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    messageInternetMessage(JenisKaranganActivity.this);
                }

            }
        }, 5000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StartAppSDK.init(this, getResources().getString(R.string.start_apps_id), false);
//        Toolbar toolbar = findViewById(R.id.app_bar);
//        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_jenis_karangan);
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
