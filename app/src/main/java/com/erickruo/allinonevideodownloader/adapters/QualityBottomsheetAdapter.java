/*
 * *
 *  * Created by Syed Usama Ahmad on 3/4/23, 2:35 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/4/23, 2:28 AM
 *
 */

package com.erickruo.allinonevideodownloader.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.models.dlapismodels.Format;
import com.erickruo.allinonevideodownloader.models.dlapismodels.Video;
import com.erickruo.allinonevideodownloader.utils.DownloadFileMain;
import com.erickruo.allinonevideodownloader.utils.iUtils;
import com.erickruo.allinonevideodownloader.utils.work.DownloadWorker;
import com.yausername.youtubedl_android.mapper.VideoFormat;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class QualityBottomsheetAdapter extends RecyclerView.Adapter<QualityBottomsheetAdapter.ViewHolder> {

    private String url, title, id;
    boolean isDLAPI = false;
    boolean issingle;
    boolean hasMultiAlbumb = false;
    private Activity context;
    private Activity activity;
    private List<Format> filesList;
    private List<Video> filesVideoList;
    private List<VideoFormat> videoFormatList;
    private final String vidSource;
    private String vidurl;

    public QualityBottomsheetAdapter(Activity context, List<Format> filesList, String source, boolean issingle) {
        this.context = context;
        this.filesList = addtoarrayno_m3u8(filesList);
        this.issingle = issingle;
        this.vidSource = source;
    }

    public QualityBottomsheetAdapter(Activity context, String source, boolean issingle, List<Video> filesList, boolean hasMultiAlbumb) {
        this.context = context;
        this.filesVideoList = filesList;
        this.issingle = issingle;
        this.hasMultiAlbumb = hasMultiAlbumb;
        this.vidSource = source;
    }

    public QualityBottomsheetAdapter(Activity context, String url, String source, boolean issingle) {
        this.context = context;
        this.vidurl = url;
        this.issingle = issingle;
        this.vidSource = source;
    }

    public QualityBottomsheetAdapter(Activity activity, String source, boolean issingle, List<VideoFormat> videoList_sub_video, boolean isDLAPI, String title, String id, String url) {
        this.activity = activity;
        this.videoFormatList = videoList_sub_video;
        this.issingle = issingle;
        this.vidSource = source;
        this.isDLAPI = isDLAPI;
        this.url = url;
        this.title = title;
        this.id = id;
    }

    @NonNull
    @Override
    public QualityBottomsheetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quality_bottomfragment, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull QualityBottomsheetAdapter.ViewHolder holder, final int position) {
        String resolution = "none";
        if (isDLAPI) {

            final VideoFormat videoFormatFiles = videoFormatList.get(position);
            System.out.println("reccccc VVKK working1111 06 " + videoFormatFiles.getFormat() + " >>>>> " + videoFormatFiles.getFormatId());


            if (videoFormatFiles.getFormat().length() > 20 && videoFormatFiles.getFormatNote() != null) {
                if (videoFormatFiles.getFormat().contains("-")) {
                    String[] ccc = videoFormatFiles.getFormat().split("-");
                    resolution = (ccc.length > 2) ? ccc[2] : ccc[1];
                } else {
                    resolution = videoFormatFiles.getFormatNote();
                }

            } else {
                resolution = videoFormatFiles.getFormat();
            }
            holder.resolution.setText(String.format("%s", resolution));
            holder.resolution.setSelected(true);
            holder.resolution.setSingleLine(true);

            String formatedsizew = "", newformatedsizew = "";
            newformatedsizew = iUtils.getStringSizeLengthFile(videoFormatFiles.getFileSize());
            if (newformatedsizew.contains("0.00")) {
                formatedsizew = iUtils.getStringSizeLengthFile(videoFormatFiles.getFileSizeApproximate());
            } else {
                formatedsizew = newformatedsizew;
            }
            formatedsizew = formatedsizew.replace(",", ".");
            holder.fileSize.setText(!formatedsizew.equals("") ? formatedsizew + "" : "undefined");
            String finalResolution = resolution;
            holder.downloadbtnd.setOnClickListener(v -> {

                VideoFormat formattt = videoFormatFiles;
                Data workData = new Data.Builder()
                        .putString(DownloadWorker.urlKey, url)
                        .putString(DownloadWorker.nameKey, title + finalResolution)
                        .putString(DownloadWorker.formatIdKey, formattt.getFormatId())
                        .putString(DownloadWorker.acodecKey, formattt.getAcodec())
                        .putString(DownloadWorker.vcodecKey, formattt.getVcodec())
                        .putString(DownloadWorker.taskIdKey, id + "_" + finalResolution)
                        .putString(DownloadWorker.ext, formattt.getExt())
                        .build();

                WorkRequest workRequest = new OneTimeWorkRequest.Builder(DownloadWorker.class)
                        .addTag(id)
                        .setInputData(workData)
                        .build();

                WorkManager.getInstance(activity).enqueueUniqueWork(
                        id,
                        ExistingWorkPolicy.KEEP,
                        (OneTimeWorkRequest) workRequest
                );

                activity.runOnUiThread(() -> iUtils.ShowToast(activity, activity.getResources().getString(R.string.don_start)));


//                    if (videoFormatFiles.getExt().equals("com") || videoFormatFiles.getExt().equals("")) {
//                        DownloadFileMain.startDownloading(context, videoFormatFiles.getUrl(), vidSource + "_" + System.currentTimeMillis(), ".mp4");
//                    } else if (videoFormatFiles.getExt().equals("gif")) {
//                        DownloadFileMain.startDownloading(context, videoFormatFiles.getUrl(), vidSource + "_" + System.currentTimeMillis(), "." + videoFormatFiles.getExt());
//                    } else {
//                        DownloadFileMain.startDownloading(context, videoFormatFiles.getUrl(), vidSource + "_" + System.currentTimeMillis(), "." + videoFormatFiles.getExt());
//                    }

            });


        } else {
            if (issingle) {
                System.out.println("reccccc VVKK working0000 ");

                holder.resolution.setText("HD");

                holder.fileSize.setText("undefined");
                holder.downloadbtnd.setOnClickListener(v -> DownloadFileMain.startDownloading(context, vidurl, vidSource + "_" + System.currentTimeMillis(), ".mp4"));

            } else {

                if (hasMultiAlbumb) {

                    final Video files = filesVideoList.get(position);


                    if (!files.getURL().contains(".m3u8") && files.getProtocol().contains("http")) {
                        holder.resolution.setText(String.format("%s", files.getFormat().length() > 20 && files.getFormatID() != null ? files.getFormatID() : files.getFormat()));

                        String formatedsizew = "NaN";


                        holder.fileSize.setText(formatedsizew);

                        holder.downloadbtnd
                                .setOnClickListener(
                                        v -> {
                                            if (files.getEXT().equals("com") || files.getEXT().equals("")) {
                                                DownloadFileMain.startDownloading(context, files.getURL(), vidSource + "_" + files.getTitle(), ".mp4");
                                            } else if (files.getEXT().equals("gif")) {
                                                DownloadFileMain.startDownloading(context, files.getURL(), vidSource + "_" + files.getTitle(), "." + files.getEXT());
                                            } else {
                                                DownloadFileMain.startDownloading(context, files.getURL(), vidSource + "_" + files.getTitle(), "." + files.getEXT());
                                            }
                                        });
                    }

                } else {
                    final Format files = filesList.get(position);
                    System.out.println("reccccc VVKK working1111 06 " + files.getFormat() + files.getProtocol());


                    if (!files.getURL().contains(".m3u8") && files.getProtocol().contains("http")) {


                        holder.resolution.setText(String.format("%s", files.getFormat().length() > 20 && files.getFormatNote() != null ? files.getFormatNote() : files.getFormat()));

                        String formatedsizew = iUtils.getStringSizeLengthFile(files.getFilesize());
                        formatedsizew = formatedsizew.replace(",", ".");
                        holder.fileSize.setText(!formatedsizew.equals("") ? formatedsizew + "" : "undefined");
                        holder.downloadbtnd.setOnClickListener(v -> {
                            if (files.getEXT().equals("com") || files.getEXT().equals("")) {
                                DownloadFileMain.startDownloading(context, files.getURL(), vidSource + "_" + System.currentTimeMillis(), ".mp4");
                            } else if (files.getEXT().equals("gif")) {
                                DownloadFileMain.startDownloading(context, files.getURL(), vidSource + "_" + System.currentTimeMillis(), "." + files.getEXT());
                            } else {
                                DownloadFileMain.startDownloading(context, files.getURL(), vidSource + "_" + System.currentTimeMillis(), "." + files.getEXT());
                            }

                        });
                    }

                }

            }
        }

    }


    String formet_size(long bytes) {

        System.out.println("fhsdjfsdjfsfsdfk addd long=" + bytes);

        try {

            if (bytes < 1024) {
                return bytes + "B";
            } else if (bytes < 1048576L) {
                return Math.round(bytes / 1024) + "KB";
            } else if (bytes < 1073741824) {
                return Math.round(bytes / 1048576) + "MB";
            } else if (bytes > 1073741824) {
                System.out.println("fhsdjfsdjfsfsdfk addd long=" + bytes);

                return Math.round(bytes / 1073741824) + "GB";

            }

        } catch (Exception e) {
            System.out.println("fhsdjfsdjfsfsdfk addd long eee=" + e.getMessage());

            return "NaN";

        }

        return "NaN";
    }


    @Override
    public int getItemCount() {
        if (isDLAPI) {
            return videoFormatList.size();
        }
        if (hasMultiAlbumb) {

            return filesVideoList.size();
        }
        return issingle ? 1 : filesList.size();
    }

    List<Format> addtoarrayno_m3u8(List<Format> fromList) {
        if (fromList != null) {
            List<Format> toList = new ArrayList<>();
            for (int i = 0; i < fromList.size(); i++) {
                System.out.println("fhsdjfsdjfk " + fromList.get(i).getURL());

                if (!fromList.get(i).getURL().contains(".m3u8")) {
                    System.out.println("fhsdjfsdjfk addd" + fromList.get(i).getURL());

                    toList.add(fromList.get(i));
                }
            }
            return toList;
        }
        return new ArrayList<>();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView resolution;
        TextView fileSize;
        Button downloadbtnd;


        public ViewHolder(View itemView) {
            super(itemView);
            resolution = itemView.findViewById(R.id.resolution);
            fileSize = itemView.findViewById(R.id.fileSize);
            downloadbtnd = itemView.findViewById(R.id.downloadqua);
        }
    }
}
