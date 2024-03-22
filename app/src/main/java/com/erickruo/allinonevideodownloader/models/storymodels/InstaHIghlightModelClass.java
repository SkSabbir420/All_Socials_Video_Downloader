/*
 * *
 *  * Created by Syed Usama Ahmad on 1/31/23, 11:56 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 1/31/23, 11:53 PM
 *
 */

package com.erickruo.allinonevideodownloader.models.storymodels;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@Keep
public class InstaHIghlightModelClass implements Serializable {


    @SerializedName("tray")
    private ArrayList<ModelHighlightsUsrTray> highlightsTray;

    @SerializedName("status")
    private String status;

    public ArrayList<ModelHighlightsUsrTray> getHighlightsTray() {
        return highlightsTray;
    }

    public void setHighlightsTray(ArrayList<ModelHighlightsUsrTray> highlightsTray) {
        this.highlightsTray = highlightsTray;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
