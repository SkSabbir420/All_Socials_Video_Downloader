/*
 * *
 *  * Created by Syed Usama Ahmad on 1/31/23, 11:56 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 1/31/23, 11:53 PM
 *
 */

package com.erickruo.allinonevideodownloader.models.storymodels;

import com.google.gson.annotations.SerializedName;

public class CoverMedia {
    @SerializedName("cropped_image_version")
    private CroppedImageVersion croppedImageVersion;
    @SerializedName("media_id")
    private String mediaID;


    public CroppedImageVersion getCroppedImageVersion() {
        return croppedImageVersion;
    }

    public void setCroppedImageVersion(CroppedImageVersion value) {
        this.croppedImageVersion = value;
    }


    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String value) {
        this.mediaID = value;
    }

}
