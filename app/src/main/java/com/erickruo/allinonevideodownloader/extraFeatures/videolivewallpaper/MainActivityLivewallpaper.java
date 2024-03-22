/*
 * *
 *  * Created by Syed Usama Ahmad on 3/4/23, 8:29 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/4/23, 8:04 PM
 *
 */

package com.erickruo.allinonevideodownloader.extraFeatures.videolivewallpaper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.erickruo.allinonevideodownloader.BuildConfig;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.databinding.ActivityMainLivevideoBinding;
import com.erickruo.allinonevideodownloader.utils.AdsManager;
import com.erickruo.allinonevideodownloader.utils.Constants;
import com.erickruo.allinonevideodownloader.utils.GlideApp;
import com.erickruo.allinonevideodownloader.utils.LocaleHelper;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsMainApp;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import java.io.File;
import java.io.IOException;


public class MainActivityLivewallpaper extends AppCompatActivity {
    public CinimaWallService cinimaService;

    private String url = null;

    private ActivityMainLivevideoBinding binding;
    private String nn = "nnn";

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityMainLivevideoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        try {


            binding.spinKit.setVisibility(View.GONE);
            cinimaService = new CinimaWallService();
            binding.checkboxSound.setOnClickListener(view13 -> cinimaService.setEnableVideoAudio(MainActivityLivewallpaper.this, binding.checkboxSound.isChecked()));
            binding.checkboxPlayBegin.setOnClickListener(view12 -> cinimaService.setPlayB(MainActivityLivewallpaper.this, binding.checkboxPlayBegin.isChecked()));
            binding.checkboxBattery.setOnClickListener(view1 -> cinimaService.setPlayBatterySaver(MainActivityLivewallpaper.this, binding.checkboxBattery.isChecked()));


            nn = new SharedPrefsMainApp(MainActivityLivewallpaper.this).getPREFERENCE_inappads();

            if (Constants.show_Ads && !BuildConfig.ISPRO) {
                if (nn.equals("nnn")) {

                    if (AdsManager.status_AdmobBanner) {

                        MobileAds.initialize(
                                getApplicationContext(),
                                initializationStatus -> {
                                    AdsManager.loadBannerAdsAdapter(MainActivityLivewallpaper.this, binding.bannerContainer);
                                });

                    } else {
                        binding.bannerContainer.setVisibility(View.GONE);

                    }

                    AdsManager.loadInterstitialAd(MainActivityLivewallpaper.this);

                } else {

                    binding.bannerContainer.setVisibility(View.GONE);
                }
            } else {

                binding.bannerContainer.setVisibility(View.GONE);
            }


            if (!((PowerManager) getSystemService(POWER_SERVICE)).isIgnoringBatteryOptimizations(getPackageName())
            ) {
                if (!isFinishing()) {
                new AlertDialog.Builder(this).setTitle(R.string.removebatteryris)
                        .setMessage(
                                R.string.pleasesetbattery
                        )
                        .setCancelable(false)
                        .setPositiveButton("ALLOW", (dialog, which) -> {

                            Intent intent1 = new Intent();
                            intent1.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            String sb = "package:" + getPackageName();
                            intent1.setData(Uri.parse(sb));
                            startActivity(intent1);

                        })
                        .show();}
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        try {
            if (i == 53213 && i2 == -1) {

                this.url = getPath(intent.getData());

                binding.spinKit.setVisibility(View.VISIBLE);
                binding.videoSelectButton.setVisibility(View.GONE);
                GlideApp.with(this).asBitmap().addListener(new RequestListener<>() {
                    public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
                        binding.spinKit.setVisibility(View.GONE);
                        binding.videoSelectButton.setVisibility(View.VISIBLE);
                        return false;
                    }

                    public boolean onResourceReady(Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
                        binding.spinKit.setVisibility(View.GONE);
                        binding.videoSelectButton.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).load(Uri.fromFile(new File(this.url))).placeholder(R.drawable.ic_appicon_pro).into(binding.imgThumb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath(Uri uri) {
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else return null;
        } catch (Exception e) {
            return null;
        }
    }


    private void showAdmobAds() {
        if (Constants.show_Ads && !BuildConfig.ISPRO) {
            if (nn.equals("nnn")) {

                AdsManager.showAdmobInterstitialAd(MainActivityLivewallpaper.this, new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        AdsManager.loadInterstitialAd(MainActivityLivewallpaper.this);

                    }
                });

            }
        }
    }

    public void set_up_video_clicked(View view) {
        try {
            showAdmobAds();
            if (this.url == null) {


                runOnUiThread(() -> {
                    iUtils.ShowToastError(this, getString(R.string.please_select_video)
                    );
                });


                return;
            }
            cinimaService.setEnableVideoAudio(this, binding.checkboxSound.isChecked());
            cinimaService.setPlayB(this, binding.checkboxPlayBegin.isChecked());
            cinimaService.setPlayBatterySaver(this, binding.checkboxBattery.isChecked());
            cinimaService.setVidSource(this, this.url);
            if (cinimaService.getVideoSource(this) == null) {


                runOnUiThread(() -> {
                    iUtils.ShowToastError(this, getString(R.string.error_emty_video)
                    );
                });

                return;
            }
            try {
                clearWallpaper();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
            intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", new ComponentName(this, CinimaWallService.class));
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void video_on_clicked(View view) {
        try {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 53213);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdsManager.destroyAdview();
    }
}
