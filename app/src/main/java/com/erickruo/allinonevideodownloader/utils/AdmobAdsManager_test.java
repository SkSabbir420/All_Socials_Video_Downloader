/*
 * *
 *  * Created by Syed Usama Ahmad on 2/27/23, 1:22 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/26/23, 11:10 PM
 *
 */

package com.erickruo.allinonevideodownloader.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.erickruo.allinonevideodownloader.R;

public class AdmobAdsManager_test {


    public static int adCounter = 1;


    public static AdView gadView;

    public static void loadBannerAd(Context context, LinearLayout adContainer) {
        gadView = new AdView(context);
        gadView.setAdUnitId(context.getString(R.string.AdmobBanner));
        adContainer.addView(gadView);
        loadBanner(context);
    }

    static void loadBanner(Context context) {
        AdRequest adRequest =
                new AdRequest.Builder().build();

        AdSize adSize = getAdSize((Activity) context);
        gadView.setAdSize(adSize);
        gadView.loadAd(adRequest);
    }

    static AdSize getAdSize(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    public static void adptiveBannerAd(Context context, LinearLayout adContainer) {
        AdView adView = new AdView(context);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdSize(AdSize.LARGE_BANNER);
        adView.setAdUnitId(context.getString(R.string.AdmobBanner));
        adView.loadAd(adRequest);
        adContainer.addView(adView);
    }

    public static InterstitialAd mInterstitialAd;
    public static InterstitialAd mInterstitialAdStart;

    public static void LoadFullAd(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, context.getString(R.string.AdmobInterstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }
        });
    }


    public static void ShowFullAd(final Activity context, final Intent intent, final int requstCode) {

        if (mInterstitialAd != null) {
            mInterstitialAd.show((Activity) context);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    LoadFullAd(context);
                    startActivity(context, intent, requstCode);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    LoadFullAd(context);
                    startActivity(context, intent, requstCode);

                }


                @Override
                public void onAdShowedFullScreenContent() {

                    mInterstitialAd = null;

                }
            });
        }


    }


    public static void loadInterAdStart(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, context.getString(R.string.AdmobInterstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAdStart = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAdStart = null;
            }
        });
    }

    public static void showInterAdStart(final Activity context, final Intent intent, final int requstCode) {
        if (mInterstitialAdStart != null) {
            mInterstitialAdStart.show((Activity) context);
            mInterstitialAdStart.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    loadInterAdStart(context);
                    startActivity(context, intent, requstCode);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    loadInterAdStart(context);
                    startActivity(context, intent, requstCode);

                }


                @Override
                public void onAdShowedFullScreenContent() {
                    mInterstitialAdStart = null;

                }
            });


        } else {
            startActivity(context, intent, requstCode);

        }


    }


    static void startActivity(Activity context, Intent intent, int requestCode) {
        if (intent != null) {
            context.startActivityForResult(intent, requestCode);
        }
    }


}

