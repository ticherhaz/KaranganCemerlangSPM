package net.ticherhaz.karangancemerlangspm.Util;

public class MyTipsAdapter {
//public class MyTipsAdapter extends RecyclerView.Adapter<MyTipsAdapter.MyViewHolder> {

//    private MainActivity mainActivity;
//    private List<SkuDetails> skuDetailsList;
//    private BillingClient billingClient;
//
//    public MyTipsAdapter(MainActivity mainActivity, List<SkuDetails> skuDetailsList, BillingClient billingClient) {
//        this.mainActivity = mainActivity;
//        this.skuDetailsList = skuDetailsList;
//        this.billingClient = billingClient;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_product, parent, false);
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.getTextViewMyProduct().setText(skuDetailsList.get(position).getTitle());
//
//        //Product click
//        holder.setiProductClickListener(new IProductClickListener() {
//            @Override
//            public void onProductClickListener(View view, int position) {
//                //Launch billing flow
//                BillingFlowParams billingFlowParams = BillingFlowParams
//                        .newBuilder()
//                        .setSkuDetails(skuDetailsList.get(position))
//                        .build();
//
//                billingClient.launchBillingFlow(mainActivity, billingFlowParams);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return skuDetailsList.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        IProductClickListener iProductClickListener;
//        private TextView textViewMyProduct;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            textViewMyProduct = itemView.findViewById(R.id.text_view_my_product);
//            itemView.setOnClickListener(this);
//
//        }
//
//        public void setiProductClickListener(IProductClickListener iProductClickListener) {
//            this.iProductClickListener = iProductClickListener;
//        }
//
//        public TextView getTextViewMyProduct() {
//            return textViewMyProduct;
//        }
//
//        public void setTextViewMyProduct(TextView textViewMyProduct) {
//            this.textViewMyProduct = textViewMyProduct;
//        }
//
//        @Override
//        public void onClick(View view) {
//            iProductClickListener.onProductClickListener(view, getAdapterPosition());
//        }
//    }
}
