package net.ticherhaz.karangancemerlangspm.version2025.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import net.ticherhaz.karangancemerlangspm.databinding.MyProductBinding
import net.ticherhaz.karangancemerlangspm.version2025.ui.donation.DonationActivity

class MyProductAdapter(
    private val donationActivity: DonationActivity,
    private val productDetailsList: List<ProductDetails>,
    private val billingClient: BillingClient
) : RecyclerView.Adapter<MyProductAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = MyProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = productDetailsList[position]
        holder.binding.tvProduct.text = product.oneTimePurchaseOfferDetails?.formattedPrice ?: "N/A"

        holder.binding.root.setOnClickListener {
            val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(product)
                .build()

            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(productDetailsParams))
                .build()

            billingClient.launchBillingFlow(donationActivity, billingFlowParams)
        }
    }

    override fun getItemCount(): Int = productDetailsList.size

    inner class MyViewHolder(val binding: MyProductBinding) : RecyclerView.ViewHolder(binding.root)
}
