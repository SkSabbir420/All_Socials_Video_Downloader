/*
 * *
 *  * Created by Syed Usama Ahmad on 2/27/23, 1:22 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/26/23, 11:10 PM
 *
 */

package com.erickruo.allinonevideodownloader.adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.activities.BulkDownloaderProfileActivity;
import com.erickruo.allinonevideodownloader.activities.InstagramFollowersList;
import com.erickruo.allinonevideodownloader.models.instafollowers.Node;
import com.erickruo.allinonevideodownloader.models.instawithlogin.ModelInstagramPref;
import com.erickruo.allinonevideodownloader.utils.GlideApp;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsForInstagram;
import com.erickruo.allinonevideodownloader.webservices.api.RetrofitApiInterface;
import com.erickruo.allinonevideodownloader.webservices.api.RetrofitClient;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstagramFollowersListAdapter extends RecyclerView.Adapter<InstagramFollowersListAdapter.Viewholder> {
    boolean followsback = false;
    private final List<Node> data;
    private final Activity context;

    public InstagramFollowersListAdapter(List<Node> data, Activity context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.instragram_followers_row_layout, parent, false);

        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {

        Node model = data.get(position);

        GlideApp.with(context).load(model.getProfilePicURL()).placeholder(R.drawable.ic_appicon_pro).into(holder.row_search_imageview);
        holder.row_search_name_textview.setText(model.getFullName());
        holder.row_search_detail_textview.setText(model.getUsername());

        if (!model.getIsVerified()) {
            holder.row_search_approved_imageview.setVisibility(View.GONE);

        } else {
            holder.row_search_approved_imageview.setVisibility(View.VISIBLE);

        }

        if (!model.getIsPrivate()) {
            holder.row_search_private_imageview.setVisibility(View.GONE);

        } else {
            holder.row_search_private_imageview.setVisibility(View.VISIBLE);

        }


        holder.row_followedyou_textview.setOnClickListener(v -> {
            checkfollowBackInstaAccount(context, model.getUsername(), holder);
        });


        holder.row_btn_unfollow.setOnClickListener(view -> {

            if (model.getFollowedByViewer()) {
                InstagramFollowersList.followUnfollowInstaAccount(context, model.getID(), false);
                holder.row_btn_unfollow.setText(context.getResources().getString(R.string.follow));
                holder.row_followedyou_textview.setText(context.getResources().getString(R.string.notfollowedbyviewer));

            } else {
                InstagramFollowersList.followUnfollowInstaAccount(context, model.getID(), true);
                holder.row_btn_unfollow.setText(context.getResources().getString(R.string.unfollow));
                holder.row_followedyou_textview.setText(context.getResources().getString(R.string.followedbyviewer));

            }


        });

        holder.row_search_name_textview.setOnClickListener(v -> {
            openBulkProfilePage(model);
        });
        holder.row_search_imageview.setOnClickListener(v -> {
            openBulkProfilePage(model);
        });


    }

    public void checkfollowBackInstaAccount(Activity context, String username, Viewholder holder) {
        try {


            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getResources().getString(R.string.loading));
            progressDialog.show();


            SharedPrefsForInstagram sharedPrefsFor = new SharedPrefsForInstagram(context);
            ModelInstagramPref map = sharedPrefsFor.getPreference();
            String myCookies = "";
            if (map != null && map.getPREFERENCE_USERID() != null && !map.getPREFERENCE_USERID().equals("oopsDintWork") && !map.getPREFERENCE_USERID().equals("")) {
                myCookies = "ds_user_id=" + map.getPREFERENCE_USERID() + "; sessionid=" + map.getPREFERENCE_SESSIONID();
                System.out.println("hvjksdhfhdkd userpkId yhyhy ");

            } else {

                followsback = false;
            }

            if (TextUtils.isEmpty(myCookies)) {
                myCookies = "";
            }

            RetrofitApiInterface apiService = RetrofitClient.getClient();

            Call<JsonObject> callResult = apiService.getInstagramSearchResults("https://i.instagram.com/api/v1/users/web_profile_info/?username=" + username, myCookies,
                    "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+");


            callResult.enqueue(new Callback<JsonObject>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    System.out.println("response1122334455_jsomobj: follows  " + response);

                    if (progressDialog != null)
                        progressDialog.dismiss();

                    try {

                        String resss = response.body().toString();
                        System.out.println("hvjksdhfhdkd follows" + resss);

                        JSONObject jsonObject = new JSONObject(resss);
                        followsback = jsonObject.getJSONObject("data").getJSONObject("user").getBoolean("follows_viewer");
                        System.out.println("hvjksdhfhdkd followsback" + followsback);


                        if (followsback) {
                            holder.row_followedyou_textview.setTextColor(context.getResources().getColor(R.color.green_colorPrimary));
                            holder.row_followedyou_textview.setText(context.getResources().getString(R.string.followedbyviewer));
                        } else {
                            holder.row_followedyou_textview.setTextColor(context.getResources().getColor(R.color.colorNegative));
                            holder.row_followedyou_textview.setText(context.getResources().getString(R.string.notfollowedbyviewer));
                        }

                    } catch (Exception e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        followsback = false;
                        holder.row_followedyou_textview.setTextColor(context.getResources().getColor(R.color.colorNegative));
                        holder.row_followedyou_textview.setText(context.getResources().getString(R.string.notfollowedbyviewer));
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    System.out.println("response1122334455:   " + "Failed0");
                    if (progressDialog != null)
                        progressDialog.dismiss();

                    followsback = false;
                    holder.row_followedyou_textview.setTextColor(context.getResources().getColor(R.color.colorNegative));
                    holder.row_followedyou_textview.setText(context.getResources().getString(R.string.notfollowedbyviewer));
                }
            });


        } catch (Exception e) {

            System.out.println("working errpr \t Value: " + e.getMessage());
            followsback = false;
            holder.row_followedyou_textview.setTextColor(context.getResources().getColor(R.color.colorNegative));
            holder.row_followedyou_textview.setText(context.getResources().getString(R.string.notfollowedbyviewer));
        }


    }

    void openBulkProfilePage(Node model) {
        context.startActivity(new Intent(context, BulkDownloaderProfileActivity.class).putExtra("username", model.getUsername()).putExtra("pkId", model.getID()));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        public com.makeramen.roundedimageview.RoundedImageView row_search_imageview;
        public ImageView row_search_private_imageview;
        public TextView row_search_name_textview;
        public ImageView row_search_approved_imageview;
        public TextView row_search_detail_textview;
        public TextView row_followedyou_textview;
        public LinearLayout row_search_lay;
        public Button row_btn_unfollow;


        public Viewholder(View itemView) {
            super(itemView);

            row_search_lay = itemView.findViewById(R.id.row_search_lay);
            row_search_imageview = itemView.findViewById(R.id.row_search_imageview);
            row_search_private_imageview = itemView.findViewById(R.id.row_search_private_imageview);
            row_search_name_textview = itemView.findViewById(R.id.row_search_name_textview);
            row_search_approved_imageview = itemView.findViewById(R.id.row_search_approved_imageview);
            row_search_detail_textview = itemView.findViewById(R.id.row_search_detail_textview);
            row_btn_unfollow = itemView.findViewById(R.id.row_btn_unfollow);
            row_followedyou_textview = itemView.findViewById(R.id.row_followedyou_textview);

        }
    }

}
