package net.ticherhaz.karangancemerlangspm;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static net.ticherhaz.karangancemerlangspm.utils.Others.DismissProgressDialog;
import static net.ticherhaz.karangancemerlangspm.utils.Others.ShowProgressDialog;
import static net.ticherhaz.karangancemerlangspm.utils.Others.ShowToast;
import static net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasa;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.google.common.collect.ImmutableList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.model.DownloadKarangan;
import net.ticherhaz.karangancemerlangspm.utils.DoubleClickListener;
import net.ticherhaz.karangancemerlangspm.utils.MyProductDownloadKaranganAdapter;
import net.ticherhaz.karangancemerlangspm.utils.RunTransaction;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class KaranganDetailActivity extends SkinActivity implements PurchasesUpdatedListener {

    private BillingClient billingClient;
    private RecyclerView recyclerView;
    private ProgressBar progressBarMain;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    //TextView
    private TextView textViewTajuk;
    private TextView textViewKarangan;
    private TextView textViewTarikh;
    private TextView textViewFav;
    private TextView textViewViewer;

    private Button buttonIncreaseSize;
    private Button buttonDecreaseSize;
    private Button buttonFont;

    private String karanganJenis;
    private String userUid;
    private String uidKarangan;
    private String tajuk;
    private String karangan;
    private String tarikh;
    private int vote;
    private int mostVisited;

    //Method listID
    private void listID() {
        textViewTajuk = findViewById(R.id.text_view_tajuk);
        textViewKarangan = findViewById(R.id.text_view_karangan);
        textViewTarikh = findViewById(R.id.text_view_tarikh);
        textViewFav = findViewById(R.id.text_view_fav);
        textViewViewer = findViewById(R.id.text_view_viewer);

        buttonIncreaseSize = findViewById(R.id.button_increase_size);
        buttonDecreaseSize = findViewById(R.id.button_decrease_size);
        buttonFont = findViewById(R.id.button_font);
        progressBarMain = findViewById(R.id.pb_main);
        recyclerView = findViewById(R.id.products);
        recyclerView.setHasFixedSize(true);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("karangan");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        setBillingClient();

        retrieveData();
        displayData();
        checkLike();

        setButtonIncreaseSize();
        setButtonDecreaseSizeSize();
        setButtonFont();
    }

    private void setBillingClient() {
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(this)
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    final QueryProductDetailsParams queryProductDetailsParams =
                            QueryProductDetailsParams.newBuilder()
                                    .setProductList(ImmutableList.of(
                                            QueryProductDetailsParams.Product.newBuilder()
                                                    .setProductId("muat_turun_karangan")
                                                    .setProductType(BillingClient.ProductType.INAPP)
                                                    .build()))
                                    .build();


                    billingClient.queryProductDetailsAsync(queryProductDetailsParams, (billingResult2, productDetailsList) -> {

                                final MyProductDownloadKaranganAdapter adapter = new MyProductDownloadKaranganAdapter(KaranganDetailActivity.this, productDetailsList, billingClient);
                                runOnUiThread(() -> {
                                    recyclerView.setAdapter(adapter);
                                    progressBarMain.setVisibility(View.GONE);
                                });

                            }
                    );


                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.BILLING_UNAVAILABLE) {
                    runOnUiThread(() -> {
                        progressBarMain.setVisibility(View.GONE);
                        ShowToast(KaranganDetailActivity.this, "Billing service unavailable on device");
                    });
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                runOnUiThread(() -> {
                    progressBarMain.setVisibility(View.GONE);
                    ShowToast(KaranganDetailActivity.this, "Please try again later.");
                });
            }
        });
    }

    private void downloadFile(final Context context, final String fileName, final String fileExtension, final String destinationDirectory, final String url) {
        final DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final Uri uri = Uri.parse(url);
        final DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension); //ini akan simpan dekat dalam folder net.ticherhaz.karangancemerlangspm
        request.setDestinationInExternalPublicDir(destinationDirectory, File.separator + "Karangan Cemerlang SPM" + File.separator + fileName + fileExtension); //ini kita akan simpan dkt downloads

        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
        runOnUiThread(() -> {
            DismissProgressDialog();
            ShowToast(KaranganDetailActivity.this, "Selesai Muat Turun " + tajuk + "\nSila semak di 'Download'");
        });
    }

    private void handleConsume(final Purchase purchase) {
        // Grant entitlement to the user.
        // Acknowledge the purchase if it hasn't already been acknowledged.
        if (!purchase.isAcknowledged()) {

            ShowProgressDialog(KaranganDetailActivity.this);
            /*
             * Use this if you want to purchase many times
             */
            final ConsumeParams consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.getPurchaseToken())
                    .build();

            /**
             * NOTED! TODO: consumeAsync VS acknowledgePurchase
             * https://www.kodeco.com/754-in-app-purchases-non-renewing-subscriptions-tutorial
             * https://stackoverflow.com/a/56585836/9346054
             *
             * consumeAysnc = boleh beli banyak kali
             * acknowledgePurchase = boleh purchase sekali
             *
             */


            billingClient.consumeAsync(consumeParams, (billingResult, purchaseToken) -> {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    //Success purchased
                    final String downloadKaranganUid = FirebaseDatabase.getInstance().getReference().push().getKey();
                    final String onPurchasedDateUTC = GetTarikhMasa();
                    final long onPurchasedDate = System.currentTimeMillis();

                    final String orderUid = purchase.getOrderId();
                    final String skutName = purchase.getPackageName();
                    final String signature = purchase.getSignature();
                    final String originalJson = purchase.getOriginalJson();
                    final int purchaseState = purchase.getPurchaseState();
                    final String developerPayload = purchase.getDeveloperPayload();

                    final String userUid = databaseReference.push().getKey();

                    final DownloadKarangan downloadKarangan = new DownloadKarangan(downloadKaranganUid, uidKarangan, orderUid, skutName, signature, originalJson, developerPayload, onPurchasedDateUTC, purchaseState, onPurchasedDate);
                    //Store info in database
                    assert userUid != null;
                    databaseReference.child("downloadKarangan").child(userUid).setValue(downloadKarangan).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            final String description = "Sedang Muat Turun " + tajuk;

                            //and then we download the data from firebase storage
                            storageReference.child(uidKarangan + ".pdf").getDownloadUrl().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    runOnUiThread(() -> {
                                        final String url = Objects.requireNonNull(task1.getResult()).toString();
                                        ShowToast(KaranganDetailActivity.this, description);
                                        downloadFile(KaranganDetailActivity.this, tajuk, ".pdf", DIRECTORY_DOWNLOADS, url);
                                    });
                                } else {
                                    DismissProgressDialog();
                                    ShowToast(KaranganDetailActivity.this, "Error: " + task1.getException().getMessage() + "\nSila hubungi di ticherhaz@gmail.com");
                                }
                            });

                        }
                    });
                }

            });
        }
    }

    //Method increase size text
    private void setButtonIncreaseSize() {
        buttonIncreaseSize.setOnClickListener(v -> textViewKarangan.setTextSize(0, textViewKarangan.getTextSize() + 2.0f));
    }

    //Method increase size text
    private void setButtonDecreaseSizeSize() {
        buttonDecreaseSize.setOnClickListener(v -> textViewKarangan.setTextSize(0, textViewKarangan.getTextSize() - 2.0f));
    }

    //Set button font
    private void setButtonFont() {
        buttonFont.setOnClickListener(v -> {
            FontDialog fontDialog = new FontDialog(KaranganDetailActivity.this);
            fontDialog.setTextViewKarangan(textViewKarangan);
            fontDialog.show();
        });
    }

    //Method retrieve the data
    private void retrieveData() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            uidKarangan = intent.getExtras().getString("uidKarangan");
            karanganJenis = intent.getExtras().getString("karanganJenis");
            userUid = intent.getExtras().getString("userUid");
            tajuk = intent.getExtras().getString("tajukPenuh");
            karangan = intent.getExtras().getString("karangan");
            tarikh = intent.getExtras().getString("tarikh");
            vote = intent.getExtras().getInt("vote");
            mostVisited = intent.getExtras().getInt("mostVisited");
        }
    }

    //Method display the data
    private void displayData() {
        textViewTajuk.setText(tajuk);
        textViewKarangan.setText(karangan);
        textViewTarikh.setText(tarikh);
        textViewFav.setText(String.valueOf(vote));
        textViewViewer.setText(String.valueOf(mostVisited));
    }

    //Method check the user if user already like or not
    private void checkLike() {
        //We are using the value event listener instead single value because we want to database always online
        databaseReference.child("userFirstKaranganClick").child(userUid).child(uidKarangan).child("like").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    vote = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                    //So kalau dia dah like tukar ini kepada warna merah
                    textViewFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favourite_red, 0, 0, 0);
                    textViewFav.setCompoundDrawablePadding(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Method click the like text view
    private void setTextViewLike() {
        textViewFav.setOnClickListener(v -> {
            //If the text already become liked
            if (textViewFav.getCompoundDrawablePadding() == 1) {
                Toast.makeText(getApplicationContext(), "Anda Sudah Suka Karangan Ini", Toast.LENGTH_SHORT).show();
            } else {
                //1st. we read and update at karangan
                new RunTransaction().runTransactionUserVoteKarangan(databaseReference, karanganJenis, uidKarangan);
                databaseReference.child("userFirstKaranganClick").child(userUid).child(uidKarangan).child("like").setValue(1);
                textViewFav.setText(String.valueOf(vote + 1));
                Toast.makeText(getApplicationContext(), "Anda Suka Karangan Ini", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTextViewKarangan() {
        textViewKarangan.setOnClickListener(new DoubleClickListener() {
            //So we use the double click from the custom at the utils.
            @Override
            public void onSingleClick(View v) {
            }

            @Override
            public void onDoubleClick(View v) {
                //If the text already become liked
                if (textViewFav.getCompoundDrawablePadding() == 1) {
                    Toast.makeText(getApplicationContext(), "Anda Sudah Suka Karangan Ini", Toast.LENGTH_SHORT).show();
                } else {
                    //1st. we read and update at karangan
                    new RunTransaction().runTransactionUserVoteKarangan(databaseReference, karanganJenis, uidKarangan);
                    databaseReference.child("userFirstKaranganClick").child(userUid).child(uidKarangan).child("like").setValue(1);
                    textViewFav.setText(String.valueOf(vote + 1));
                    Toast.makeText(getApplicationContext(), "Anda Suka Karangan Ini", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karangan_detail);
        listID();
        setTextViewLike();
        setTextViewKarangan();
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

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> list) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
            for (Purchase purchase : list) {
                handlePurchase(purchase);
            }
        }
    }

    private void handlePurchase(final Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            handleConsume(purchase);
        } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
            //so this will be pending, it means that it will take some time.
            //need to inform the user about this
            ShowToast(KaranganDetailActivity.this, "Purchasing will take a few minutes, please wait...");
            handleConsume(purchase);
        } else if (purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE) {
            //so this will be pending, it means that it will take some time.
            //need to inform the user about this
            ShowToast(KaranganDetailActivity.this, "Purchasing will take a few minutes, please wait... Please do contact our support if there any failure. Sorry for the inconvenience.");
            handleConsume(purchase);
        }
    }
}