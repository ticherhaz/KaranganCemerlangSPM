package net.ticherhaz.karangancemerlangspm.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;

import net.ticherhaz.karangancemerlangspm.KaranganDetailActivity;
import net.ticherhaz.karangancemerlangspm.R;

import java.util.List;

public class MyProductDownloadKaranganAdapter extends RecyclerView.Adapter<MyProductDownloadKaranganAdapter.MyViewHolder> {

    private KaranganDetailActivity karanganDetailActivity;
    private List<SkuDetails> skuDetailsList;
    private BillingClient billingClient;

    public MyProductDownloadKaranganAdapter(KaranganDetailActivity karanganDetailActivity, List<SkuDetails> skuDetailsList, BillingClient billingClient) {
        this.karanganDetailActivity = karanganDetailActivity;
        this.skuDetailsList = skuDetailsList;
        this.billingClient = billingClient;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_product_download_karangan, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //holder.getTextViewProduct().setText(skuDetailsList.get(position).getTitle());
        //holder.getTextViewProduct().setText(skuDetailsList.get(position).getPrice());
        final String title = "Muat Turun Karangan Ini";
        holder.getButtonProduct().setText(title);
        holder.getButtonProduct().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList.get(position))
                        .build();
                billingClient.launchBillingFlow(karanganDetailActivity, billingFlowParams);
            }
        });
//        holder.setiProductClickListener(new IProductClickListener() {
//            @Override
//            public void onProductClickListener(View view, int position) {
//                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
//                        .setSkuDetails(skuDetailsList.get(position))
//                        .build();
//                billingClient.launchBillingFlow(karanganDetailActivity, billingFlowParams);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return skuDetailsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Button buttonProduct;
        private IProductClickListener iProductClickListener;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonProduct = itemView.findViewById(R.id.btn_product);
            itemView.setOnClickListener(this);
        }

        public IProductClickListener getiProductClickListener() {
            return iProductClickListener;
        }

        void setiProductClickListener(IProductClickListener iProductClickListener) {
            this.iProductClickListener = iProductClickListener;
        }

        public Button getButtonProduct() {
            return buttonProduct;
        }

        public void setButtonProduct(Button buttonProduct) {
            this.buttonProduct = buttonProduct;
        }

        @Override
        public void onClick(View view) {
            iProductClickListener.onProductClickListener(view, getAdapterPosition());
        }
    }
}
