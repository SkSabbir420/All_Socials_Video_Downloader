/*
 * *
 *  * Created by Syed Usama Ahmad on 3/17/23, 11:37 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/17/23, 10:41 PM
 *
 */

@file:Suppress("DEPRECATION")

package com.erickruo.allinonevideodownloader.utils

import android.app.Activity
import android.app.ProgressDialog
import androidx.appcompat.app.AlertDialog
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.erickruo.allinonevideodownloader.R
import com.erickruo.allinonevideodownloader.models.instawithlogin.CarouselMedia
import com.erickruo.allinonevideodownloader.models.instawithlogin.ModelInstaWithLogin
import com.erickruo.allinonevideodownloader.models.storymodels.ModelEdNode
import com.erickruo.allinonevideodownloader.models.storymodels.ModelGetEdgetoNode
import com.erickruo.allinonevideodownloader.models.storymodels.ModelInstagramResponse
import org.json.JSONObject
import java.lang.reflect.Type


object Constants {


    //TODO NOTE: Should make it true if you have purchased admin panel, then add info here
    const val iSAdminAttached = false
    const val adminApiUrl: String = "https://adminvideodd.infusiblecoder.com/api"

    //TODO NOTE: Should make both false if you want to upload to the play-store
    const val showyoutube = true

    //TODO NOTE: if you want to hide subscription button you can make this false
    const val show_subscription = false

    //TODO NOTE: if you want to disable ads you can make this false
    const val show_Ads = true
    const val enableTestAds = false


    const val isNonPlayStoreApp = true


    //https://api.m3u8-downloader.com/api/parse?url=https%3A%2F%2Fwww.tiktok.com%2F%40leemayy_%2Fvideo%2F7126189062129470747


    const val SAVE_FOLDER_NAME = "/Download/AIO_Status_Saver/"
    const val MY_ANDROID_10_IDENTIFIER_OF_FILE = "All_Video_Downloader_"
    const val MY_ANDROID_IDENTIFIER_OF_FILE_DL = "_All_Video_Downloader."


    const val directoryInstaShoryDirectorydownload_videos = "/InstaStory/videos/"
    const val directoryInstaShoryDirectorydownload_images = "/InstaStory/images/"
    const val directoryInstaShoryDirectorydownload_audio = "/InstaStory/audios/"


    const val PREF_APPNAME: String = "aiovidedownloader"
    const val PREF_CLIP: String = "tikVideoDownloader"
    const val FOLDER_NAME = "/WhatsApp/"
    const val FOLDER_NAME_Whatsappbusiness = "/WhatsApp Business/"
    const val FOLDER_NAME_Whatsapp_and11 = "/Android/media/com.whatsapp/WhatsApp/"
    const val FOLDER_NAME_Whatsapp_and11_B = "/Android/media/com.whatsapp.w4b/WhatsApp Business/"
    const val tiktokWebviewUrl = "https://www.tiktok.com/?lang=en"


    var myVideoUrlIs: String? = ""
    var myPhotoUrlIs: String? = ""
    lateinit var myprogressDD: ProgressDialog


