/*
 * *
 *  * Created by Syed Usama Ahmad on 3/3/23, 12:53 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/2/23, 11:57 PM
 *
 */

package com.erickruo.allinonevideodownloader.adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.activities.FullImageActivity;
import com.erickruo.allinonevideodownloader.activities.FullViewArrayActivity;
import com.erickruo.allinonevideodownloader.activities.VideoPlayActivity;
import com.erickruo.allinonevideodownloader.interfaces.OnClickFileDeleteListner;
import com.erickruo.allinonevideodownloader.utils.GlideApp;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

public class InstagramImageFileListAdapter extends RecyclerView.Adapter<InstagramImageFileListAdapter.ViewHolder> {
    private final Activity context;
    private final ArrayList<File> fileArrayList;
    private LayoutInflater layoutInflater;
    private boolean isVideo = true;

    OnClickFileDeleteListner onClickFileDeleteListner;

    public InstagramImageFileListAdapter(Activity context, ArrayList<File> fileArrayList, OnClickFileDeleteListner onClickFileDeleteListner) {
        this.context = context;
        this.fileArrayList = fileArrayList;
        this.onClickFileDeleteListner = onClickFileDeleteListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        return new ViewHolder(layoutInflater.inflate(R.layout.image_item_instagram, viewGroup, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        File fileItem = fileArrayList.get(i);
        String contentType = "";

        try {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                contentType = Files.probeContentType(fileItem.toPath());

            }

            if (!contentType.equals("")) {
                System.out.println("InstagramImageFileListAdapter_contentType= " + contentType);
                if (contentType.contains("image")) {
                    viewHolder.playButtonImageadapter.setVisibility(View.GONE);
                    isVideo = false;
                } else if (contentType.contains("video")) {
                    viewHolder.playButtonImageadapter.setVisibility(View.VISIBLE);
                    isVideo = true;
                }
            } else {
                String extension = fileItem.getName().substring(fileItem.getName().lastIndexOf("."));

                if (fileItem.getAbsolutePath().endsWith(".mp4") || fileItem.getAbsolutePath().endsWith(".webm") || fileItem.getAbsolutePath().endsWith(".mkv")) {
                    viewHolder.playButtonImageadapter.setVisibility(View.VISIBLE);
                    isVideo = true;
                } else {
                    viewHolder.playButtonImageadapter.setVisibility(View.GONE);
                    isVideo = false;

                }


            }

            GlideApp.with(context)
                    .load(fileItem.getPath())
                    .placeholder(R.drawable.ic_appicon_pro)
                    .into(viewHolder.myimage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        viewHolder.mainrelativelayout.setOnClickListener(v -> {
            if (isVideo) {
                if (!fileItem.getAbsolutePath().contains(".gif")) {

                    context.startActivity(new Intent(context, VideoPlayActivity.class)
                            .putExtra("videourl", fileItem.getAbsolutePath()));

                } else {
                    iUtils.ShowToast(context, context.getString(R.string.preview_not));

                }
            } else {
                try {

//                    Intent intent = new Intent(context, FullImageActivity.class);
//                    intent.putExtra("myimgfile", fileArrayList.get(i).getAbsolutePath());
//                    context.startActivity(intent);


                    Intent intent = new Intent(context, FullViewArrayActivity.class);
                    intent.putExtra("imagesList", fileArrayList);
                    intent.putExtra("pos", i);
                    context.startActivity(intent);

                } catch (Exception e) {
                    iUtils.ShowToast(context, context.getResources().getString(R.string.somth_video_wrong));
                    Log.e("Errorisnewis", e.getMessage());
                }
            }
        });
        viewHolder.mainrelativelayout.setOnLongClickListener(v -> {


            if (isVideo) {

                final String[] options = {context.getString(R.string.watch_arr), context.getString(R.string.del_arr), context.getString(R.string.share_arr)};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.choose);
                builder.setItems(options, (dialog, which) -> {
                    if (options[which].contains(context.getString(R.string.watch_arr))) {

                        try {

                            dialog.dismiss();


                            if (!fileItem.getAbsolutePath().contains(".gif")) {
//                                        context.startActivity(new Intent(context, PlayActivity.class)
//                                                .putExtra("videourl", fileItem.getAbsolutePath())
//                                                .putExtra(AppMeasurementSdk.ConditionalUserProperty.NAME, fileItem.getName()));
//

                                context.startActivity(new Intent(context, VideoPlayActivity.class)
                                        .putExtra("videourl", fileItem.getAbsolutePath()));

                            } else {
                                iUtils.ShowToast(context, context.getString(R.string.preview_not));

                            }

                        } catch (Exception e) {
                            iUtils.ShowToast(context, context.getResources().getString(R.string.somth_video_wrong));
                            Log.e("Errorisnewis", e.getMessage());
                        }
                    } else if (options[which].contains(context.getString(R.string.del_arr))) {
                        new AlertDialog.Builder(context)
                                .setTitle(context.getString(R.string.del_arr))
                                .setMessage(context.getResources().getString(R.string.delete_confirm))
                                .setCancelable(false)
                                .setPositiveButton(context.getString(R.string.del_arr), (dialog1, whichButton) -> {
                                    try {
                                        fileArrayList.get(i).delete();
                                        System.out.println("mudatauriis = " + fileArrayList.get(i).getAbsolutePath());

                                        onClickFileDeleteListner.delFile(fileArrayList.get(i).getAbsolutePath());

                                        fileArrayList.remove(i);
                                        notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                })
                                .setNegativeButton(context.getString(R.string.cancel_option), null).show();

                    } else {
                        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                        File fileWithinMyDir = new File(fileItem.getAbsolutePath());

                        if (fileWithinMyDir.exists()) {

                            try {
                                intentShareFile.setType("video/mp4");
                                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileItem.getAbsolutePath()));

                                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                        context.getString(R.string.SharingVideoSubject));
                                intentShareFile.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.SharingVideoBody));

                                context.startActivity(Intent.createChooser(intentShareFile, context.getString(R.string.SharingVideoTitle)));
                            } catch (ActivityNotFoundException e) {
                                iUtils.ShowToast(context, context.getResources().getString(R.string.went_shARRE));

                            }

                        }
                    }
                });
                builder.show();

            } else {
                final String[] options = {context.getString(R.string.viewimage), context.getString(R.string.del_arr), context.getString(R.string.share_arr)};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.choose);
                builder.setItems(options, (dialog, which) -> {
                    if (options[which].contains(context.getString(R.string.viewimage))) {

                        try {

                            Intent intent = new Intent(context, FullImageActivity.class);
                            intent.putExtra("myimgfile", fileArrayList.get(i).getAbsolutePath());
                            context.startActivity(intent);

                        } catch (Exception e) {
                            iUtils.ShowToast(context, context.getResources().getString(R.string.somth_video_wrong));
                            Log.e("Errorisnewis", e.getMessage());
                        }
                    } else if (options[which].contains(context.getString(R.string.del_arr))) {
                        new AlertDialog.Builder(context)
                                .setTitle(context.getString(R.string.del_arr))
                                .setMessage(context.getResources().getString(R.string.delete_confirm_image))
                                .setCancelable(false)
                                .setPositiveButton(context.getString(R.string.del_arr), (dialog12, whichButton) -> {
                                    try {
                                        fileArrayList.get(i).delete();
                                        System.out.println("mudatauriis = " + fileArrayList.get(i).getAbsolutePath());

                                        onClickFileDeleteListner.delFile(fileArrayList.get(i).getAbsolutePath());

                                        fileArrayList.remove(i);
                                        notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                })
                                .setNegativeButton(context.getString(R.string.cancel_option), null).show();

                    } else {
                        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                        File fileWithinMyDir = new File(fileArrayList.get(i).getAbsolutePath());

                        if (fileWithinMyDir.exists()) {

                            try {
                                intentShareFile.setType("image/*");
                                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileArrayList.get(i).getAbsolutePath()));

                                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                        context.getString(R.string.SharingimageSubject));
                                intentShareFile.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.SharingimageBody));

                                context.startActivity(Intent.createChooser(intentShareFile, context.getString(R.string.SharingimageTitle)));
                            } catch (ActivityNotFoundException e) {
                                iUtils.ShowToast(context, context.getResources().getString(R.string.went_shARRE));

                            }

                        }
                    }
                });
                builder.show();

            }

            return false;
        });
    }

    @Override
    public int getItemCount() {
        return fileArrayList == null ? 0 : fileArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout mainrelativelayout;
        private final ImageView myimage;
        private final ImageView playButtonImageadapter;


        public ViewHolder(View view) {
            super(view);
            mainrelativelayout = view.findViewById(R.id.mainrelativelayout);
            myimage = view.findViewById(R.id.myimage);
            playButtonImageadapter = view.findViewById(R.id.playButtonImageadapter);

        }
    }
}