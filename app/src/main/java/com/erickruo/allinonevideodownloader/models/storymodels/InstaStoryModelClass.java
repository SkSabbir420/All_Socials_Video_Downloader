/*
 * *
 *  * Created by Syed Usama Ahmad on 2/27/23, 1:22 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/26/23, 11:10 PM
 *
 */

package com.erickruo.allinonevideodownloader.models.storymodels;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@Keep
public class InstaStoryModelClass implements Serializable {

    @SerializedName("tray")
    private ArrayList<ModelUsrTray> tray;


    @SerializedName("status")
    private String status;


    public ArrayList<ModelUsrTray> getTray() {
        return tray;
    }

    public void setTray(ArrayList<ModelUsrTray> tray) {
        this.tray = tray;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
