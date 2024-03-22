/*
 * *
 *  * Created by Syed Usama Ahmad on 2/23/23, 12:24 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/22/23, 10:00 PM
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

import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.activities.FullImageActivity;
import com.erickruo.allinonevideodownloader.models.bulkdownloader.EdgeInfo;
import com.erickruo.allinonevideodownloader.utils.Constants;
import com.erickruo.allinonevideodownloader.utils.GlideApp;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import java.util.List;


public class ListAllProfilePostsInstagramUserAdapter extends RecyclerView.Adapter<ListAllProfilePostsInstagramUserAdapter.ViewHolder> {
    private final Activity context;
    private final List<EdgeInfo> storyModelInstaItemList;

    public ListAllProfilePostsInstagramUserAdapter(Activity context, List<EdgeInfo> list) {
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
        EdgeInfo modelInstaItem = storyModelInstaItemList.get(position);

        try {
            if (modelInstaItem.getNode().getIs_video()) {
                viewHolder.playIcon.setVisibility(View.VISIBLE);
            } else {
                viewHolder.playIcon.setVisibility(View.GONE);
            }
            GlideApp.with(context)
                    .load(modelInstaItem.getNode().getThumbnail_src())
                    .placeholder(R.drawable.ic_appicon_pro).into(viewHolder.savedImage);
            System.out.println("response1122ff3344554444tt:  " + modelInstaItem.getNode().getThumbnail_src());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        viewHolder.relativinstastoryplace.setOnClickListener(view -> {
            if (modelInstaItem.getNode().getIs_video()) {

                Constants.INSTANCE.startInstaDownload((Activity) context,
                        "http://www.instagram.com/p/" + modelInstaItem.getNode().getShortcode() + "?__a=1&__d=dis"
                );

            } else {
                try {

                    Intent intent = new Intent(context, FullImageActivity.class);
                    intent.putExtra("myimgfile", modelInstaItem.getNode().getThumbnail_src());
                    context.startActivity(intent);

                } catch (Exception e) {
                    iUtils.ShowToast(context, context.getResources().getString(R.string.somth_video_wrong));
                    Log.e("Errorisnewis", e.getMessage());
                }

            }
        });


        viewHolder.downloadID.setOnClickListener(v -> Constants.INSTANCE.startInstaDownload((Activity) context,
                "http://www.instagram.com/p/" + modelInstaItem.getNode().getShortcode() + "?__a=1&__d=dis"
        ));


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