    fun startInstaDownload(context: Activity, Url: String) {

        System.err.println("workkkkkkkkk cons 4$Url")

        try {
            System.err.println("workkkkkkkkk cons 4")


            val sharedPrefsFor = SharedPrefsForInstagram(context)
            val map = sharedPrefsFor.preference
            if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != "") {

                downloadInstagramImageOrVideoData(
                    context,
                    Url,
                    "ds_user_id=" + map.preferencE_USERID + "; sessionid=" + map.preferencE_SESSIONID,
                    true
                )
            } else {
                downloadInstagramImageOrVideoData(
                    context,
                    Url,
                    iUtils.myInstagramTempCookies,
                    false
                )
            }


        } catch (e: java.lang.Exception) {
            System.err.println("workkkkkkkkk 5")
            e.printStackTrace()
        }
    }


    private fun downloadInstagramImageOrVideoData(
        context: Activity,
        URL: String?,
        Cookie: String?,
        isLog: Boolean
    ) {

        myprogressDD = ProgressDialog(context)
        myprogressDD.setMessage("Loading....")
        myprogressDD.setCancelable(false)
        myprogressDD.show()

        val j = iUtils.getRandomNumber(iUtils.UserAgentsList.size)


        AndroidNetworking.get(URL!!)
            .setPriority(Priority.MEDIUM)
            .addHeaders("Cookie", Cookie!!)
            .addHeaders(
                "User-Agent",
                iUtils.UserAgentsList[j]
            )
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    println("wojfdjhfdjhtik yyyy $response")

                    try {

                        if (!isLog) {
                            val listType = object : TypeToken<ModelInstagramResponse?>() {}.type
                            val modelInstagramResponse: ModelInstagramResponse? =
                                GsonBuilder().create()
                                    .fromJson<ModelInstagramResponse>(
                                        response.toString(),
                                        listType
                                    )
                            if (modelInstagramResponse != null) {
                                if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                                    val modelGetEdgetoNode: ModelGetEdgetoNode =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children


                                    val modelEdNodeArrayList: List<ModelEdNode> =
                                        modelGetEdgetoNode.modelEdNodes
                                    for (i in modelEdNodeArrayList.indices) {
                                        if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                            myVideoUrlIs =
                                                modelEdNodeArrayList[i].modelNode.video_url
                                            DownloadFileMain.startDownloading(
                                                context,
                                                myVideoUrlIs,
                                                iUtils.getVideoFilenameFromURL(
                                                    myVideoUrlIs
                                                ),
                                                ".mp4"
                                            )
                                            // etText.setText("");
                                            try {
                                                myprogressDD.dismiss()
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                            myVideoUrlIs = ""
                                        } else {
                                            myPhotoUrlIs =
                                                modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                            DownloadFileMain.startDownloading(
                                                context,
                                                myPhotoUrlIs,
                                                iUtils.getImageFilenameFromURL(
                                                    myPhotoUrlIs
                                                ),
                                                ".png"
                                            )
                                            myPhotoUrlIs = ""
                                            try {
                                                myprogressDD.dismiss()
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                            // etText.setText("");
                                        }
                                    }
                                } else {
                                    val isVideo =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video


                                    if (isVideo) {
                                        myVideoUrlIs =
                                            modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url
                                        DownloadFileMain.startDownloading(
                                            context,
                                            myVideoUrlIs,
                                            iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                            ".mp4"
                                        )
                                        try {
                                            myprogressDD.dismiss()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                        DownloadFileMain.startDownloading(
                                            context,
                                            myPhotoUrlIs,
                                            iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                            ".png"
                                        )
                                        try {
                                            myprogressDD.dismiss()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        myPhotoUrlIs = ""
                                    }
                                }
                            } else {
                                context.runOnUiThread {
                                    iUtils.ShowToast(
                                        context,
                                        context.resources.getString(R.string.somthing),
                                    )
                                }

                                try {
                                    myprogressDD.dismiss()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        } else {

                            val listType: Type =
                                object : TypeToken<ModelInstaWithLogin?>() {}.type
                            val modelInstagramResponse: ModelInstaWithLogin = Gson().fromJson(
                                response.toString(),
                                listType
                            )
                            println("workkkkk777 " + modelInstagramResponse.items[0].code)
                            val myInstaUsername: String?

                            if (modelInstagramResponse.items[0].mediaType == 8) {
                                myInstaUsername =
                                    modelInstagramResponse.items[0].user.username + "_"

                                val modelGetEdgetoNode = modelInstagramResponse.items[0]
                                val modelEdNodeArrayList: List<CarouselMedia> =
                                    modelGetEdgetoNode.carouselMedia
                                for (i in modelEdNodeArrayList.indices) {
                                    if (modelEdNodeArrayList[i].mediaType == 2) {
                                        myVideoUrlIs =
                                            modelEdNodeArrayList[i].videoVersions[0].geturl()
                                        DownloadFileMain.startDownloading(
                                            context,
                                            myVideoUrlIs,
                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                myVideoUrlIs
                                            ),
                                            ".mp4"
                                        )
                                        // etText.setText("");
                                        try {
                                            myprogressDD.dismiss()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelEdNodeArrayList[i].imageVersions2.candidates[0]
                                                .geturl()
                                        DownloadFileMain.startDownloading(
                                            context,
                                            myPhotoUrlIs,
                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                myPhotoUrlIs
                                            ),
                                            ".png"
                                        )
                                        myPhotoUrlIs = ""
                                        try {
                                            myprogressDD.dismiss()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        // etText.setText("");
                                    }
                                }
                            } else {
                                val modelGetEdgetoNode = modelInstagramResponse.items[0]
                                myInstaUsername =
                                    modelInstagramResponse.items[0].user.username + "_"

                                if (modelGetEdgetoNode.mediaType == 2) {
                                    myVideoUrlIs =
                                        modelGetEdgetoNode.videoVersions[0].geturl()
                                    DownloadFileMain.startDownloading(
                                        context,
                                        myVideoUrlIs,
                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                            myVideoUrlIs
                                        ),
                                        ".mp4"
                                    )
                                    try {
                                        myprogressDD.dismiss()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelGetEdgetoNode.imageVersions2.candidates[0].geturl()
                                    DownloadFileMain.startDownloading(
                                        context,
                                        myPhotoUrlIs,
                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                            myPhotoUrlIs
                                        ),

                                        ".png"
                                    )
                                    try {
                                        myprogressDD.dismiss()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    myPhotoUrlIs = ""
                                }
                            }


                        }
                    } catch (e: java.lang.Exception) {
                        myprogressDD.dismiss()
                        val alertDialog = AlertDialog.Builder(context).create()
                        alertDialog.setTitle(context.getString(R.string.logininsta))
                        alertDialog.setMessage(context.getString(R.string.urlisprivate))
                        alertDialog.setButton(
                            AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel)
                        ) { dialog, _ ->
                            dialog.dismiss()
                        }
                        alertDialog.show()

                    }

                }

                override fun onError(error: ANError) {
                    println("myresponseis111 exp " + error.message)
                    try {
                        println("response1122334455:   " + "Failed1 " + error.message)
                        myprogressDD.dismiss()
                        val alertDialog = AlertDialog.Builder(context).create()
                        alertDialog.setTitle(context.getString(R.string.logininsta))
                        alertDialog.setMessage(context.getString(R.string.urlisprivate))
                        alertDialog.setButton(
                            AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel)
                        ) { dialog, _ ->
                            dialog.dismiss()
                        }
                        alertDialog.show()
                    } catch (e: Exception) {

                    }
                }
            })


    }


}