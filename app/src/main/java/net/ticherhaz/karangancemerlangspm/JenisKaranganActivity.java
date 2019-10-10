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

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyUserMetadata;
import com.adcolony.sdk.AdColonyZone;
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
    //private AdView adView;

    private AdColonyInterstitial ad;
    private AdColonyInterstitialListener listener;
    private AdColonyAdOptions adOptions;

    private void setTimeAdsColony() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ad != null)
                    ad.show();
            }
        }, 10000);
    }

    private void setAdsColony() {
        // Construct optional app options object to be sent with configure
        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                //.setUserID("")
                .setKeepScreenOn(true);

        // Configure AdColony in your launching Activity's onCreate() method so that cached ads can
        // be available as soon as possible.
        AdColony.configure(this, appOptions, getResources().getString(R.string.adColony_app_id), getResources().getString(R.string.adColony_zone_id_2));

        // Optional user metadata sent with the ad options in each request
        AdColonyUserMetadata metadata = new AdColonyUserMetadata()
                .setUserAge(26)
                .setUserEducation(AdColonyUserMetadata.USER_EDUCATION_BACHELORS_DEGREE)
                .setUserGender(AdColonyUserMetadata.USER_MALE);

        // Ad specific options to be sent with request
        adOptions = new AdColonyAdOptions().setUserMetadata(metadata);

        // Set up listener for interstitial ad callbacks. You only need to implement the callbacks
        // that you care about. The only required callback is onRequestFilled, as this is the only
        // way to get an ad object.
        listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                // Ad passed back in request filled callback, ad can now be shown
                JenisKaranganActivity.this.ad = ad;
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                // Ad request was not filled
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                // Ad opened, reset UI to reflect state change

            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                // Request a new ad if ad is expiring

                AdColony.requestInterstitial(getResources().getString(R.string.adColony_zone_id_2), this, adOptions);

            }
        };
    }


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


        setAdsColony();

        setTimeAdsColony();

        //adsLoaderBanner();
        retrieveData();
        setFirebaseRecyclerAdapter();
    }

//    private void adsLoaderBanner() {
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//
//        // Initialize the Mobile Ads SDK.
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//
//        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
//        // values/strings.xml.
//        adView = findViewById(R.id.ad_view);
//
//        // Create an ad request. Check your logcat output for the hashed device ID to
//        // get test ads on a physical device. e.g.
//        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .build();
//
//        // Start loading the ad in the background.
//        adView.loadAd(adRequest);
//
//    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
//        if (adView != null) {
//            adView.resume();
//        }

        // It's somewhat arbitrary when your ad request should be made. Here we are simply making
        // a request if there is no valid ad available onResume, but really this can be done at any
        // reasonable time before you plan on showing an ad.
        if (ad == null || ad.isExpired()) {
            // Optionally update location info in the ad options for each request:
            // LocationManager locationManager =
            //     (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Location location =
            //     locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // adOptions.setUserMetadata(adOptions.getUserMetadata().setUserLocation(location));
            AdColony.requestInterstitial(getResources().getString(R.string.adColony_zone_id_2), listener, adOptions);
        }
    }

    /**
     * Called when leaving the activity
     */
    @Override
    protected void onPause() {
//        if (adView != null) {
//            adView.pause();
//        }
        super.onPause();
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
//        if (adView != null) {
//            adView.destroy();
//        }
        super.onDestroy();
    }

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
                                    long size = dataSnapshot1.child("mostVisited").getValue(Long.class);
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
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
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
