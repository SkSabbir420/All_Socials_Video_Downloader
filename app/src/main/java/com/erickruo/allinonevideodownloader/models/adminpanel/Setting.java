/*
 * *
 *  * Created by Syed Usama Ahmad on 2/27/23, 1:22 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/26/23, 11:10 PM
 *
 */

package com.erickruo.allinonevideodownloader.models.adminpanel;

import android.app.Activity;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
import com.erickruo.allinonevideodownloader.R;

import java.io.Serializable;

@Keep
public class Setting implements Serializable {
    @SerializedName("primarycolor")
    private String primaryColor;
    @SerializedName("secondarycolor")
    private String secondaryColor;
    @SerializedName("accentcolor")
    private String accentColor;

    @SerializedName("newui")
    private long newUi;
    @SerializedName("privacypolicy")
    public static String privacyPolicy = "";
    @SerializedName("appname")
    private String appName;
    @SerializedName("appversion")
    private String appVersion;
    @SerializedName("author")
    private String author;
    @SerializedName("contact")
    private String contact;
    @SerializedName("email")
    public static String email = "";
    @SerializedName("website")
    private String website;
    @SerializedName("applogo")
    private String appLogo;
    @SerializedName("developedby")
    private String developedBy;

    public boolean getNewUi() {
        return newUi == 1;
    }

    public void setNewUi(long newUi) {
        this.newUi = newUi;
    }

    public static String getPrivacyPolicy(Activity context, boolean isAdmin) {
        return privacyPolicy = (isAdmin && !privacyPolicy.equals("")) ? privacyPolicy : context.getString(R.string.privacy_policy_url);
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        Setting.privacyPolicy = privacyPolicy;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


    public static String getEmail(Activity context, boolean isAdmin) {
        return email = (isAdmin && !email.equals("")) ? email : context.getString(R.string.supportemail);
    }

    public void setEmail(String email) {
        Setting.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }

    public String getDevelopedBy() {
        return developedBy;
    }

    public void setDevelopedBy(String developedBy) {
        this.developedBy = developedBy;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String value) {
        this.primaryColor = value;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String value) {
        this.secondaryColor = value;
    }

    public String getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(String value) {
        this.accentColor = value;
    }
}
