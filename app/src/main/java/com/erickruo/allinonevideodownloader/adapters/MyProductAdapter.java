/*
 * *
 *  * Created by Syed Usama Ahmad on 2/23/23, 12:24 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/22/23, 10:39 PM
 *
 */

package com.erickruo.allinonevideodownloader.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ProductDetails;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.activities.SplashScreen;
import com.erickruo.allinonevideodownloader.interfaces.IRecyclerClickListener;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsMainApp;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import java.util.Arrays;
import java.util.List;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyViewHolder> {

    AppCompatActivity appCompatActivity;
    List<ProductDetails> skuDetailsList;
    BillingClient billingClient;

    public MyProductAdapter(AppCompatActivity appCompatActivity, List<ProductDetails> skuDetailsList, BillingClient billingClient) {
        this.appCompatActivity = appCompatActivity;
        this.skuDetailsList = skuDetailsList;
        this.billingClient = billingClient;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(appCompatActivity.getBaseContext())
                .inflate(R.layout.layout_product_display, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_product_name.setText(skuDetailsList.get(position).getTitle());
        holder.txt_description.setText(skuDetailsList.get(position).getDescription());
        String priceVal = "free";
        try {
            if (skuDetailsList.get(position).getSubscriptionOfferDetails() != null) {
                priceVal = (skuDetailsList.get(position).getSubscriptionOfferDetails().size() > 1) ? skuDetailsList.get(position).getSubscriptionOfferDetails().get(1).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice() : skuDetailsList.get(position).getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice();
            }
            System.out.println("mmm>>>>>>>>>>>1111 " + priceVal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.txt_price.setText((priceVal.toLowerCase().contains("free")) ? "9.50$" : priceVal);

        //Product click
        holder.setListener((view, position1) -> {

            try {


                String offerToken = skuDetailsList.get(position)
                        .getSubscriptionOfferDetails()
                        .get(0)
                        .getOfferToken();


                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(Arrays.asList(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(skuDetailsList.get(position))
                                        .setOfferToken(offerToken)
                                        .build()
                        ))
                        .build();


                int reponse = billingClient.launchBillingFlow(appCompatActivity, billingFlowParams)
                        .getResponseCode();
                switch (reponse) {
                    case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                        appCompatActivity.runOnUiThread(() -> {
                            iUtils.ShowToastError(appCompatActivity,
                                    appCompatActivity.getResources().getString(R.string.billunavaliable) + ""
                            );
                        });
                        break;
                    case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                        appCompatActivity.runOnUiThread(() -> {
                            iUtils.ShowToastError(appCompatActivity,
                                    appCompatActivity.getResources().getString(R.string.developererrer) + ""
                            );
                        });
                        break;
                    case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                        appCompatActivity.runOnUiThread(() -> {
                            iUtils.ShowToastError(appCompatActivity,
                                    appCompatActivity.getResources().getString(R.string.featurenotsupp) + ""
                            );
                        });
                        break;
                    case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                        appCompatActivity.runOnUiThread(() -> {
                            iUtils.ShowToast(appCompatActivity,
                                    appCompatActivity.getResources().getString(R.string.alreadowned) + ""
                            );
                        });
                        new SharedPrefsMainApp(appCompatActivity).setPREFERENCE_inappads("ppp");

                        Intent intent = new Intent(appCompatActivity, SplashScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        appCompatActivity.startActivity(intent);
                        appCompatActivity.finish();

                        break;
                    case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                        appCompatActivity.runOnUiThread(() -> {
                            iUtils.ShowToast(appCompatActivity,
                                    appCompatActivity.getResources().getString(R.string.servicediss) + ""
                            );
                        });
                        break;
                    case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                        appCompatActivity.runOnUiThread(() -> {
                            iUtils.ShowToast(appCompatActivity,
                                    appCompatActivity.getResources().getString(R.string.servicetime) + ""
                            );
                        });
                        break;
                    case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                        appCompatActivity.runOnUiThread(() -> {
                            iUtils.ShowToast(appCompatActivity,
                                    appCompatActivity.getResources().getString(R.string.itemunaval) + ""
                            );
                        });
                        break;
                    default:
                        break;

                }
            } catch (Exception ignored) {
            }
        });

    }

    @Override
    public int getItemCount() {
        try {
            return skuDetailsList.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_product_name;
        TextView txt_price;
        TextView txt_description;
        IRecyclerClickListener listener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            itemView.setOnClickListener(this);
        }

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onClickRec(view, getAdapterPosition());

        }


    }
}
