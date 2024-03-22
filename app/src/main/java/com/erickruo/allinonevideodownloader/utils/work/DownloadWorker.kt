/*
 * *
 *  * Created by Syed Usama Ahmad on 3/14/23, 5:04 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/14/23, 5:01 PM
 *
 */

package com.erickruo.allinonevideodownloader.utils.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.erickruo.allinonevideodownloader.R
import com.erickruo.allinonevideodownloader.services.MyFirebaseMessagingService
import com.erickruo.allinonevideodownloader.utils.Constants
import com.erickruo.allinonevideodownloader.utils.Constants.directoryInstaShoryDirectorydownload_audio
import com.erickruo.allinonevideodownloader.utils.Constants.directoryInstaShoryDirectorydownload_images
import com.erickruo.allinonevideodownloader.utils.Constants.directoryInstaShoryDirectorydownload_videos
import com.erickruo.allinonevideodownloader.utils.iUtils
import com.erickruo.allinonevideodownloader.utils.iUtils.createFilenameWithJapneseAndOthers
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.io.FilenameUtils
import java.io.File

class DownloadWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager?

    override suspend fun doWork(): Result {

        val url = inputData.getString(urlKey)!!
        val name = createFilenameWithJapneseAndOthers(inputData.getString(nameKey)!!)
        println("myfilepath nameeAFTER = $name")
        val formatId = inputData.getString(formatIdKey)!!
        val acodec = inputData.getString(acodecKey)
        val vcodec = inputData.getString(vcodecKey)
        val taskId = inputData.getString(taskIdKey)!!
        val ext = inputData.getString(ext)!!

        createNotificationChannel()
        val notificationId = id.hashCode()
        val notification = NotificationCompat.Builder(
            applicationContext,
            iUtils.myNotificationChannel
        )
            .setSmallIcon(R.drawable.ic_download_yellow)
            .setContentTitle(name)
            .setSound(null)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .setContentText(applicationContext.getString(R.string.don_start))
            .build()

        val foregroundInfo = ForegroundInfo(notificationId, notification)
        setForeground(foregroundInfo)

        val request = YoutubeDLRequest(url)


        try {
            val file_v = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + directoryInstaShoryDirectorydownload_videos
            )
            if (!file_v.exists()) {
                file_v.mkdir()
            }
            val file_i = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + directoryInstaShoryDirectorydownload_images
            )
            if (!file_i.exists()) {
                file_i.mkdir()
            }
            val file_a = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + directoryInstaShoryDirectorydownload_audio
            )
            if (!file_a.exists()) {
                file_a.mkdir()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

        var mypath: String = ""
        var myseleext: String = ""

        myseleext = ext
        println("myfilepath ext = $ext")

        when (ext) {
            "png", "jpg", "gif", "jpeg" -> {
                mypath =
                    Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_DOWNLOADS + directoryInstaShoryDirectorydownload_images
            }
            "mp4", "webm" -> {
                myseleext = "mp4";
                mypath =
                    Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_DOWNLOADS + directoryInstaShoryDirectorydownload_videos

            }
            "mp3", "m4a", "wav" -> {
                mypath =
                    Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_DOWNLOADS + directoryInstaShoryDirectorydownload_audio
            }
        }


        val tmpFile = withContext(Dispatchers.IO) {
            File.createTempFile("allvideodd", null, applicationContext.externalCacheDir)
        }
        tmpFile.delete()
        tmpFile.mkdir()
        tmpFile.deleteOnExit()


//        val tmpFile = File.createTempFile("directVdownload", null, applicationContext.externalCacheDir)
        val destFileDir = File(mypath)
        println("myfilepath folder = " + tmpFile.absolutePath)

        request.addOption("-o", "${tmpFile.absolutePath}/${name}.%(ext)s")
//        request.addOption("-o", "${tmpFile.absolutePath}/${name}.${myseleext}")
        val videoOnly = vcodec != "none" && acodec == "none"
        if (videoOnly) {
            request.addOption("-f", "${formatId}+bestaudio")
        } else {
            request.addOption("-f", formatId)
        }



        try {
            YoutubeDL.getInstance()
                .execute(request, taskId) { progress, _, line ->
                    showProgress(id.hashCode(), taskId, name, progress.toInt(), line, tmpFile)
                }

            tmpFile.listFiles()!!.forEach {
                org.apache.commons.io.FileUtils.moveFile(
                    it.absoluteFile,
                    File(
                        destFileDir.absolutePath + File.separator + FilenameUtils.removeExtension(it.name) + System.currentTimeMillis() + Constants.MY_ANDROID_IDENTIFIER_OF_FILE_DL + it.extension
                    )
                )
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MyFirebaseMessagingService.sendOreoNotification(
                    applicationContext,
                    applicationContext.resources.getString(R.string.app_name),
                    applicationContext.resources.getString(R.string.yourdoncomple)
                )
            } else {
                MyFirebaseMessagingService.sendNotification(
                    applicationContext,
                    applicationContext.resources.getString(R.string.app_name),
                    applicationContext.resources.getString(R.string.yourdoncomple)
                )
            }
        } catch (e: Throwable) {
            Log.e("allvideodd error ", e.message.toString())
            e.printStackTrace()
        } finally {
            tmpFile.deleteRecursively()
        }


        return Result.success()
    }

    private fun showProgress(
        id: Int,
        taskId: String,
        name: String,
        progress: Int,
        line: String,
        tmpFile: File
    ) {
        val text = line.replace(tmpFile.toString(), "")
        val intent = Intent(applicationContext, CancelReceiver::class.java)
            .putExtra("taskId", taskId)
            .putExtra("notificationId", id)

        val pendingIntent =
            PendingIntent.getBroadcast(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        val notification = NotificationCompat.Builder(
            applicationContext,
            iUtils.myNotificationChannel
        )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.drawable.ic_download_yellow)
            .setContentTitle(name)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(text)
            )
            .setSound(null)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .setProgress(100, progress, progress == -1)
            .addAction(
                R.drawable.ic_close_24dp,
                applicationContext.getString(R.string.cancel),
                pendingIntent
            )
            .build()
        notificationManager?.notify(id, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel =
                notificationManager?.getNotificationChannel(iUtils.myNotificationChannel)
            if (notificationChannel == null) {
                val channelName = applicationContext.getString(R.string.app_name)
                notificationChannel = NotificationChannel(
                    iUtils.myNotificationChannel,
                    channelName, NotificationManager.IMPORTANCE_LOW
                )
                notificationChannel.description =
                    channelName
                notificationManager?.createNotificationChannel(notificationChannel)
            }
        }
    }

    companion object {
        const val urlKey = "url"
        const val nameKey = "name"
        const val formatIdKey = "formatId"
        const val acodecKey = "acodec"
        const val vcodecKey = "vcodec"
        const val taskIdKey = "taskId"
        const val ext = "ext"
    }
}