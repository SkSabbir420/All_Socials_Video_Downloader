package com.erickruo.allinonevideodownloader.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.common.collect.ImmutableList;
import com.erickruo.allinonevideodownloader.BuildConfig;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.utils.AdsManager;
import com.erickruo.allinonevideodownloader.utils.Constants;
import com.erickruo.allinonevideodownloader.utils.PrefsCoins;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SuppressLint("SetTextI18n")
public class CoinsConsumableItemsActivity extends AppCompatActivity {

    BillingClient billingClient;
    TextView clicks;
    Button btn_100;
    Button btn_500;
    Button btn_intAd;
    Button btn_videoAd;
    PrefsCoins prefs;
    List<ProductDetails> productDetailsList;
    Activity activity;
    String TAG = "TestInApp";
    Handler handler;
    ProgressBar progress_circular;
    List<Integer> coins;
    private static final String FORMAT = "%02d:%02d:%02d";

    int seconds, minutes;

    //change this as to change the time for ads timeout
    private final long video_ad_time = 600000;
    private final long int_ad_time = 300000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_coins);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Get Coins");
        try {
            initViews();
            activity = this;

            AdsManager.loadVideoAdAdmob(CoinsConsumableItemsActivity.this, new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();


                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    iUtils.ShowToastError(CoinsConsumableItemsActivity.this, getString(R.string.videonotavaliabl));

                }
            });

            AdsManager.loadInterstitialAd(CoinsConsumableItemsActivity.this);

            billingClient = BillingClient.newBuilder(this)
                    .enablePendingPurchases()
                    .setListener(
                            (billingResult, list) -> {
                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                                    for (Purchase purchase : list) {
                                        verifyPurchase(purchase);
                                    }
                                }
                            }
                    ).build();

            //start the connection after initializing the billing client
            connectGooglePlayBilling();

            btn_100.setOnClickListener(v -> {
                //we are opening product at index zero since we only have one product
                launchPurchaseFlow(productDetailsList.get(0));
            });

            btn_500.setOnClickListener(v -> {
                //we are opening product at index zero since we only have one product
                launchPurchaseFlow(productDetailsList.get(1));
            });

            btn_intAd.setOnClickListener(v -> AdsManager.showAdmobInterstitialAd(CoinsConsumableItemsActivity.this, new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    prefs.setInt("coins", (coins.get(2)) + prefs.getInt("coins", 0));
                    clicks.setText(getString(R.string.available_coins_00) + prefs.getInt("coins", 0));

                    startCountDown(int_ad_time);
                    prefs.setLong("adtime", System.currentTimeMillis() + int_ad_time);

                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    iUtils.ShowToastError(CoinsConsumableItemsActivity.this, getString(R.string.videonotavaliabl));

                }
            }));

            btn_videoAd.setOnClickListener(v -> {

                showVideoAd();
            });

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    void startCountDown(long milisecinfurure) {
        btn_videoAd.setEnabled(false);
        btn_intAd.setEnabled(false);
        new CountDownTimer(milisecinfurure, 1000) {

            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {

                String formated_time = "" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                btn_intAd.setText(formated_time);
                btn_videoAd.setText(formated_time);
            }

            public void onFinish() {
                btn_intAd.setText(getString(R.string.watch_interstitial_ad_5_coins));
                btn_intAd.setEnabled(true);


                btn_videoAd.setText(getString(R.string.watch_video_ad_15_coins));
                btn_videoAd.setEnabled(true);
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {

        clicks = findViewById(R.id.clicks);
        btn_100 = findViewById(R.id.btn_100);
        btn_500 = findViewById(R.id.btn_500);
        btn_videoAd = findViewById(R.id.btn_videoAd);
        btn_intAd = findViewById(R.id.btn_intAd);
        progress_circular = findViewById(R.id.progress_circular);

        prefs = new PrefsCoins(this);
        handler = new Handler();

        coins = new ArrayList<>();
        productDetailsList = new ArrayList<>();

//Change these to add more coins
        coins.add(100);
        coins.add(500);
        coins.add(5);
        coins.add(15);

        clicks.setText(getString(R.string.available_coins_00) + prefs.getInt("coins", 0));

        System.out.println("network timevvv " + prefs.getLong("adtime", System.currentTimeMillis()));
        System.out.println("network timecur " + System.currentTimeMillis());

        if (System.currentTimeMillis() < prefs.getLong("adtime", System.currentTimeMillis())) {
            long newval = prefs.getLong("adtime", System.currentTimeMillis()) - System.currentTimeMillis();
            System.out.println("network timennn " + newval);

            if (newval > 0) {
                startCountDown(newval);
            }
        }


    }


    private void showVideoAd() {
        if (Constants.show_Ads && !BuildConfig.ISPRO) {

            AdsManager.showVideoAdAdmob(CoinsConsumableItemsActivity.this, rewardItem -> {
                prefs.setInt("coins", (coins.get(3)) + prefs.getInt("coins", 0));
                clicks.setText(getString(R.string.available_coins_00) + prefs.getInt("coins", 0));

                startCountDown(video_ad_time);
                prefs.setLong("adtime", System.currentTimeMillis() + video_ad_time);
            });
        }
    }

    void connectGooglePlayBilling() {

        Log.d(TAG, "connectGooglePlayBilling ");

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                connectGooglePlayBilling();
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    showProducts();
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    void showProducts() {

        Log.d(TAG, "showProducts ");

        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(
                //Product 1
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(getString(R.string.coins_100))
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                , QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(getString(R.string.coins_500))
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
        );

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        billingClient.queryProductDetailsAsync(params, (billingResult, list) -> {
            //Clear the list
            productDetailsList.clear();

//            Log.d(TAG, "Size " + list.size());
            try {
                if (list.size() > 0) {
                    //Handler to delay by two seconds to wait for google play to return the list of products.
                    handler.postDelayed(() -> {
                        //Adding new productList, returned from google play
                        productDetailsList.addAll(list);


                        ProductDetails productDetails = list.get(0);
                        String price = productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice();
                        String productName = productDetails.getName();
                        btn_100.setText(price + "  -  " + productName);
                        btn_100.setVisibility(View.VISIBLE);

                        ProductDetails productDetails1 = list.get(1);
                        String price1 = productDetails1.getOneTimePurchaseOfferDetails().getFormattedPrice();
                        String productName1 = productDetails1.getName();
                        btn_500.setText(price1 + "  -  " + productName1);
                        btn_500.setVisibility(View.VISIBLE);

                        progress_circular.setVisibility(View.INVISIBLE);

                    }, 2000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    void launchPurchaseFlow(ProductDetails productDetails) {
        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                ImmutableList.of(BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .build());
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();
        billingClient.launchBillingFlow(activity, billingFlowParams);
    }

    void verifyPurchase(Purchase purchase) {
        ConsumeParams consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
        ConsumeResponseListener listener = (billingResult, s) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                giveUserCoins(purchase);
            }
        };
        billingClient.consumeAsync(consumeParams, listener);
    }

    protected void onResume() {
        super.onResume();
        clicks.setText(getString(R.string.available_coins_00) + prefs.getInt("coins", 0));

        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(),
                (billingResult, list) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : list) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                                verifyPurchase(purchase);
                            }
                        }
                    }
                }
        );
    }

    @SuppressLint("SetTextI18n")
    void giveUserCoins(Purchase purchase) {
        Log.d(TAG, "SizeCOINS_QUA  " + purchase.getQuantity());
        Log.d(TAG, "SizeCOINS_QUA  " + purchase.getSkus().get(0));
        Log.d(TAG, "SizeCOINS  " + (coins.get(0) * purchase.getQuantity()) + prefs.getInt("coins", 0));

        if (purchase.getSkus().get(0).equals(getString(R.string.coins_100))) {
            //set coins
            prefs.setInt("coins", (coins.get(0) * purchase.getQuantity()) + prefs.getInt("coins", 0));
            //Update UI
            clicks.setText(getString(R.string.available_coins_00) + prefs.getInt("coins", 0));
        } else if (purchase.getSkus().get(0).equals(getString(R.string.coins_500))) {

            //set coins
            prefs.setInt("coins", (coins.get(1) * purchase.getQuantity()) + prefs.getInt("coins", 0));
            //Update UI
            clicks.setText(getString(R.string.available_coins_00) + prefs.getInt("coins", 0));
        }
    }


}