/*
 * *
 *  * Created by Syed Usama Ahmad on 3/1/23, 11:05 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/1/23, 5:30 PM
 *
 */

package com.erickruo.allinonevideodownloader.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsMainApp;
import com.erickruo.allinonevideodownloader.utils.iUtils;
import com.erickruo.allinonevideodownloader.utils.work.DownloadWorker;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.mapper.VideoFormat;
import com.yausername.youtubedl_android.mapper.VideoInfo;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

public class DLApiActivity extends AppCompatActivity {

    private static final String TAG = "DownloadTask";
    private static final String YTDL_URL = "https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp";

    private static final String FFMPEG_URL = "https://github.com/syedusama5556/Seal_downloader_yt-dlp_kotlin/releases/download/0.0.1/ffmpeg";

    private static String FFMPEG_BINARY_PATH = "/ffmpeg";
    private static String YTDLP_BINARY_PATH = "/yt-dlp";
    private String filesdirloc;
    String urkk = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlapi);


//        ContextWrapper c = new ContextWrapper(this);
//        filesdirloc = c.getFilesDir().getPath();
//        FFMPEG_BINARY_PATH = filesdirloc + FFMPEG_BINARY_PATH;
//        YTDLP_BINARY_PATH = filesdirloc + YTDLP_BINARY_PATH;


        Executors.newSingleThreadExecutor().submit(() -> {

            urkk = "https://youtube.com/watch?v=OqzdUcc3k9Y";
//                 urkk = "https://vimeo.com/22439234";

            VideoInfo streamInfo = null;
            try {

                YoutubeDL.getInstance().updateYoutubeDL(DLApiActivity.this,YoutubeDL.UpdateChannel._NIGHTLY);


                streamInfo = YoutubeDL.getInstance().getInfo(urkk);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            System.out.println(streamInfo.getFulltitle());
            System.out.println(streamInfo.getDuration());
            System.out.println(streamInfo.getId());
//            for (int i = 0; i <streamInfo.getFormats().size() ; i++) {
//                System.out.println(streamInfo.getFormats().get(i).getFormat());
////                System.out.println(streamInfo.getFormats().get(i).getUrl());
//            }

            VideoFormat formattt = streamInfo.getFormats().get(streamInfo.getFormats().size() - 1);
            Data workData = new Data.Builder()
                    .putString(DownloadWorker.urlKey, urkk)
                    .putString(DownloadWorker.nameKey, streamInfo.getFulltitle())
                    .putString(DownloadWorker.formatIdKey, formattt.getFormatId())
                    .putString(DownloadWorker.acodecKey, formattt.getAcodec())
                    .putString(DownloadWorker.vcodecKey, formattt.getVcodec())
                    .putString(DownloadWorker.taskIdKey, streamInfo.getId())
                    .putString(DownloadWorker.ext, formattt.getExt())
                    .build();

            WorkRequest workRequest = new OneTimeWorkRequest.Builder(DownloadWorker.class)
                    .addTag(streamInfo.getId())
                    .setInputData(workData)
                    .build();

            WorkManager.getInstance(DLApiActivity.this).enqueueUniqueWork(
                    streamInfo.getId(),
                    ExistingWorkPolicy.KEEP,
                    (OneTimeWorkRequest) workRequest
            );

            DLApiActivity.this.runOnUiThread(() -> iUtils.ShowToast(DLApiActivity.this, getResources().getString(R.string.don_start)));


        });


//        loadYtDlp();
//        loadFFMPEG();
//
//
//        getJsonFormVideo("https://youtube.com/watch?v=OqzdUcc3k9Y");


    }


    private File loadYtDlp() {
        File file = new File(filesdirloc, "yt-dlp");
        if (!file.exists()) {
            downloadytdlp(YTDL_URL);
        }

        file.setExecutable(true);
        return file;
    }


    private File loadFFMPEG() {
        File file = new File(filesdirloc, "ffmpeg");
        if (!file.exists()) {
            downloadFFFFmpeg(FFMPEG_URL);
        }

        file.setExecutable(true);
        String[] nonff = new String[]{"sh", "-c", "chmod", "+x", file.getAbsolutePath()};
        try {
            Process infoProcess = Runtime.getRuntime().exec(nonff);
        } catch (IOException e) {

            e.printStackTrace();
        }

        return file;
    }


    private String getJsonFormVideo(String url) {

        File ytDlpPath = new File(YTDLP_BINARY_PATH);
        File ffmpegPath = new File(FFMPEG_BINARY_PATH);
        if (ytDlpPath.exists() && ffmpegPath.exists()) {
            try {
                // Video downloaded successfully, extract video information in JSON format
                String[] infoCommand = new String[]{"sh", "-c", ytDlpPath.getAbsolutePath(), "--dump-json", "--no-cache-dir", "--ffmpeg-location", ffmpegPath.getAbsolutePath(), url};
                String[] nonff = new String[]{ytDlpPath.getAbsolutePath(), "--dump-json", "--no-cache-dir", url};
                Log.d(TAG, "Video command : " + infoCommand);

                run_command_test(nonff);
//                Process infoProcess = Runtime.getRuntime().exec(nonff);
//                BufferedReader reader = new BufferedReader(new InputStreamReader(infoProcess.getInputStream()));
//                StringBuilder output = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    output.append(line);
//                }
//                infoProcess.waitFor();
//                String json = output.toString();
//                Log.d(TAG, "Video information: " + json);

                return "json";
            } catch (Exception e) {

                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }


    public static boolean run_command_test(@NonNull String[] command1) {

        StringBuilder con = new StringBuilder();
        String result;
//


        try {
            Process process = Runtime.getRuntime().exec(command1);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((result = br.readLine()) != null) {
                con.append(result);
                con.append('\n');
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            Log.d("run_command", "command=" + command1[0] + "; crash; result=" + con);
            return false;
        }

        Log.d("run_command", "command=" + command1[0] + "; finish; result=" + con);
        return true;
    }


    public void downloadFFFFmpeg(String url) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Downloading FFmpeg");
        request.setDescription("Please wait...");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "ffmpeg");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @SuppressLint("Range")
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadId == id) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            String filePath = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                            System.out.println("downlaod cc " + filePath);
                            File ffmpegDir = new File(filesdirloc, "ffmpeg");

                            try {
                                FileUtils.copyFile(new File("/storage/emulated/0/Download/ffmpeg"), ffmpegDir);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            // Handle download failure...
                        }
                    }
                    cursor.close();
                }
                unregisterReceiver(this);
            }
        };
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, filter);
    }


    public void downloadytdlp(String url) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Downloading Dlapi");
        request.setDescription("Please wait...");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "dlapi");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @SuppressLint("Range")
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadId == id) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            String filePath = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                            System.out.println("downlaod cc " + filePath);
                            File ffmpegDir = new File(filesdirloc, "yt-dlp");

                            try {
                                FileUtils.copyFile(new File("/storage/emulated/0/Download/dlapi"), ffmpegDir);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            // Handle download failure...
                        }
                    }
                    cursor.close();
                }
                unregisterReceiver(this);
            }
        };
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, filter);
    }

    public class DownloadTask extends AsyncTask<String, Integer, File> {

        private final Activity activity;
        private final String outputDirectory;
        private ProgressDialog progressDialog;

        public DownloadTask(Activity activity, String outputDirectory) {
            this.activity = activity;
            this.outputDirectory = outputDirectory;
        }


        @Override
        protected File doInBackground(String... strings) {
            try {

                File ffmpegTar = new File(outputDirectory);

                // Extract ffmpeg binaries
                File ffmpegDir = new File(filesdirloc, "ffmpeg");
                if (!ffmpegDir.exists()) {
                    ffmpegDir.mkdirs();
                }
                String[] commands = {"tar", "xf", ffmpegTar.getAbsolutePath(), "-C", ffmpegDir.getAbsolutePath()};
                Process process = new ProcessBuilder(commands).start();
                process.waitFor();


                // Set path to ffmpeg binary
                String ffmpegPath = new File(ffmpegDir, "ffmpeg-master-latest-linuxarm64-gpl-shared/ffmpeg").getAbsolutePath();

                new SharedPrefsMainApp(DLApiActivity.this).setPREFERENCE_ffmpeg(ffmpegPath);

                // Download video

                return new File(outputDirectory);
            } catch (Exception e) {
                Log.e("DownloadTask", e.getMessage(), e);
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null) {
                Toast.makeText(activity, "Video downloaded successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Failed to download video", Toast.LENGTH_SHORT).show();
            }
        }
    }


}