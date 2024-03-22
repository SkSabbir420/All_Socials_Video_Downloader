/*
 * *
 *  * Created by Syed Usama Ahmad on 3/4/23, 8:29 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/4/23, 8:04 PM
 *
 */

package com.erickruo.allinonevideodownloader.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.ads.MobileAds;
import com.erickruo.allinonevideodownloader.BuildConfig;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.databinding.ActivityGalleryBinding;
import com.erickruo.allinonevideodownloader.fragments.GalleryAudiosFragment;
import com.erickruo.allinonevideodownloader.fragments.GalleryImagesFragment;
import com.erickruo.allinonevideodownloader.fragments.GalleryStatusSaver;
import com.erickruo.allinonevideodownloader.fragments.GalleryVideosFragment;
import com.erickruo.allinonevideodownloader.utils.AdsManager;
import com.erickruo.allinonevideodownloader.utils.Constants;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsMainApp;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import java.util.Objects;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class GalleryActivity extends AppCompatActivity {

    private ActivityGalleryBinding binding;
    private boolean isChecked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGalleryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            final MyAdapter adapter = new MyAdapter(getSupportFragmentManager(), getLifecycle(), 4);
            binding.viewpagergallery.setAdapter(adapter);


            String nn = new SharedPrefsMainApp(GalleryActivity.this).getPREFERENCE_inappads();
            boolean istutshownlong = new SharedPrefsMainApp(GalleryActivity.this).getPREFERENCE_istutshownlong();
            if (!istutshownlong) {
                if (!isFinishing()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GalleryActivity.this);
                    builder.setTitle(R.string.newop);
                    builder.setMessage(R.string.singleclickandlongclick);
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.close, (dialog, which) -> {
                        new SharedPrefsMainApp(GalleryActivity.this).setPREFERENCE_istutshownlong(true);

                    });
                    builder.show();
                }
            }


            if (Constants.show_Ads && !BuildConfig.ISPRO) {
                if (nn.equals("nnn") && AdsManager.status_AdmobBanner) {
                    MobileAds.initialize(
                            getApplicationContext(),
                            initializationStatus -> {
                                AdsManager.loadBannerAd(GalleryActivity.this, binding.bannerContainer);
                            });
                } else {
                    binding.bannerContainer.setVisibility(View.GONE);
                }
            } else {
                binding.bannerContainer.setVisibility(View.GONE);
            }

            if (Constants.isNonPlayStoreApp) {
                binding.bottomNavBargallery.setOnItemSelectedListener((OnItemSelectedListener) i -> {
                    binding.viewpagergallery.setCurrentItem(i);
                    return false;
                });


                binding.viewpagergallery.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        binding.bottomNavBargallery.setItemActiveIndex(position);

                    }
                });


            }else {
                binding.bottomNavBargallery.setVisibility(View.GONE);
                binding.bottomNavBargalleryNonPLay.setVisibility(View.VISIBLE);
                binding.bottomNavBargalleryNonPLay.setOnItemSelectedListener((OnItemSelectedListener) i -> {
                    binding.viewpagergallery.setCurrentItem(i);
                    return false;
                });


                binding.viewpagergallery.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        binding.bottomNavBargalleryNonPLay.setItemActiveIndex(position);

                    }
                });
            }
            binding.viewpagergallery.setOffscreenPageLimit(2);

        } catch (Throwable e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gallery_context_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.enablebio);
        SwitchCompat mySwitch = (SwitchCompat) menuItem.getActionView();


        SharedPreferences prefs = getSharedPreferences("whatsapp_pref", MODE_PRIVATE);
        boolean lang = prefs.getBoolean("isBio", false);


        mySwitch.setChecked(lang);

        mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GalleryActivity.this.isChecked = mySwitch.isChecked();
            mySwitch.setChecked(isChecked);


            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isBio", GalleryActivity.this.isChecked);
            editor.apply();


            runOnUiThread(() -> {
                iUtils.ShowToast(GalleryActivity.this,
                        "Lock Enabled " + GalleryActivity.this.isChecked
                );
            });

        });
        return true;

    }


    private class MyAdapter extends FragmentStateAdapter {
        int totalTabs;

        public MyAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int totalTabs) {
            super(fragmentManager, lifecycle);
            this.totalTabs = totalTabs;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (Constants.isNonPlayStoreApp) {

                switch (position) {
                    case 1:
                        return new GalleryStatusSaver();
                    case 2:
                        return new GalleryImagesFragment();
                    case 3:
                        return new GalleryAudiosFragment();
                    default:
                        return new GalleryVideosFragment();
                }
            }else {
                switch (position) {

                    case 1:
                        return new GalleryImagesFragment();
                    case 2:
                        return new GalleryAudiosFragment();
                    default:
                        return new GalleryVideosFragment();
                }
            }
        }

        @Override
        public int getItemCount() {
            // Hardcoded, use lists
            return totalTabs;
        }
    }


}
