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


public class ReelsHighlightsMedia {
    @SerializedName("reels")
    private Map<String, ReelHighlights> reels;
    @SerializedName("reels_media")
    private ReelHighlights[] reelsMedia;
    @SerializedName("status")
    private String status;


    public Map<String, ReelHighlights> getReels() {
        return reels;
    }

    public void setReels(Map<String, ReelHighlights> value) {
        this.reels = value;
    }

    public ReelHighlights[] getReelsMedia() {
        return reelsMedia;
    }

    public void setReelsMedia(ReelHighlights[] value) {
        this.reelsMedia = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }
}


