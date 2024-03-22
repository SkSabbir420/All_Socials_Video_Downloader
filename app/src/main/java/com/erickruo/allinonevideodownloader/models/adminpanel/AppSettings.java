/*
 * *
 *  * Created by Syed Usama Ahmad on 2/27/23, 1:22 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/26/23, 11:10 PM
 *
 */


package com.erickruo.allinonevideodownloader.models.adminpanel;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Keep
public class AppSettings implements Serializable {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private boolean status;
    @SerializedName("adids")
    private List<AdId> adIds;
    @SerializedName("socialmedias")
    private List<SocialMedia> socialMedia;
    @SerializedName("settings")
    private List<Setting> settings;

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean value) {
        this.status = value;
    }

    public List<AdId> getAdIds() {
        return adIds;
    }

    public void setAdIds(List<AdId> value) {
        this.adIds = value;
    }

    public List<SocialMedia> getSocialMedias() {
        return socialMedia;
    }

    public void setSocialMedias(List<SocialMedia> value) {
        this.socialMedia = value;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void setSettings(List<Setting> value) {
        this.settings = value;
    }
}

