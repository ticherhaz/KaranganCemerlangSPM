package net.ticherhaz.karangancemerlangspm.util;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

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
                Dexter.withContext(karanganDetailActivity)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                // permission is granted, open the camera
                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetailsList.get(position))
                                        .build();
                                billingClient.launchBillingFlow(karanganDetailActivity, billingFlowParams);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                // check for permanent denial of permission
                                if (response.isPermanentlyDenied()) {
                                    // navigate user to app settings
                                    AlertDialog alertDialog = new AlertDialog.Builder(karanganDetailActivity)
                                            .setTitle("Info")
                                            .setMessage("You need to enable write storage at app settings")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    Uri uri = Uri.fromParts("package", karanganDetailActivity.getPackageName(), null);
                                                    intent.setData(uri);
                                                    karanganDetailActivity.startActivity(intent);
                                                }
                                            })
                                            .create();
                                    alertDialog.show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
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
