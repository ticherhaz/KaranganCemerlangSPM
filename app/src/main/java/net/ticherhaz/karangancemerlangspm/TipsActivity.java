package net.ticherhaz.karangancemerlangspm;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.firebase.database.FirebaseDatabase;
import com.zxy.skin.sdk.SkinActivity;

import net.ticherhaz.karangancemerlangspm.model.Donat;
import net.ticherhaz.karangancemerlangspm.util.MyProductAdapter;

import java.util.Arrays;
import java.util.List;

import static net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasa;

public class TipsActivity extends SkinActivity implements PurchasesUpdatedListener {

    private BillingClient billingClient;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        recyclerView = findViewById(R.id.products);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setBillingClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (billingClient.isReady()) {
                    SkuDetailsParams params = SkuDetailsParams.newBuilder()
                            .setSkusList(Arrays.asList("myr_3", "myr_5", "myr_25"))
                            .setType(BillingClient.SkuType.INAPP)
                            .build();
                    billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                MyProductAdapter adapter = new MyProductAdapter(TipsActivity.this, skuDetailsList, billingClient);
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    });
                } else {

                }
            }
        }, 1000);

    }

    private void setBillingClient() {
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(this)
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }

    private void initConsume() {

    }

    private void handlePurchase(final Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user.
            // Acknowledge the purchase if it hasn't already been acknowledged.
            if (!purchase.isAcknowledged()) {
                ConsumeParams consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .setDeveloperPayload(purchase.getDeveloperPayload())
                        .build();
                billingClient.consumeAsync(consumeParams, new ConsumeResponseListener() {
                    @Override
                    public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {

                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            Toast.makeText(TipsActivity.this, "Berjaya memberi tips. Ribuan terima kasih :)", Toast.LENGTH_LONG).show();
                            final String donatUid = FirebaseDatabase.getInstance().getReference().push().getKey();
                            final Donat donat = new Donat(donatUid, purchase.getPackageName(), purchase.getPurchaseToken(), purchase.getSignature(), purchase.getOrderId(), purchase.getDeveloperPayload(), purchase.getOriginalJson(), GetTarikhMasa());
                            //Store info in database
                            if (donatUid != null) {
                                FirebaseDatabase.getInstance().getReference().child("donation").child(donatUid).setValue(donat);
                            }
                        }


                    }
                });


                /*
                 * Use this if you want 1 purchase only.
                 */
                //                AcknowledgePurchaseParams acknowledgePurchaseParams =
//                        AcknowledgePurchaseParams.newBuilder()
//                                .setPurchaseToken(purchase.getPurchaseToken())
//                                .build();


//                billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
//                    @Override
//                    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
//                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//
//                            ConsumeParams consumeParams = ConsumeParams.newBuilder()
//                                    .setPurchaseToken(purchase.getPurchaseToken())
//                                    .setDeveloperPayload(purchase.getDeveloperPayload())
//                                    .build();
//
//
//                            billingClient.consumeAsync(consumeParams, new ConsumeResponseListener() {
//                                @Override
//                                public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
//
//                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                                        Toast.makeText(TipsActivity.this, "Berjaya memberi tips. Ribuan terima kasih :)", Toast.LENGTH_LONG).show();
//                                        final String donatUid = FirebaseDatabase.getInstance().getReference().push().getKey();
//                                        final Donat donat = new Donat(donatUid, purchase.getPackageName(), purchase.getPurchaseToken(), purchase.getSignature(), purchase.getOrderId(), purchase.getDeveloperPayload(), purchase.getOriginalJson(), GetTarikhMasa());
//                                        //Store info in database
//                                        if (donatUid != null) {
//                                            FirebaseDatabase.getInstance().getReference().child("donation").child(donatUid).setValue(donat);
//                                        }
//                                    }
//
//
//                                }
//                            });
//
//                        }
//
//                    }
//                });
            }
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else {
        }
    }
}
