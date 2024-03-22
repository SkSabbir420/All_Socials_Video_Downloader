/*
 * *
 *  * Created by Syed Usama Ahmad on 3/4/23, 2:35 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/4/23, 2:19 AM
 *
 */

package com.erickruo.allinonevideodownloader.utils.work

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.work.WorkManager
import com.erickruo.allinonevideodownloader.R
import com.yausername.youtubedl_android.YoutubeDL

class CancelReceiver : BroadcastReceiver() {
    private val TAG = "CancelReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Task Canceler called")

        if (intent == null) return
        val taskId = intent.getStringExtra("taskId")
        val notificationId = intent.getIntExtra("notificationId", 0)
        if (taskId.isNullOrEmpty()) return
        val result = YoutubeDL.getInstance().destroyProcessById(taskId)
        if (result) {
            Log.d(TAG, "Task (id:$taskId) was killed.")
            WorkManager.getInstance(context!!).cancelAllWorkByTag(taskId)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as
                        NotificationManager?
            Toast.makeText(context, R.string.taskcancel, Toast.LENGTH_LONG).show()
            notificationManager?.cancel(notificationId)
        }
    }
}