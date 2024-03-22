/*
 * *
 *  * Created by Syed Usama Ahmad on 2/27/23, 1:22 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/26/23, 11:10 PM
 *
 */

package com.erickruo.allinonevideodownloader.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.adapters.ListAllProfilePostsInstagramUserAdapter;
import com.erickruo.allinonevideodownloader.databinding.ActivityBulkDownloaderProfileBinding;
import com.erickruo.allinonevideodownloader.models.bulkdownloader.EdgeInfo;
import com.erickruo.allinonevideodownloader.models.bulkdownloader.UserProfileDataModelBottomPart;
import com.erickruo.allinonevideodownloader.models.instawithlogin.ModelInstagramPref;
import com.erickruo.allinonevideodownloader.utils.GlideApp;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsForInstagram;
import com.erickruo.allinonevideodownloader.utils.iUtils;
import com.erickruo.allinonevideodownloader.webservices.api.RetrofitApiInterface;
import com.erickruo.allinonevideodownloader.webservices.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Keep
public class BulkDownloaderProfileActivity extends AppCompatActivity {

    public static String INSTAGRAM_END_CAROUSEL = "";
    ListAllProfilePostsInstagramUserAdapter listAllProfilePostsInstagramUserAdapter;
    private String myUserPKID;
    private ActivityBulkDownloaderProfileBinding binding;
    private List<EdgeInfo> storyModelInstaItemList;
    private String myCookies = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBulkDownloaderProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        try {

            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            storyModelInstaItemList = new ArrayList<>();


            listAllProfilePostsInstagramUserAdapter = new ListAllProfilePostsInstagramUserAdapter(BulkDownloaderProfileActivity.this, storyModelInstaItemList);
            binding.recyclerviewProfileLong.setLayoutManager(new GridLayoutManager(BulkDownloaderProfileActivity.this, 3));
            binding.recyclerviewProfileLong.setAdapter(listAllProfilePostsInstagramUserAdapter);

            if (getIntent() != null) {
                String myUserName = getIntent().getStringExtra("username");
                myUserPKID = getIntent().getStringExtra("pkId");
                loadAllProfileData(myUserName, myUserPKID);
            } else {

                binding.profileLongProgress.setVisibility(View.GONE);


                runOnUiThread(() -> {
                    iUtils.ShowToastError(BulkDownloaderProfileActivity.this,
                            "Error Getting Detail !!!"
                    );
                });
            }


            binding.recyclerviewProfileLong.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                }

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    try {
                        if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                            binding.floatingloadmore.setVisibility(View.VISIBLE);
                            runOnUiThread(() -> {
                                iUtils.ShowToast(BulkDownloaderProfileActivity.this,
                                        getResources().getString(R.string.taptoloadmore)
                                );
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            binding.floatingloadmore.setOnClickListener(v -> {
                binding.floatingloadmore.setVisibility(View.GONE);
                loadAllPostsData();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadAllProfileData(String username, String pkId) {

        SharedPrefsForInstagram sharedPrefsFor = new SharedPrefsForInstagram(BulkDownloaderProfileActivity.this);
        ModelInstagramPref map = sharedPrefsFor.getPreference();
        if (map != null && map.getPREFERENCE_USERID() != null && !map.getPREFERENCE_USERID().equals("oopsDintWork") && !map.getPREFERENCE_USERID().equals("")) {
            myCookies = "ds_user_id=" + map.getPREFERENCE_USERID() + "; sessionid=" + map.getPREFERENCE_SESSIONID();
        } else {
            myCookies = iUtils.myInstagramTempCookies;
        }

        if (TextUtils.isEmpty(myCookies)) {
            myCookies = "";
        }

        RetrofitApiInterface apiService = RetrofitClient.getClient();

        Call<JsonObject> callResult = apiService.getInstagramProfileDataBulk("https://www.instagram.com/" + username + "/?__a=1&__d=dis", myCookies,
                "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+");


        callResult.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                System.out.println("hvjksdhfhdkd userpkId 00 " + pkId + " username " + username);

                //4162923872
                //3401888503

                try {


                    JsonObject userdata = Objects.requireNonNull(response.body()).getAsJsonObject("graphql").getAsJsonObject("user");
                    binding.profileFollowersNumberTextview.setText(userdata.getAsJsonObject("edge_followed_by").get("count").getAsString());
                    binding.profileFollowingNumberTextview.setText(userdata.getAsJsonObject("edge_follow").get("count").getAsString());
                    binding.profilePostNumberTextview.setText(userdata.getAsJsonObject("edge_owner_to_timeline_media").get("count").getAsString());
                    binding.profileLongIdTextview.setText(userdata.get("username").getAsString());

                    if (userdata.get("is_verified").getAsBoolean()) {
                        binding.profileLongApprovedImageview.setVisibility(View.VISIBLE);
                        GlideApp.with(BulkDownloaderProfileActivity.this)
                                .load(R.drawable.approved)
                                .placeholder(R.drawable.ic_appicon_pro).into( binding.profileLongApprovedImageview);

                    } else {
                        binding.profileLongApprovedImageview.setVisibility(View.GONE);

                    }

                    if (userdata.get("is_private").getAsBoolean()) {
                        binding.rowSearchPrivateImageviewPrivate.setVisibility(View.VISIBLE);
                        GlideApp.with(BulkDownloaderProfileActivity.this)
                                .load(R.drawable.private_icon)
                                .placeholder(R.drawable.ic_appicon_pro).into(binding.rowSearchPrivateImageviewPrivate);
                    } else {
                        binding.rowSearchPrivateImageviewPrivate.setVisibility(View.GONE);

                    }

                    GlideApp.with(BulkDownloaderProfileActivity.this).load(userdata.get("profile_pic_url").getAsString()).placeholder(R.drawable.ic_appicon_pro).into(binding.profileLongCircle);


                } catch (Exception e) {

                    System.out.println("hvjksdhfhdkd eeee errr 00 " + e.getMessage());


                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                System.out.println("hvjksdhfhdkd:   " + "Failed0");

            }
        });

        loadAllPostsData();


    }


    void loadAllPostsData() {
        binding.profileLongProgress.setVisibility(View.VISIBLE);


        SharedPrefsForInstagram sharedPrefsFor = new SharedPrefsForInstagram(BulkDownloaderProfileActivity.this);
        ModelInstagramPref map = sharedPrefsFor.getPreference();

        if (map != null && map.getPREFERENCE_USERID() != null && !map.getPREFERENCE_USERID().equals("oopsDintWork") && !map.getPREFERENCE_USERID().equals("")) {
            myCookies = "ds_user_id=" + map.getPREFERENCE_USERID() + "; sessionid=" + map.getPREFERENCE_SESSIONID();
            System.out.println("hvjksdhfhdkd userpkId yhyhy 2");

        } else {
            myCookies = iUtils.myInstagramTempCookies;
        }

        if (TextUtils.isEmpty(myCookies)) {
            myCookies = "";
        }

        RetrofitApiInterface apiService = RetrofitClient.getClient();

        Call<JsonObject> callResult = apiService.getInstagramProfileDataAllImagesAndVideosBulk("https://instagram.com/graphql/query/?query_id=17888483320059182&id=" + myUserPKID + "&first=20&after=" + INSTAGRAM_END_CAROUSEL, myCookies,
                iUtils.UserAgentsList[0]);


        callResult.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                binding.profileLongProgress.setVisibility(View.GONE);

                System.out.println("hvjksdhfhdkd userpkId vv " + myUserPKID + " username");

                //4162923872
                //3401888503

                try {
                    Gson gson = new Gson();
                    UserProfileDataModelBottomPart root = gson.fromJson(Objects.requireNonNull(response.body()).toString(), UserProfileDataModelBottomPart.class);
                    storyModelInstaItemList.addAll(root.getData().getUser().getEdge_owner_to_timeline_media().getEdges());
                    INSTAGRAM_END_CAROUSEL = root.getData().getUser().getEdge_owner_to_timeline_media().getPage_info().getEnd_cursor();

                } catch (Exception e) {
                    System.out.println("hvjksdhfhdkd eeee  vv errrrrrrr" + e.getMessage());
                }
                listAllProfilePostsInstagramUserAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                binding.profileLongProgress.setVisibility(View.GONE);

                System.out.println("hvjksdhfhdkd eeee  vv " + t.getLocalizedMessage());

            }
        });


    }


    @Override
    public void onBackPressed() {
        INSTAGRAM_END_CAROUSEL = "";
        finish();
    }
}