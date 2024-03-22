/*
 * *
 *  * Created by Syed Usama Ahmad on 2/15/23, 10:26 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/15/23, 9:18 PM
 *
 */

package com.erickruo.allinonevideodownloader.models.adminpanel;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep
public class AdId implements Serializable {
    @SerializedName("bannerID")
    private String bannerID;
    @SerializedName("intID")
    private String intID;
    @SerializedName("rewardVideoID")
    private String rewardVideoID;
    @SerializedName("nativeAdID")
    private String nativeAdID;

    @SerializedName("ad_network")
    private String adNetwork;
    @SerializedName("bannerID_status")
    private long bannerIDStatus = 1;
    @SerializedName("intClicks")
    private int intClicks = 12;
    @SerializedName("intID_status")
    private long intIDStatus = 1;
    @SerializedName("rewardVideoID_status")
    private long rewardVideoIDStatus = 1;
    @SerializedName("nativeAdID_status")
    private long nativeAdIDStatus = 1;

    public String getAdNetwork() {
        return adNetwork;
    }

    public void setAdNetwork(String adNetwork) {
        this.adNetwork = adNetwork;
    }

    public boolean getBannerIDStatus() {
        return bannerIDStatus == 1;
    }

    public void setBannerIDStatus(long bannerIDStatus) {
        this.bannerIDStatus = bannerIDStatus;
    }

    public int getIntClicks() {
        return intClicks;
    }

    public void setIntClicks(int intClicks) {
        this.intClicks = intClicks;
    }

    public boolean getIntIDStatus() {
        return intIDStatus == 1;
    }

    public void setIntIDStatus(long intIDStatus) {
        this.intIDStatus = intIDStatus;
    }

    public boolean getRewardVideoIDStatus() {
        return rewardVideoIDStatus == 1;
    }

    public void setRewardVideoIDStatus(long rewardVideoIDStatus) {
        this.rewardVideoIDStatus = rewardVideoIDStatus;
    }

    public boolean getNativeAdIDStatus() {
        return nativeAdIDStatus == 1;
    }

    public void setNativeAdIDStatus(long nativeAdIDStatus) {
        this.nativeAdIDStatus = nativeAdIDStatus;
    }

    public String getBannerID() {
        return bannerID;
    }

    public void setBannerID(String value) {
        this.bannerID = value;
    }

    public String getIntID() {
        return intID;
    }

    public void setIntID(String value) {
        this.intID = value;
    }

    public String getRewardVideoID() {
        return rewardVideoID;
    }

    public void setRewardVideoID(String value) {
        this.rewardVideoID = value;
    }

    public String getNativeAdID() {
        return nativeAdID;
    }

    public void setNativeAdID(String value) {
        this.nativeAdID = value;
    }
}
