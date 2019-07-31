package net.ticherhaz.karangancemerlangspm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.Model.HubungiKamiChat;
import net.ticherhaz.karangancemerlangspm.Model.HubungiKamiMessage;
import net.ticherhaz.karangancemerlangspm.ViewHolder.HubungiKamiViewHolder;
import net.ticherhaz.tarikhmasa.TarikhMasa;

import static net.ticherhaz.karangancemerlangspm.Util.Others.isNetworkAvailable;
import static net.ticherhaz.karangancemerlangspm.Util.Others.messageInternetMessage;
import static net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasa;

public class HubungiKamiActivity extends SkinActivity {

    public static final int VT_SENDER = 0;
    public static final int VT_RECIPIENT = 1;
    private final static String adminUid = "-LkmB21s35x7L9Le27NW";
    int _count = 30;//30 seconds
    private FirebaseDatabase fDe;
    private DatabaseReference dRe;
    private FirebaseAuth fAh;
    private FirebaseUser fUr;
    private FirebaseRecyclerOptions<HubungiKamiMessage> fRo;
    private FirebaseRecyclerAdapter<HubungiKamiMessage, HubungiKamiViewHolder> fRa;
    private String registeredUid;
    private FloatingActionButton faBSend;
    private EditText etSend;
    private RecyclerView rvHubungiKami;
    private ProgressBar pB;
    private String chatUid2, senderUid;
    private boolean canSendMessage = true;
    private Runnable countDown = new Runnable() {
        @Override
        public void run() {
            while (_count > 0) {
                _count--;
                try {
                    Thread.sleep(1000);//1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            _count = 30;//again set to 30 seconds
            canSendMessage = true;//enable send
        }
    };

    private void listID() {
        faBSend = findViewById(R.id.btn_send);
        etSend = findViewById(R.id.et_send);
        rvHubungiKami = findViewById(R.id.rv_hubungi_kami);
        pB = findViewById(R.id.progressbar);
        fDe = FirebaseDatabase.getInstance();
        dRe = fDe.getReference();
        fAh = FirebaseAuth.getInstance();
        fUr = fAh.getCurrentUser();

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            senderUid = intent.getExtras().getString("senderUid");
            setfRaAdmin();
            setFaBSendAdmin();
        } else if (fUr != null) {
            registeredUid = fUr.getUid();
            callChatUid();
            setfRa();
            setFaBSend();
        }

    }

    private void callChatUid() {
        dRe.child("hubungiKamiUid").child(registeredUid).child("chatUid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final String chatUid = dataSnapshot.getValue(String.class);
                    if (chatUid != null) {
                        chatUid2 = chatUid;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setfRaAdmin() {
        fRo = new FirebaseRecyclerOptions.Builder<HubungiKamiMessage>()
                .setQuery(dRe.child("hubungiKamiMessage").child(adminUid).child(senderUid), HubungiKamiMessage.class)
                .build();

        fRa = new FirebaseRecyclerAdapter<HubungiKamiMessage, HubungiKamiViewHolder>(fRo) {
            @Override
            protected void onBindViewHolder(@NonNull HubungiKamiViewHolder hubungiKamiViewHolder, int i, @NonNull HubungiKamiMessage hubungiKami) {
                hubungiKamiViewHolder.getTvDate().setText(TarikhMasa.GetTarikhMasaTimeAgo(hubungiKami.getDate(), "MY", true, false));
                hubungiKamiViewHolder.getTvMessage().setText(hubungiKami.getMessage());
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
        rvHubungiKami.setLayoutManager(new LinearLayoutManager(HubungiKamiActivity.this));
        rvHubungiKami.setAdapter(fRa);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pB.getVisibility() == View.VISIBLE) {
                    messageInternetMessage(HubungiKamiActivity.this);
                }

            }
        }, 5000);
    }

    private void setfRa() {
        fRo = new FirebaseRecyclerOptions.Builder<HubungiKamiMessage>()
                .setQuery(dRe.child("hubungiKamiMessage").child(registeredUid).child(adminUid), HubungiKamiMessage.class)
                .build();

        fRa = new FirebaseRecyclerAdapter<HubungiKamiMessage, HubungiKamiViewHolder>(fRo) {
            @Override
            protected void onBindViewHolder(@NonNull HubungiKamiViewHolder hubungiKamiViewHolder, int i, @NonNull HubungiKamiMessage hubungiKami) {
                hubungiKamiViewHolder.getTvDate().setText(TarikhMasa.GetTarikhMasaTimeAgo(hubungiKami.getDate(), "MY", true, false));
                hubungiKamiViewHolder.getTvMessage().setText(hubungiKami.getMessage());
            }

            @NonNull
            @Override
            public HubungiKamiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = null;
                switch (viewType) {
                    case VT_SENDER:
                        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hubungi_kami_sender_item, parent, false);
                        break;
                    case VT_RECIPIENT:
                        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hubungi_kami_item, parent, false);
                        break;
                }
                assert v != null;
                return new HubungiKamiViewHolder(v);
            }

            @Override
            public void onDataChanged() {
                pB.setVisibility(View.GONE);
            }

            @Override
            public int getItemViewType(int position) {
                if (fRa.getItem(position).getReceiverUid().equals(registeredUid))
                    return VT_RECIPIENT;
                else
                    return VT_SENDER;
            }
        };

