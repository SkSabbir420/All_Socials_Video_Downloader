/*
 * *
 *  * Created by Syed Usama Ahmad on 2/27/23, 1:22 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/26/23, 11:10 PM
 *
 */

package com.erickruo.allinonevideodownloader.models.storymodels;

import com.google.gson.annotations.SerializedName;

import java.util.Map;


public class ReelsMedia {
    @SerializedName("reels")
    private Map<String, Reel> reels;
    @SerializedName("reels_media")
    private Reel[] reelsMedia;
    @SerializedName("status")
    private String status;


    public Map<String, Reel> getReels() {
        return reels;
    }

    public void setReels(Map<String, Reel> value) {
        this.reels = value;
    }

    public Reel[] getReelsMedia() {
        return reelsMedia;
    }

    public void setReelsMedia(Reel[] value) {
        this.reelsMedia = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }
}


