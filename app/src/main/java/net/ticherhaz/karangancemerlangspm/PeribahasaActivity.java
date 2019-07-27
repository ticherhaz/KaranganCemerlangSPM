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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.Model.Peribahasa;
import net.ticherhaz.karangancemerlangspm.ViewHolder.PeribahasaViewHolder;

import static net.ticherhaz.karangancemerlangspm.Util.Others.isNetworkAvailable;
import static net.ticherhaz.karangancemerlangspm.Util.Others.messageInternetMessage;

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

                        Toast.makeText(getApplicationContext(), "Disalin ke Papan Klip", Toast.LENGTH_SHORT).show();

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

        setFirebaseRecyclerAdapter(false);
        settPeri();

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