        //Display
        fRa.startListening();
        fRa.notifyDataSetChanged();
        rvHubungiKami.setLayoutManager(new LinearLayoutManager(HubungiKamiActivity.this));
        rvHubungiKami.setAdapter(fRa);

        if (rvHubungiKami.getAdapter() != null)
            rvHubungiKami.scrollToPosition(rvHubungiKami.getAdapter().getItemCount());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pB.getVisibility() == View.VISIBLE) {
                    messageInternetMessage(HubungiKamiActivity.this);
                }
            }
        }, 5000);
    }

    private void setFaBSendAdmin() {
        faBSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etSend.getText().toString())) {
                    if (isNetworkAvailable(HubungiKamiActivity.this)) {
                        //Send message
                        final String chatUid = dRe.push().push().getKey();
                        final String hkUid = dRe.push().getKey();
                        final String date = GetTarikhMasa();
                        final String message = etSend.getText().toString();

                        //Class
                        HubungiKamiMessage hubungiKamiMessage = new HubungiKamiMessage(hkUid, chatUid, senderUid, date, message, senderUid);
                        HubungiKamiChat hubungiKamiChat = new HubungiKamiChat(chatUid, senderUid, senderUid, date);

                        if (hkUid != null && chatUid != null) {
                            dRe.child("hubungiKamiUid").child(senderUid).child(chatUid).child("chatUid").setValue(chatUid);
                            dRe.child("hubungiKamiParticipant").child(chatUid).child(adminUid).setValue(true);
                            dRe.child("hubungiKamiParticipant").child(chatUid).child(senderUid).setValue(true);

                            dRe.child("hubungiKamiChat").child(senderUid).child(chatUid).setValue(hubungiKamiChat);
                            //Store the message at admin too
                            dRe.child("hubungiKamiChat").child(adminUid).child(senderUid).setValue(hubungiKamiChat);
                            //Store the message at user
                            dRe.child("hubungiKamiMessage").child(adminUid).child(senderUid).child(chatUid).setValue(hubungiKamiMessage);
                            dRe.child("hubungiKamiMessage").child(senderUid).child(adminUid).child(chatUid).setValue(hubungiKamiMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        etSend.getText().clear();
                                        etSend.requestFocus();
                                    }
                                }
                            });
                        }


                    } else {
                        messageInternetMessage(HubungiKamiActivity.this);
                    }
                }
            }
        });
    }

    private void setFaBSend() {
        faBSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etSend.getText().toString())) {
                    if (isNetworkAvailable(HubungiKamiActivity.this)) {
                        if (canSendMessage) {

                            //Send message
                            final String chatUid = dRe.push().push().getKey();
                            final String hkUid = dRe.push().getKey();
                            final String date = GetTarikhMasa();
                            final String message = etSend.getText().toString();

                            //Class
                            HubungiKamiMessage hubungiKamiMessage = new HubungiKamiMessage(hkUid, chatUid, registeredUid, date, message, adminUid);
                            HubungiKamiChat hubungiKamiChat = new HubungiKamiChat(chatUid, registeredUid, registeredUid, date);

                            if (hkUid != null && chatUid != null) {
                                dRe.child("hubungiKamiChat").child(registeredUid).child(chatUid).setValue(hubungiKamiChat);
                                //Store the message at admin too
                                dRe.child("hubungiKamiChat").child(adminUid).child(registeredUid).setValue(hubungiKamiChat);
                                //Store the message at user
                                dRe.child("hubungiKamiMessage").child(adminUid).child(registeredUid).child(chatUid).setValue(hubungiKamiMessage);
                                dRe.child("hubungiKamiMessage").child(registeredUid).child(adminUid).child(chatUid).setValue(hubungiKamiMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            etSend.getText().clear();
                                            etSend.requestFocus();
                                        }
                                    }
                                });
                            }

                            canSendMessage = false;
                            Thread t = new Thread(countDown);
                            t.start();
                        } else {
                            Toast.makeText(getApplicationContext(), "Kamu boleh membalas semula " + _count + " saat lagi...", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        messageInternetMessage(HubungiKamiActivity.this);
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubungi_kami);
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
