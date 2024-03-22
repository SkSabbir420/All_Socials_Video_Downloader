/*
 * *
 *  * Created by Syed Usama Ahmad on 3/15/23, 2:27 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/15/23, 2:24 PM
 *
 */

package com.erickruo.allinonevideodownloader.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryPurchasesParams;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.adapters.MyProductAdapter;
import com.erickruo.allinonevideodownloader.databinding.ActivitySubscriptionBinding;
import com.erickruo.allinonevideodownloader.inappbilling.BillingClientSetup;
import com.erickruo.allinonevideodownloader.utils.LocaleHelper;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsMainApp;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import java.util.List;

public class SubscriptionActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    private ActivitySubscriptionBinding binding;

    BillingClient billingClient;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;
    private MyProductAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscriptionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        try {
            init();
            setupBillingClient();


            adapter = new MyProductAdapter(SubscriptionActivity.this, iUtils.SkuDetailsList, billingClient);
            binding.recyclerViewSub.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init() {

        binding.recyclerViewSub.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        binding.backImg.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        });


    }

    private void setupBillingClient() {


        acknowledgePurchaseResponseListener = billingResult -> {
            System.out.println("mypurchase 4 = ");

            binding.textPremium.setVisibility(View.VISIBLE);
            binding.recyclerViewSub.setVisibility(View.GONE);

            if (!isFinishing()) {
                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(SubscriptionActivity.this);
                builder.setTitle(R.string.removeads);
                builder.setMessage(R.string.adsareremoved);

                builder.setPositiveButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();

                });
                builder.show();
            }

            new SharedPrefsMainApp(SubscriptionActivity.this).setPREFERENCE_inappads("ppp");

        };
        billingClient = BillingClientSetup.getInstance(SubscriptionActivity.this, SubscriptionActivity.this);
        billingClient.startConnection(new BillingClientStateListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                try {
                    System.out.println("mypurchase 2 = ");

                    binding.textPremium.setVisibility(View.GONE);
                    binding.recyclerViewSub.setVisibility(View.VISIBLE);


                    //TODO error in
                    runOnUiThread(() -> {

                        //  loadAllSubscribePackage();
                        adapter.notifyDataSetChanged();
                    });


                    billingClient.queryPurchasesAsync(
                            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(),
                            (billingResult1, purchases) -> {


                                System.out.println("mypurchase 6.5 = ");


                                if (purchases.size() > 0) {
                                    iUtils.isSubactive = true;
                                    binding.recyclerViewSub.setVisibility(View.GONE);
                                    for (Purchase purchase : purchases)
                                        handlesAlreadyPurchased(purchase);

                                } else {
                                    iUtils.isSubactive = false;
                                    System.out.println("mypurchase ttt = ");

                                    binding.textPremium.setVisibility(View.GONE);
                                    binding.recyclerViewSub.setVisibility(View.VISIBLE);
                                    // loadAllSubscribePackage();
                                    runOnUiThread(() -> {
                                        adapter.notifyDataSetChanged();
                                    });

                                    System.out.println("mypurchase 4 = " + billingResult1.getResponseCode());
                                    new SharedPrefsMainApp(SubscriptionActivity.this).setPREFERENCE_inappads("nnn");

//                                    System.out.println("mypurchase 9 nnndd= " + purchases.get(0).getSkus());
                                }


                            }
                    );


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

                runOnUiThread(() -> {
                    iUtils.ShowToastError(SubscriptionActivity.this,
                            getResources().getString(R.string.yourasrediss)
                    );
                });
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void handlesAlreadyPurchased(Purchase purchases) {

        System.out.println("mypurchase 9 state= " + purchases.getPurchaseState());
        System.out.println("mypurchase 9 ACK = " + purchases.isAcknowledged());

        if (purchases.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {

            if (!purchases.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchases.getPurchaseToken())
                        .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            } else {

                binding.textPremium.setVisibility(View.VISIBLE);
                binding.textPremium.setText(getResources().getString(R.string.alreadysubbed) + "");
                binding.recyclerViewSub.setVisibility(View.GONE);
                new SharedPrefsMainApp(SubscriptionActivity.this).setPREFERENCE_inappads("ppp");

                System.out.println("mypurchase 9 ppdone 0= ");

            }
        }
    }


    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        try {
            if (list != null) {
                for (Purchase purchase : list)
                    handlesAlreadyPurchased(purchase);
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                runOnUiThread(() -> {
                    iUtils.ShowToast(SubscriptionActivity.this,
                            getResources().getString(R.string.cancekdbilling)
                    );
                });
            } else {

                runOnUiThread(() -> {
                    iUtils.ShowToastError(SubscriptionActivity.this,
                            getResources().getString(R.string.error_occ) + "" + billingResult.getResponseCode()
                    );
                });
            }
        } catch (Exception ignored) {
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
