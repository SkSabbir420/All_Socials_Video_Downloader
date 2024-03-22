/*
 * *
 *  * Created by Syed Usama Ahmad on 3/10/23, 6:48 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/8/23, 12:53 PM
 *
 */

package com.erickruo.allinonevideodownloader.receiver;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.services.MyFirebaseMessagingService;

public class DownloadBroadcastReceiver extends BroadcastReceiver {
    public static boolean isFirstTimeDownload = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (downloadId == -1)
                return;

            // query download status
            Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {

                    // download is successful
                    if (!isFirstTimeDownload) {
                        DownloadBroadcastReceiver.isFirstTimeDownload = true;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            MyFirebaseMessagingService.sendOreoNotification(context, context.getResources().getString(R.string.app_name), context.getResources().getString(R.string.yourdoncomple));
                        } else {
                            MyFirebaseMessagingService.sendNotification(context, context.getResources().getString(R.string.app_name), context.getResources().getString(R.string.yourdoncomple));
                        }
                    }
                } else {
                    Log.d("error", "error");

                }
            } else {
                Log.d("error", "error");

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}
