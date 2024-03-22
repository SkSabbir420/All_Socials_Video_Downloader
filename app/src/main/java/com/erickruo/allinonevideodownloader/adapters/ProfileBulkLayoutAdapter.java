package com.erickruo.allinonevideodownloader.adapters;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.activities.BulkDownloaderProfileActivity;
import com.erickruo.allinonevideodownloader.models.bulkdownloader.UserUser;
import com.erickruo.allinonevideodownloader.utils.GlideApp;

import java.util.List;

public class ProfileBulkLayoutAdapter extends RecyclerView.Adapter<ProfileBulkLayoutAdapter.Viewholder> {

    private final List<UserUser> data;
    private final Activity context;

    public ProfileBulkLayoutAdapter(List<UserUser> data, Activity context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_row_bulk_downloader_layout, parent, false);

        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {

        UserUser model = data.get(position);

        GlideApp.with(context).load(model.profile_pic_url).placeholder(R.drawable.insta).into(holder.row_search_imageview);
        holder.row_search_name_textview.setText(model.fullName);
        holder.row_search_detail_textview.setText(model.username);

        if (!model.is_verified) {
            holder.row_search_approved_imageview.setVisibility(View.GONE);

        } else {
            holder.row_search_approved_imageview.setVisibility(View.VISIBLE);

        }

        if (!model.is_private) {
            holder.row_search_private_imageview.setVisibility(View.GONE);

        } else {
            holder.row_search_private_imageview.setVisibility(View.VISIBLE);

        }

        holder.row_search_lay.setOnClickListener(v -> context.startActivity(new Intent(context, BulkDownloaderProfileActivity.class).putExtra("username", model.username).putExtra("pkId", model.pk)));


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        public LinearLayout row_search_lay;
        public com.makeramen.roundedimageview.RoundedImageView row_search_imageview;
        public ImageView row_search_private_imageview;
        public TextView row_search_name_textview;
        public ImageView row_search_approved_imageview;
        public TextView row_search_detail_textview;


        public Viewholder(View itemView) {
            super(itemView);

            row_search_lay = itemView.findViewById(R.id.row_search_lay);
            row_search_imageview = itemView.findViewById(R.id.row_search_imageview);
            row_search_private_imageview = itemView.findViewById(R.id.row_search_private_imageview);
            row_search_name_textview = itemView.findViewById(R.id.row_search_name_textview);
            row_search_approved_imageview = itemView.findViewById(R.id.row_search_approved_imageview);
            row_search_detail_textview = itemView.findViewById(R.id.row_search_detail_textview);

        }
    }

}
