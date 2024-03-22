/*
 * *
 *  * Created by Syed Usama Ahmad on 3/4/23, 8:29 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/4/23, 8:04 PM
 *
 */

package com.erickruo.allinonevideodownloader.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.MobileAds;
import com.google.gson.JsonObject;
import com.erickruo.allinonevideodownloader.BuildConfig;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.databinding.ActivityInstagramFollowersListBinding;
import com.erickruo.allinonevideodownloader.extraFeatures.instafollowersfrags.FollowersListInsta;
import com.erickruo.allinonevideodownloader.extraFeatures.instafollowersfrags.FollowingsListInsta;
import com.erickruo.allinonevideodownloader.models.instawithlogin.ModelInstagramPref;
import com.erickruo.allinonevideodownloader.utils.AdsManager;
import com.erickruo.allinonevideodownloader.utils.Constants;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsForInstagram;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsMainApp;
import com.erickruo.allinonevideodownloader.webservices.api.RetrofitApiInterface;
import com.erickruo.allinonevideodownloader.webservices.api.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import me.ibrahimsn.lib.OnItemSelectedListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstagramFollowersList extends AppCompatActivity {

    private ActivityInstagramFollowersListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstagramFollowersListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.instagram_followers));


        } catch (Throwable e) {
            e.printStackTrace();
        }


        try {
            final MyAdapterInstaFollowers adapter = new MyAdapterInstaFollowers(getSupportFragmentManager(), 2);
            binding.viewpagergallery.setAdapter(adapter);


            String nn = new SharedPrefsMainApp(InstagramFollowersList.this).getPREFERENCE_inappads();


            if (Constants.show_Ads && !BuildConfig.ISPRO) {
                if (nn.equals("nnn") && AdsManager.status_AdmobBanner) {
                    MobileAds.initialize(
                            getApplicationContext(),
                            initializationStatus -> {
                                AdsManager.loadBannerAdsAdapter(this, binding.bannerContainer);
                            });
                } else {
                    binding.bannerContainer.setVisibility(View.GONE);
                }
            }

            binding.bottomNavBargallery.setOnItemSelectedListener((OnItemSelectedListener) i -> {
                binding.viewpagergallery.setCurrentItem(i);
                return false;
            });

            binding.viewpagergallery.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    binding.bottomNavBargallery.setItemActiveIndex(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } catch (Throwable e) {
            e.printStackTrace();
        }


    }


    public static void followUnfollowInstaAccount(Context context, String userid, boolean follow) {
        try {

            ProgressDialog progressDralogGenaratinglink = new ProgressDialog(context);
            progressDralogGenaratinglink.setMessage(context.getResources().getString(R.string.loading));
            progressDralogGenaratinglink.show();

            String FOLLOW_ACCOUNT = "https://i.instagram.com/api/v1/web/friendships/" + userid + "/follow/";
            String UNFOLLOW_ACCOUNT = "https://i.instagram.com/api/v1/web/friendships/" + userid + "/unfollow/";


            SharedPrefsForInstagram sharedPrefsFor = new SharedPrefsForInstagram(context);
            ModelInstagramPref map = sharedPrefsFor.getPreference();
            String myCookies = "";
            if (map != null && map.getPREFERENCE_USERID() != null && !map.getPREFERENCE_USERID().equals("oopsDintWork") && !map.getPREFERENCE_USERID().equals("")) {
                myCookies = "ds_user_id=" + map.getPREFERENCE_USERID() + "; sessionid=" + map.getPREFERENCE_SESSIONID();
                System.out.println("hvjksdhfhdkd userpkId yhyhy ");

            } else {

                return;
            }

            if (TextUtils.isEmpty(myCookies)) {
                myCookies = "";
            }

            RetrofitApiInterface apiService = RetrofitClient.getClient();

            Call<JsonObject> callResult = apiService.getInstagramSearchResultsPost((follow) ? FOLLOW_ACCOUNT : UNFOLLOW_ACCOUNT, myCookies,
                    "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+");


            callResult.enqueue(new Callback<JsonObject>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    System.out.println("response1122334455_jsomobj:   " + response);

                    if (progressDralogGenaratinglink != null)
                        progressDralogGenaratinglink.dismiss();


                    try {

                        String resss = response.body().toString();
                        System.out.println("hvjksdhfhdkd " + resss);


                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    System.out.println("response1122334455:   " + "Failed0");
                    if (progressDralogGenaratinglink != null)
                        progressDralogGenaratinglink.dismiss();
                }
            });


        } catch (Exception e) {
            System.out.println("working errpr \t Value: " + e.getMessage());
        }
    }


    public class MyAdapterInstaFollowers extends FragmentPagerAdapter {

        int totalTabs;

        public MyAdapterInstaFollowers(FragmentManager fm, int totalTabs) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.totalTabs = totalTabs;
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FollowersListInsta();
                case 1:
                    return new FollowingsListInsta();
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