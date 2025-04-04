package net.ticherhaz.karangancemerlangspm;

import static net.ticherhaz.karangancemerlangspm.utils.Others.ShowToast;
import static net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasa;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.google.common.collect.ImmutableList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.ticherhaz.karangancemerlangspm.model.Donat2;
import net.ticherhaz.karangancemerlangspm.utils.MyProductAdapter;

import java.util.List;

public class TipsActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    private BillingClient billingClient;
    private RecyclerView recyclerView;
    private ProgressBar progressBarMain;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        progressBarMain = findViewById(R.id.pb_main);
        recyclerView = findViewById(R.id.recycler_view_tips);
        recyclerView.setHasFixedSize(true);

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        setBillingClient();
    }

    private void setBillingClient() {
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(this)
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                //If the billing ready to display
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    if (billingClient.isReady()) {
                        //and then hide the progress bar once it finish loading the app billing
                        //so productId and title kena ikut urutan alphabet

                        final List<QueryProductDetailsParams.Product> productList = ImmutableList.of(
                                QueryProductDetailsParams.Product.newBuilder()
                                        .setProductId("sehelai_ringgit")
                                        .setProductType(BillingClient.ProductType.INAPP)
                                        .build(),
                                QueryProductDetailsParams.Product.newBuilder()
                                        .setProductId("sejambak_ringgit")
                                        .setProductType(BillingClient.ProductType.INAPP)
                                        .build(),
                                QueryProductDetailsParams.Product.newBuilder()
                                        .setProductId("sekantung_ringgit")
                                        .setProductType(BillingClient.ProductType.INAPP)
                                        .build(),
                                QueryProductDetailsParams.Product.newBuilder()
                                        .setProductId("setimbun_ringgit")
                                        .setProductType(BillingClient.ProductType.INAPP)
                                        .build(),
                                QueryProductDetailsParams.Product.newBuilder()
                                        .setProductId("seulas_ringgit")
                                        .setProductType(BillingClient.ProductType.INAPP)
                                        .build());

                        final QueryProductDetailsParams queryProductDetailsParams =
                                QueryProductDetailsParams.newBuilder()
                                        .setProductList(productList)
                                        .build();
                        billingClient.queryProductDetailsAsync(queryProductDetailsParams, (billingResult2, productDetailsList) -> {

                                    final MyProductAdapter adapter = new MyProductAdapter(TipsActivity.this, productDetailsList, billingClient);
                                    runOnUiThread(() -> {
                                        recyclerView.setAdapter(adapter);
                                        progressBarMain.setVisibility(View.GONE);
                                    });

                                }
                        );


                    } else {
                        runOnUiThread(() -> {
                            progressBarMain.setVisibility(View.GONE);
                            ShowToast(TipsActivity.this, "Please try again later.");
                        });
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                runOnUiThread(() -> {
                    progressBarMain.setVisibility(View.GONE);
                    ShowToast(TipsActivity.this, getString(R.string.internet_connection));
                });
            }
        });
    }

    private void handleConsume(final Purchase purchase) {
        // Grant entitlement to the user.
        // Acknowledge the purchase if it hasn't already been acknowledged.
        if (!purchase.isAcknowledged()) {
            // ShowProgressDialog(TipsActivity.this);
            /*
             * Use this if you want to purchase many times
             */
            ConsumeParams consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.getPurchaseToken())
                    .build();
            billingClient.consumeAsync(consumeParams, (billingResult, s) -> {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //Success purchased
                    final String userPurchasedCoinsMoreUid = FirebaseDatabase.getInstance().getReference().push().getKey();
                    final String onPurchasedDateUTC = GetTarikhMasa();
                    final long onPurchasedDate = System.currentTimeMillis();

                    final String orderUid = purchase.getOrderId();
                    final String skutName = purchase.getSkus().get(0);
                    final String signature = purchase.getSignature();
                    final String originalJson = purchase.getOriginalJson();
                    final int purchaseState = purchase.getPurchaseState();
                    final String developerPayload = purchase.getDeveloperPayload();

                    String duitNakDisplay = null;
                    switch (purchase.getSkus().get(0)) {
                        case "sehelai_ringgit":
                            duitNakDisplay = "RM1.00";
                            break;
                        case "sejambak_ringgit":
                            duitNakDisplay = "RM3.00";
                            break;
                        case "sekantung_ringgit":
                            duitNakDisplay = "RM5.00";
                            break;
                        case "setimbun_ringgit":
                            duitNakDisplay = "RM10.00";
                            break;
                        case "seulas_ringgit":
                            duitNakDisplay = "RM1500.00";
                            break;
                    }


                    final String userUid = databaseReference.push().getKey();

                    final Donat2 upurcm = new Donat2(userPurchasedCoinsMoreUid, userUid, orderUid, skutName, signature, originalJson, developerPayload, onPurchasedDateUTC, purchaseState, onPurchasedDate);
                    //Store info in database
                    assert userUid != null;
                    final String finalDuitNakDisplay = duitNakDisplay;
                    databaseReference.child("donation2").child(userUid).setValue(upurcm).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            runOnUiThread(() -> {
                                final String description = "Menyumbang " + finalDuitNakDisplay + ". Terima kasih atas sumbangan anda :)";
                                ShowToast(TipsActivity.this, description);
                            });
                        }
                    });
                } else {
                    Log.i("???", "handleConsume failed: " + billingResult.getResponseCode());
                }
            });
        }
    }

    private void handlePurchase(final Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            handleConsume(purchase);
        } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
            //so this will be pending, it means that it will take some time.
            //need to inform the user about this
            ShowToast(TipsActivity.this, "Purchasing will take a few minutes, please wait...");
            handleConsume(purchase);
        } else if (purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE) {
            //so this will be pending, it means that it will take some time.
            //need to inform the user about this
            ShowToast(TipsActivity.this, "Purchasing will take a few minutes, please wait... Please do contact our support if there any failure. Sorry for the inconvenience.");
            handleConsume(purchase);
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        }
    }
}
