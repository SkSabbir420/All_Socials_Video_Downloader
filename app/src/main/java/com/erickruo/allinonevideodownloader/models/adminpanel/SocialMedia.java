/*
 * *
 *  * Created by Syed Usama Ahmad on 1/22/23, 8:42 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 1/22/23, 8:12 PM
 *
 */

package com.erickruo.allinonevideodownloader.models.adminpanel;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class SocialMedia {
    @SerializedName("source")
    private String source;
    @SerializedName("status")
    private int status;

    public String getSource() {
        return source;
    }

    public void setSource(String value) {
        this.source = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int value) {
        this.status = value;
    }
}
