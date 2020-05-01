package net.ticherhaz.karangancemerlangspm;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.model.Peribahasa;
import net.ticherhaz.karangancemerlangspm.viewHolder.PeribahasaViewHolder;

import static net.ticherhaz.karangancemerlangspm.util.Others.isNetworkAvailable;
import static net.ticherhaz.karangancemerlangspm.util.Others.messageInternetMessage;

public class PeribahasaActivity extends SkinActivity {

    //private static final String AD_UNIT_ID_BANNER = "ca-app-pub-3940256099942544/9214589741";
    //private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712";

    //---BANNER START ----
    private FrameLayout adContainerView;
    private AdView adView;
    //---BANNER END ----
    //---INTERSTITIAL END ----
    private InterstitialAd interstitialAd;


    //Declare variables
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseRecyclerOptions<Peribahasa> firebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<Peribahasa, PeribahasaViewHolder> firebaseRecyclerAdapter;
    private TextInputEditText tPeri;
    private TextView tvPeriRekod;
    private Toast toast;

    private void settPeri() {
        tPeri.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (tPeri.getText() != null)
                    if (tPeri.getText().toString().equals("")) {
                        if (firebaseRecyclerAdapter != null) {
                            firebaseRecyclerAdapter.stopListening();
                            setFirebaseRecyclerAdapter(false);
                            tPeri.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);
                        }
                    } else {
                        //Display drawable right
                        tPeri.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, R.drawable.ic_no, 0);
                        setFirebaseRecyclerAdapter(true);
                    }
            }
        });
        setEditTextSearchDrawableRight();
    }

    public void ShowToast(Context context, final String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void setFirebaseRecyclerAdapter(final boolean isSearching) {
        Query query = null;
        if (isSearching) {
            //Making query
            if (tPeri.getText() != null) {
                final String search = tPeri.getText().toString().toLowerCase();
                query = databaseReference.orderByChild("title").startAt(search).endAt(search + "\uf8ff");
            }
        } else {
            query = databaseReference.orderByChild("title");
        }

        if (query != null)
            //Making Option
            firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Peribahasa>()
                    .setQuery(query, Peribahasa.class)
                    .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Peribahasa, PeribahasaViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final PeribahasaViewHolder peribahasaViewHolder, int i, @NonNull final Peribahasa peribahasa) {
                peribahasaViewHolder.getTextViewTitle().setText(peribahasa.getTitle());
                peribahasaViewHolder.getTextViewDescription().setText(peribahasa.getDescription());

                peribahasaViewHolder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //When user press it, it will save to clipboard
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("title", peribahasa.getTitle());
                        if (clipboard != null)
                            clipboard.setPrimaryClip(clip);

                        ShowToast(PeribahasaActivity.this, "Disalin ke Papan Klip");

                        peribahasaViewHolder.getView().setEnabled(false);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                peribahasaViewHolder.getView().setEnabled(true);
                            }
                        }, 2300);
                    }
                });
            }

            @NonNull
            @Override
            public PeribahasaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.peribahasa_item, parent, false);
                return new PeribahasaViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                progressBar.setVisibility(View.INVISIBLE);

                //If in database there is no result
                if (firebaseRecyclerAdapter.getItemCount() == 0) {
                    tvPeriRekod.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tvPeriRekod.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        };

        //Display
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(PeribahasaActivity.this));
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    messageInternetMessage(PeribahasaActivity.this);
                }

            }
        }, 5000);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setEditTextSearchDrawableRight() {
        tPeri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // final int DRAWABLE_LEFT = 0;
                //final int DRAWABLE_TOP = 1;
                //final int DRAWABLE_RIGHT = 2;
                //final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (tPeri.getRight() - tPeri.getCompoundPaddingRight())) {
                        //Then check it and clear
                        if (tPeri.getText() != null)
                            if (!TextUtils.isEmpty(tPeri.getText().toString())) {
                                tPeri.getText().clear();
                            }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void listID() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        setSwipeRefreshLayout();
        progressBar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.recycler_view);
        tPeri = findViewById(R.id.tiet_peri);
        tvPeriRekod = findViewById(R.id.tv_peri_rekod);

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("peribahasa");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adContainerView = findViewById(R.id.ad_view_container);
        // Since we're loading the banner based on the adContainerView size, we need to wait until this
        // view is laid out before we can get the width.
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });

        interstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        interstitialAd.setAdUnitId(getString(R.string.interstitialPeribahasaUid));
        //interstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {

            }
        });
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        }


        setFirebaseRecyclerAdapter(false);
        settPeri();
    }

    private void loadBanner() {
        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.bannerPeribahasa));
        //adView.setAdUnitId(AD_UNIT_ID_BANNER);
        adContainerView.removeAllViews();
        adContainerView.addView(adView);
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        AdRequest adRequest =
                new AdRequest.Builder().build();
        // Start loading the ad in the background.
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // AdsChecker(dahPremium, imageViewAds, adContainerView, false);
            }

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdLoaded() {
                // AdsChecker(dahPremium, imageViewAds, adContainerView, true);
            }

        });
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (interstitialAd != null && interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        }, 2000);
    }

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //If there is connection
                        if (isNetworkAvailable(PeribahasaActivity.this)) {
                            setFirebaseRecyclerAdapter(false);
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            messageInternetMessage(PeribahasaActivity.this);
                        }
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peribahasa);
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
