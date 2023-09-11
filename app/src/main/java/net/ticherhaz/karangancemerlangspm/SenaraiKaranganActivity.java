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
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.model.Karangan;
import net.ticherhaz.karangancemerlangspm.utils.Others;
import net.ticherhaz.karangancemerlangspm.utils.RunTransaction;
import net.ticherhaz.karangancemerlangspm.viewHolder.KaranganViewHolder;

public class SenaraiKaranganActivity extends SkinActivity {

    //private static final String AD_UNIT_ID_BANNER = "ca-app-pub-3940256099942544/9214589741";
    //private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712";
    //---BANNER START ----
    //private FrameLayout adContainerView;
    //private AdView adView;
    //---BANNER END ----

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
    private String karanganJenis;

    //Ads
    //private InterstitialAd mInterstitialAd;

    //Method listID
    private void listID() {
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressbar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        setSwipeRefreshLayout();
        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

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
//        adView.setAdUnitId(getString(R.string.bannerSenaraiUid));
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
                        new Others().lastVisitedKarangan(databaseReference, userUid, model);
                        //2. So about the mostvisited karangan.

                        //26.3.2019 : I'm making the new class because the main activity need to use to, so the share the method
                        //18.5.2019 : Changing the model.getTajukPenuh to become model.getUid
                        new RunTransaction().runTransactionUserClick(databaseReference, userUid, model.getUid());
                        new RunTransaction().runTransactionKaranganMostVisited(progressBar, databaseReference, SenaraiKaranganActivity.this, userUid, model.getUid(), model.getTajukPenuh(), model.getDeskripsiPenuh(), model.getTarikh(), model.getKarangan(), model.getVote(), model.getMostVisited(), model.getUserLastVisitedDate(), karanganJenis);

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
//                    Toast toast = Toast.makeText(getApplicationContext(), new InternetMessage().getMessage(), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
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
