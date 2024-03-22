/*
 * *
 *  * Created by Syed Usama Ahmad on 2/27/23, 1:22 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/26/23, 11:10 PM
 *
 */

package com.erickruo.allinonevideodownloader.models.instafollowers;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Node implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("profile_pic_url")
    private String profilePicURL;
    @SerializedName("is_private")
    private boolean isPrivate;
    @SerializedName("is_verified")
    private boolean isVerified;
    @SerializedName("followed_by_viewer")
    private boolean followedByViewer;
    @SerializedName("requested_by_viewer")
    private boolean requestedByViewer;

    public String getID() {
        return id;
    }

    public void setID(String value) {
        this.id = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String value) {
        this.username = value;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String value) {
        this.fullName = value;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String value) {
        this.profilePicURL = value;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean value) {
        this.isPrivate = value;
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean value) {
        this.isVerified = value;
    }

    public boolean getFollowedByViewer() {
        return followedByViewer;
    }

    public void setFollowedByViewer(boolean value) {
        this.followedByViewer = value;
    }

    public boolean getRequestedByViewer() {
        return requestedByViewer;
    }

    public void setRequestedByViewer(boolean value) {
        this.requestedByViewer = value;
    }
}
