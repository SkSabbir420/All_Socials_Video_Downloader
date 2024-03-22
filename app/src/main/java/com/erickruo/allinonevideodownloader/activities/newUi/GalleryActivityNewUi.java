/*
 * *
 *  * Created by Syed Usama Ahmad on 3/4/23, 8:29 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/4/23, 8:04 PM
 *
 */

package com.erickruo.allinonevideodownloader.activities.newUi;

import android.content.Context;
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
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.erickruo.allinonevideodownloader.BuildConfig;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.fragments.GalleryAudiosFragment;
import com.erickruo.allinonevideodownloader.fragments.GalleryImagesFragment;
import com.erickruo.allinonevideodownloader.fragments.GalleryStatusSaver;
import com.erickruo.allinonevideodownloader.fragments.GalleryVideosFragment;
import com.erickruo.allinonevideodownloader.utils.AdsManager;
import com.erickruo.allinonevideodownloader.utils.Constants;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsMainApp;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GalleryActivityNewUi extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    private boolean isChecked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_newui);


        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabsgallery);
        viewPager = (ViewPager) findViewById(R.id.viewpagergallery);


        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.gallery_fragment_statussaver)).setIcon(R.drawable.ic_gallery_color_24dp));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.StatusSaver_gallery)).setIcon(R.drawable.statuspic));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.instaimage)).setIcon(R.drawable.ic_image));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.audios)).setIcon(R.drawable.ic_music_note_24dp));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final MyAdapter adapter = new MyAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        String nn = new SharedPrefsMainApp(GalleryActivityNewUi.this).getPREFERENCE_inappads();


        if (Constants.show_Ads && !BuildConfig.ISPRO) {
            if (nn.equals("nnn") && AdsManager.status_AdmobBanner) {
                MobileAds.initialize(
                        GalleryActivityNewUi.this,
                        initializationStatus -> {
                            AdsManager.loadBannerAd(GalleryActivityNewUi.this, findViewById(R.id.banner_container));
                        });

            } else {
                findViewById(R.id.banner_container).setVisibility(View.GONE);
            }
        } else {
            findViewById(R.id.banner_container).setVisibility(View.GONE);
        }


        boolean istutshownlong = new SharedPrefsMainApp(GalleryActivityNewUi.this).getPREFERENCE_istutshownlong();
        if (!istutshownlong) {
            if (!isFinishing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GalleryActivityNewUi.this);
                builder.setTitle(R.string.newop);
                builder.setMessage(R.string.singleclickandlongclick);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.close, (dialog, which) -> {
                    new SharedPrefsMainApp(GalleryActivityNewUi.this).setPREFERENCE_istutshownlong(true);

                });
                builder.show();
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {

        MenuInflater inflater = getMenuInflater();
        if (iUtils.isNewUi) {
            inflater.inflate(R.menu.gallery_context_menu_new, menu);

        } else {
            inflater.inflate(R.menu.gallery_context_menu, menu);
        }
        MenuItem menuItem = menu.findItem(R.id.enablebio);
        SwitchCompat mySwitch = (SwitchCompat) menuItem.getActionView();


        SharedPreferences prefs = getSharedPreferences("whatsapp_pref", MODE_PRIVATE);
        boolean lang = prefs.getBoolean("isBio", false);


        mySwitch.setChecked(lang);

        mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GalleryActivityNewUi.this.isChecked = mySwitch.isChecked();
            mySwitch.setChecked(isChecked);


            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isBio", GalleryActivityNewUi.this.isChecked);
            editor.apply();


            runOnUiThread(() -> {
                iUtils.ShowToast(GalleryActivityNewUi.this,
                        "Lock Enabled " + GalleryActivityNewUi.this.isChecked
                );
            });

        });
        return true;

    }


    public class MyAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            myContext = context;
            this.totalTabs = totalTabs;
        }


        @NotNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new GalleryVideosFragment();
                case 1:
                    return new GalleryStatusSaver();
                case 2:
                    return new GalleryImagesFragment();
                case 3:
                    return new GalleryAudiosFragment();
                default:
                    return null;
            }
        }


        @Override
        public int getCount() {
            return totalTabs;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdsManager.destroyAdview();
    }
}
