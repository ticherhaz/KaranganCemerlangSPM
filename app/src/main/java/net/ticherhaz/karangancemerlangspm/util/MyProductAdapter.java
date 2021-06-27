package net.ticherhaz.karangancemerlangspm.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;

import net.ticherhaz.karangancemerlangspm.R;
import net.ticherhaz.karangancemerlangspm.TipsActivity;

import java.util.List;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyViewHolder> {

    private final TipsActivity tipsActivity;
    private final List<SkuDetails> skuDetailsList;
    private final BillingClient billingClient;

    public MyProductAdapter(TipsActivity tipsActivity, List<SkuDetails> skuDetailsList, BillingClient billingClient) {
        this.tipsActivity = tipsActivity;
        this.skuDetailsList = skuDetailsList;
        this.billingClient = billingClient;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_product, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //holder.getTextViewProduct().setText(skuDetailsList.get(position).getTitle());
        holder.getTextViewProduct().setText(skuDetailsList.get(position).getPrice());

        holder.setiProductClickListener((view, position1) -> {
            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetailsList.get(position1))
                    .build();
            billingClient.launchBillingFlow(tipsActivity, billingFlowParams);
        });
    }

    @Override
    public int getItemCount() {
        return skuDetailsList.size();
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
