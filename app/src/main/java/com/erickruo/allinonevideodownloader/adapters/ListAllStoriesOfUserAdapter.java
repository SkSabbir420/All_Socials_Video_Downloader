/*
 * *
 *  * Created by Syed Usama Ahmad on 2/27/23, 1:22 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/26/23, 11:10 PM
 *
 */

package com.erickruo.allinonevideodownloader.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.activities.FullImageActivity;
import com.erickruo.allinonevideodownloader.activities.VideoPlayActivity;
import com.erickruo.allinonevideodownloader.models.storymodels.ModelInstaItem;
import com.erickruo.allinonevideodownloader.utils.DownloadFileMain;
import com.erickruo.allinonevideodownloader.utils.GlideApp;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import java.util.ArrayList;


public class ListAllStoriesOfUserAdapter extends RecyclerView.Adapter<ListAllStoriesOfUserAdapter.ViewHolder> {
    private final Activity context;
    private final ArrayList<ModelInstaItem> storyModelInstaItemList;
    private String username = "";

    public ListAllStoriesOfUserAdapter(Activity context, ArrayList<ModelInstaItem> list) {
        this.context = context;
        this.storyModelInstaItemList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.items_instastory_view_placeholder, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ModelInstaItem modelInstaItem = storyModelInstaItemList.get(position);
        try {
            if (modelInstaItem.getMedia_type() == 2) {
                viewHolder.playIcon.setVisibility(View.VISIBLE);
            } else {
                viewHolder.playIcon.setVisibility(View.GONE);
            }
            GlideApp.with(context)
                    .load(modelInstaItem.getImage_versions2().getCandidates().get(0).getUrl())
                    .placeholder(R.drawable.ic_appicon_pro).into(viewHolder.savedImage);
            System.out.println("response1122ff3344554444tt:  " + modelInstaItem.getImage_versions2().getCandidates().get(0).getUrl());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        viewHolder.relativinstastoryplace.setOnClickListener(view -> {
            if (modelInstaItem.getMedia_type() == 2) {

                context.startActivity(new Intent(context, VideoPlayActivity.class).putExtra("videourl", modelInstaItem.getVideo_versions().get(0).getUrl()).putExtra(AppMeasurementSdk.ConditionalUserProperty.NAME, "allvideo" + System.currentTimeMillis() + ".mp4"));
            } else {
                try {

                    Intent intent = new Intent(context, FullImageActivity.class);
                    intent.putExtra("myimgfile", modelInstaItem.getImage_versions2().getCandidates().get(0).getUrl());
                    context.startActivity(intent);

                } catch (Exception e) {
                    iUtils.ShowToast(context, context.getResources().getString(R.string.somth_video_wrong));
                    Log.e("Errorisnewis", e.getMessage());
                }

            }
        });


        try {
            username = modelInstaItem.getUser().getPk() + "_" + modelInstaItem.getId();
        } catch (Exception e) {
            username = modelInstaItem.getId();
        }


        viewHolder.downloadID.setOnClickListener(v -> {
            if (modelInstaItem.getMedia_type() == 2) {


                DownloadFileMain.startDownloading(context, modelInstaItem.getVideo_versions().get(0).getUrl(),
                        "instastory_" + username, ".mp4");
            } else {

                DownloadFileMain.startDownloading(context, modelInstaItem.getImage_versions2().getCandidates().get(0).getUrl(),
                        "instastory_" + username, ".png");

            }
        });

        viewHolder.linlayoutclickinsta.setOnClickListener(view -> {
            if (modelInstaItem.getMedia_type() == 2) {


                DownloadFileMain.startDownloading(context, modelInstaItem.getVideo_versions().get(0).getUrl(),
                        "instastory_" + username, ".mp4");
            } else {

                DownloadFileMain.startDownloading(context, modelInstaItem.getImage_versions2().getCandidates().get(0).getUrl(),
                        "instastory_" + username, ".png");

            }
        });

    }

    @Override
    public int getItemCount() {
        return storyModelInstaItemList == null ? 0 : storyModelInstaItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        ImageView savedImage;
        ImageView playIcon;
        ImageView downloadID;
        RelativeLayout relativinstastoryplace;
        LinearLayout linlayoutclickinsta;

        public ViewHolder(View itemView) {
            super(itemView);
            relativinstastoryplace = itemView.findViewById(R.id.relativinstastoryplace);
            userName = itemView.findViewById(R.id.profileUserName);
            savedImage = itemView.findViewById(R.id.storyimgview);
            playIcon = itemView.findViewById(R.id.storyVIDview);
            downloadID = itemView.findViewById(R.id.downloadID);
            linlayoutclickinsta = itemView.findViewById(R.id.linlayoutclickinsta);
        }
    }
}