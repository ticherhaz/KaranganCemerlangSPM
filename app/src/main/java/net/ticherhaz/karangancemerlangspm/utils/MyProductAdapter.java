package net.ticherhaz.karangancemerlangspm.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ProductDetails;
import com.google.common.collect.ImmutableList;

import net.ticherhaz.karangancemerlangspm.R;
import net.ticherhaz.karangancemerlangspm.TipsActivity;

import java.util.List;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyViewHolder> {

    private final TipsActivity tipsActivity;
    private final List<ProductDetails> productDetailsList;
    private final BillingClient billingClient;

    public MyProductAdapter(TipsActivity tipsActivity, List<ProductDetails> productDetailsList, BillingClient billingClient) {
        this.tipsActivity = tipsActivity;
        this.productDetailsList = productDetailsList;
        this.billingClient = billingClient;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_product, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int positionNotUseful) {
        final int position = holder.getAbsoluteAdapterPosition();

        //holder.getTextViewProduct().setText(skuDetailsList.get(position).getTitle());
        holder.getTextViewProduct().setText(productDetailsList.get(position).getOneTimePurchaseOfferDetails().getFormattedPrice());


        holder.setiProductClickListener((view, position1) -> {

            /*BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetailsList.get(position1))
                    .build();*/

            final ImmutableList productDetailsParamsList =
                    ImmutableList.of(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                    // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                    .setProductDetails(productDetailsList.get(position))
                                    // to get an offer token, call ProductDetails.getSubscriptionOfferDetails()
                                    // for a list of offers that are available to the user
                                    //.setOfferToken(offerToken)  //for subscribe
                                    .build()
                    );


            // permission is granted, open the camera
            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build();

            billingClient.launchBillingFlow(tipsActivity, billingFlowParams);
        });
    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textViewProduct;
        private IProductClickListener iProductClickListener;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProduct = itemView.findViewById(R.id.tv_product);
            itemView.setOnClickListener(this);
        }

        public IProductClickListener getiProductClickListener() {
            return iProductClickListener;
        }

        void setiProductClickListener(IProductClickListener iProductClickListener) {
            this.iProductClickListener = iProductClickListener;
        }

        TextView getTextViewProduct() {
            return textViewProduct;
        }

        public void setTextViewProduct(TextView textViewProduct) {
            this.textViewProduct = textViewProduct;
        }

        @Override
        public void onClick(View view) {
            iProductClickListener.onProductClickListener(view, getAdapterPosition());
        }
    }
}
