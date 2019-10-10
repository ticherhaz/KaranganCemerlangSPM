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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private AdColonyInterstitial ad;
    private AdColonyInterstitialListener listener;
    private AdColonyAdOptions adOptions;

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

        setAdsColony();

        setTimeAdsColony();

        setFirebaseRecyclerAdapter(false);
        settPeri();
    }

    private void setTimeAdsColony() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ad != null)
                    ad.show();
            }
        }, 10000);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            AdColony.requestInterstitial(getResources().getString(R.string.adColony_zone_id_1), listener, adOptions);
        }
    }

    private void setAdsColony() {
        // Construct optional app options object to be sent with configure
        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                //.setUserID("")
                .setKeepScreenOn(true);

        // Configure AdColony in your launching Activity's onCreate() method so that cached ads can
        // be available as soon as possible.
        AdColony.configure(this, appOptions, getResources().getString(R.string.adColony_app_id), getResources().getString(R.string.adColony_zone_id_1));

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
                PeribahasaActivity.this.ad = ad;
                Toast.makeText(PeribahasaActivity.this, "Success", Toast.LENGTH_LONG).show();
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

                AdColony.requestInterstitial(getResources().getString(R.string.adColony_zone_id_1), this, adOptions);

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
