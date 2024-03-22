/*
 * *
 *  * Created by Syed Usama Ahmad on 9/16/22, 8:08 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 9/16/22, 7:03 PM
 *
 */

package com.erickruo.allinonevideodownloader.extraFeatures.instafollowersfrags;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.activities.InstagramLoginActivity;
import com.erickruo.allinonevideodownloader.adapters.InstagramFollowersListAdapter;
import com.erickruo.allinonevideodownloader.databinding.FragmentFollowersListInstaBinding;
import com.erickruo.allinonevideodownloader.models.instafollowers.EdgeFollowedBy;
import com.erickruo.allinonevideodownloader.models.instafollowers.InstagramFollowersModel;
import com.erickruo.allinonevideodownloader.models.instafollowers.Node;
import com.erickruo.allinonevideodownloader.models.instawithlogin.ModelInstagramPref;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsForInstagram;
import com.erickruo.allinonevideodownloader.utils.iUtils;
import com.erickruo.allinonevideodownloader.webservices.api.RetrofitApiInterface;
import com.erickruo.allinonevideodownloader.webservices.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FollowersListInsta extends Fragment {


    private FragmentFollowersListInstaBinding binding;
    private ProgressDialog progressDralogGenaratinglink;
    private InstagramFollowersListAdapter adapter;
    private List<Node> list;
    EdgeFollowedBy edgeFollowedBy;
    InstagramFollowersModel gsonObj;
    String INSTAGRAM_END_Cursor_Followers = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFollowersListInstaBinding.inflate(inflater, container, false);


        try {

            progressDralogGenaratinglink = new ProgressDialog(requireActivity());
            progressDralogGenaratinglink.setMessage(getString(R.string.nodeifittakeslonger));


            list = new ArrayList<>();

            binding.recInstaFollowers.setLayoutManager(new LinearLayoutManager(requireActivity()));

            adapter = new InstagramFollowersListAdapter(list, requireActivity());

            binding.recInstaFollowers.setAdapter(adapter);


            System.out.println("hvjksdhfhdkd bb ");
            loadSearchData();


            binding.swiperefreshlayout.setOnRefreshListener(() -> {
                try {
                    if (!INSTAGRAM_END_Cursor_Followers.equals("")) {
                        loadSearchData();
                        binding.swiperefreshlayout.setRefreshing(false);
                    }
                } catch (Exception e) {
                    binding.swiperefreshlayout.setRefreshing(false);

                    e.printStackTrace();
                }
            });


            binding.recInstaFollowers.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                }

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    try {
                        if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && !INSTAGRAM_END_Cursor_Followers.equals("")) {
                            binding.floatingloadmore.setVisibility(View.VISIBLE);
                            iUtils.ShowToast(requireActivity(), getString(R.string.taptoloadmore));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            binding.floatingloadmore.setOnClickListener(v -> {
                binding.floatingloadmore.setVisibility(View.GONE);
                loadSearchData();
            });

            binding.islogedinornot.setOnClickListener(v -> {
                startActivity(new Intent(requireActivity(), InstagramLoginActivity.class));
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


        return binding.getRoot();
    }


    public void loadSearchData() {


        SharedPrefsForInstagram sharedPrefsFor = new SharedPrefsForInstagram(requireActivity());
        ModelInstagramPref map = sharedPrefsFor.getPreference();
        String myCookies = "";
        if (map != null && map.getPREFERENCE_USERID() != null && !map.getPREFERENCE_USERID().equals("oopsDintWork") && !map.getPREFERENCE_USERID().equals("")) {
            myCookies = "ds_user_id=" + map.getPREFERENCE_USERID() + "; sessionid=" + map.getPREFERENCE_SESSIONID();
            System.out.println("hvjksdhfhdkd userpkId yhyhy ");

            binding.islogedinornot.setVisibility(View.GONE);
            binding.recInstaFollowers.setVisibility(View.VISIBLE);
            progressDralogGenaratinglink.show();
        } else {
            binding.islogedinornot.setVisibility(View.VISIBLE);
            binding.recInstaFollowers.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(myCookies)) {
            myCookies = "";
        }

//todo follows
//https://www.instagram.com/graphql/query/?query_hash=d04b0a864b4b54837c0d870b0e77e076&id=8354837521&fetch_mutual=true&first=10&after=

//todo followers
//https://www.instagram.com/graphql/query/?query_hash=c76146de99bb02f6415203be841dd25a&id=8354837521&fetch_mutual=true&first=10&after=

        RetrofitApiInterface apiService = RetrofitClient.getClient();

        Call<JsonObject> callResult = apiService.getInstagramSearchResults("https://www.instagram.com/graphql/query/?query_hash=c76146de99bb02f6415203be841dd25a&id=" + map.getPREFERENCE_USERID() + "&fetch_mutual=true&first=20&after=" + INSTAGRAM_END_Cursor_Followers, myCookies,
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
                    //4162923872
                    //3401888503


                    Gson gson = new Gson();
                    gsonObj = gson.fromJson(resss, InstagramFollowersModel.class);
                    edgeFollowedBy = gsonObj.getData().getUser().getEdge_followed_by();


                    for (int i = 0; i < edgeFollowedBy.getEdges().size(); i++) {

                        Node node = edgeFollowedBy.getEdges().get(i).getNode();

                        list.add(node);
                        System.out.println("hvjksdhfhdkd " + node.getIsVerified());

                    }


                    //     System.out.println("hvjksdhfhdkd length " + response.getJSONArray("places").getJSONObject(0).getJSONObject("place").getJSONObject("location").getString("short_name"));


                    adapter.notifyDataSetChanged();


                } catch (Exception e) {

                    e.printStackTrace();
                }


                try {
                    INSTAGRAM_END_Cursor_Followers = edgeFollowedBy.getPage_info().getEnd_cursor();
                    System.out.println("response1122334455cursor:   " + "cursor value " + INSTAGRAM_END_Cursor_Followers);

                } catch (Exception e) {
                    INSTAGRAM_END_Cursor_Followers = "";
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


    }


}