package net.ticherhaz.karangancemerlangspm;

import static net.ticherhaz.karangancemerlangspm.utils.Others.messageInternetMessage;
import static net.ticherhaz.tarikhmasa.TarikhMasa.ConvertTarikhMasa2LocalTime;

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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.model.HubungiKamiChat;
import net.ticherhaz.karangancemerlangspm.model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.viewHolder.HubungiKamiViewHolder;

public class HubungiKamiUserActivity extends SkinActivity {

    private final static String adminUid = "-LkmB21s35x7L9Le27NW";

    private FirebaseDatabase fDe;
    private DatabaseReference dRe;

    private FirebaseRecyclerOptions<HubungiKamiChat> fRO;
    private FirebaseRecyclerAdapter<HubungiKamiChat, HubungiKamiViewHolder> fRa;

    private RecyclerView rv;
    private ProgressBar pB;


    private void listID() {
        rv = findViewById(R.id.rv_hubungi_kami);
        pB = findViewById(R.id.progressbar);

        fDe = FirebaseDatabase.getInstance();
        dRe = fDe.getReference();

        setfRa();
    }

    private void setfRa() {
        fRO = new FirebaseRecyclerOptions.Builder<HubungiKamiChat>()
                .setQuery(dRe.child("hubungiKamiChat").child(adminUid), HubungiKamiChat.class)
                .build();

        fRa = new FirebaseRecyclerAdapter<HubungiKamiChat, HubungiKamiViewHolder>(fRO) {
            @Override
            protected void onBindViewHolder(@NonNull final HubungiKamiViewHolder hubungiKamiViewHolder, final int i, @NonNull final HubungiKamiChat hubungiKamiChat) {
                hubungiKamiViewHolder.getTvDate().setText(ConvertTarikhMasa2LocalTime(hubungiKamiChat.getLastUpdated()));

                dRe.child("registeredUser").child(hubungiKamiChat.getSenderUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            final RegisteredUser registeredUser = snapshot.getValue(RegisteredUser.class);
                            if (registeredUser != null) {
                                final String username = registeredUser.getUsername();
                                hubungiKamiViewHolder.getTvMessage().setText(username);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                hubungiKamiViewHolder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HubungiKamiUserActivity.this, HubungiKamiActivity.class);
                        intent.putExtra("senderUid", hubungiKamiChat.getSenderUid());
                        intent.putExtra("chatUid", hubungiKamiChat.getChatUid());
                        startActivities(new Intent[]{intent});
                    }
                });
            }

            @NonNull
            @Override
            public HubungiKamiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hubungi_kami_item, parent, false);
                return new HubungiKamiViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                pB.setVisibility(View.GONE);
            }
        };

        //Display
        fRa.startListening();
        fRa.notifyDataSetChanged();
        rv.setLayoutManager(new LinearLayoutManager(HubungiKamiUserActivity.this));
        rv.setAdapter(fRa);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pB.getVisibility() == View.VISIBLE) {
                    messageInternetMessage(HubungiKamiUserActivity.this);
                }

            }
        }, 5000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubungi_kami_user);
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
