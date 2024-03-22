/*
 * *
 *  * Created by Syed Usama Ahmad on 1/31/23, 11:56 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 1/31/23, 11:53 PM
 *
 */

package com.erickruo.allinonevideodownloader.models.storymodels;

import com.google.gson.annotations.SerializedName;

public class CroppedImageVersion {
    @SerializedName("width")
    private long width;
    @SerializedName("height")
    private long height;
    @SerializedName("url")
    private String url;

    public long getWidth() {
        return width;
    }

    public void setWidth(long value) {
        this.width = value;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long value) {
        this.height = value;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String value) {
        this.url = value;
    }

}
