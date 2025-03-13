package net.ticherhaz.karangancemerlangspm.version2025.ui.donation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.ticherhaz.karangancemerlangspm.R
import net.ticherhaz.karangancemerlangspm.databinding.ActivityDonationBinding
import net.ticherhaz.karangancemerlangspm.model.Donat2
import net.ticherhaz.karangancemerlangspm.utils.Others.ShowToast
import net.ticherhaz.karangancemerlangspm.version2025.adapter.MyProductAdapter
import net.ticherhaz.tarikhmasa.TarikhMasa.GetTarikhMasa

class DonationActivity : AppCompatActivity(), PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient
    private lateinit var binding: ActivityDonationBinding
    private val firestore = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initWindowInsets()

        binding.recyclerViewTips.setHasFixedSize(true)
        setBillingClient()
    }

    private fun setBillingClient() {
        val pendingPurchasesParams = PendingPurchasesParams.newBuilder()
            .enableOneTimeProducts()
            .build()

        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases(pendingPurchasesParams)
            .setListener(this)
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    fetchProductDetails()
                } else {
                    ShowToast(
                        this@DonationActivity,
                        "Billing setup failed. Please try again later."
                    )
                }
            }

            override fun onBillingServiceDisconnected() {
                ShowToast(this@DonationActivity, getString(R.string.internet_connection))
            }
        })
    }

    private fun fetchProductDetails() {
        if (!billingClient.isReady) return

        val productList = listOf(
            "sehelai_ringgit",
            "sejambak_ringgit",
            "sekantung_ringgit",
            "setimbun_ringgit",
            "seulas_ringgit"
        )
            .map {
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(it)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            }

        val params = QueryProductDetailsParams.newBuilder().setProductList(productList).build()

        billingClient.queryProductDetailsAsync(params) { _, productDetailsList ->
            runOnUiThread {
                binding.recyclerViewTips.adapter =
                    MyProductAdapter(this@DonationActivity, productDetailsList, billingClient)
                binding.pbMain.visibility = View.GONE
            }
        }
    }

    private fun handleConsume(purchase: Purchase) {
        if (!purchase.isAcknowledged) {
            val consumeParams =
                ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
            billingClient.consumeAsync(consumeParams) { billingResult, _ ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    savePurchaseToFirestore(purchase)
                } else {
                    Log.e("Billing", "Consume failed: ${billingResult.responseCode}")
                }
            }
        }
    }

    private fun savePurchaseToFirestore(purchase: Purchase) {
        lifecycleScope.launch(Dispatchers.IO) {

            val donation = Donat2(
                firestore.reference.push().push().key,
                firestore.reference.push().key,
                purchase.orderId,
                purchase.products.firstOrNull() ?: "",
                purchase.signature,
                purchase.originalJson,
                purchase.developerPayload,
                GetTarikhMasa(),
                purchase.purchaseState,
                System.currentTimeMillis()
            )

            firestore.reference.child("donation2").child(donation.userUid)
                .child(donation.userPurchasedCoinsMoreUid).setValue(donation)
                .addOnSuccessListener {
                    runOnUiThread {
                        val description =
                            "Menyumbang ${getPriceFromSku(donation.skuName)}. Terima kasih!"
                        ShowToast(this@DonationActivity, description)
                    }
                }
        }
    }

    private fun getPriceFromSku(sku: String): String = when (sku) {
        "sehelai_ringgit" -> "RM1.00"
        "sejambak_ringgit" -> "RM3.00"
        "sekantung_ringgit" -> "RM5.00"
        "setimbun_ringgit" -> "RM10.00"
        "seulas_ringgit" -> "RM1500.00"
        else -> "Unknown"
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !purchases.isNullOrEmpty()) {
            purchases.forEach { handleConsume(it) }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            ShowToast(this, "Purchase cancelled.")
        }
    }

    private fun initWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
