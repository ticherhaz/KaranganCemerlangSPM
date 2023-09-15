package net.ticherhaz.karangancemerlangspm.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ProductDetails;
import com.google.common.collect.ImmutableList;

import net.ticherhaz.karangancemerlangspm.KaranganDetailActivity;
import net.ticherhaz.karangancemerlangspm.R;

import java.util.List;

public class MyProductDownloadKaranganAdapter extends RecyclerView.Adapter<MyProductDownloadKaranganAdapter.MyViewHolder> {

    private final KaranganDetailActivity karanganDetailActivity;
    private final List<ProductDetails> productDetailsList;
    private final BillingClient billingClient;

    public MyProductDownloadKaranganAdapter(KaranganDetailActivity karanganDetailActivity, List<ProductDetails> productDetailsList, BillingClient billingClient) {
        this.karanganDetailActivity = karanganDetailActivity;
        this.productDetailsList = productDetailsList;
        this.billingClient = billingClient;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_product_download_karangan, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int positionNotUseful) {
        final int position = holder.getAbsoluteAdapterPosition();

        //holder.getTextViewProduct().setText(productDetailsList.get(position).getTitle());
        //holder.getTextViewProduct().setText(productDetailsList.get(position).getPrice());

        final String title = "Muat Turun Karangan Ini";
        holder.getButtonProduct().setText(title);
        holder.getButtonProduct().setOnClickListener(v -> PermissionUtils.INSTANCE.storagePermissions(karanganDetailActivity, new PermissionUtils.IStoragePermission() {
            @Override
            public void onSuccess() {

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
                billingClient.launchBillingFlow(karanganDetailActivity, billingFlowParams);
            }

            @Override
            public void onFailure() {

            }
        }));
    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private Button buttonProduct;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonProduct = itemView.findViewById(R.id.btn_product);
        }

        Button getButtonProduct() {
            return buttonProduct;
        }

        public void setButtonProduct(Button buttonProduct) {
            this.buttonProduct = buttonProduct;
        }
    }
}
