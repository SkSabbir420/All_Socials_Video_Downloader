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
public class ModelHighlightsUsrTray implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("user")
    private ModelUserData user;

    @SerializedName("owner")
    private OwnerDataStoryNew owner;

    @SerializedName("media_count")
    private int media_count;

    @SerializedName("items")
    private ArrayList<ModelInstaItem> items;

    @SerializedName("cover_media")
    private CoverMedia coverMedia;


    public CoverMedia getCoverMedia() {
        return coverMedia;
    }

    public void setCoverMedia(CoverMedia value) {
        this.coverMedia = value;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ModelUserData getUser() {
        return user;
    }

    public OwnerDataStoryNew getOwner() {
        return owner;
    }

    public void setOwner(OwnerDataStoryNew owner) {
        this.owner = owner;
    }

    public void setUser(ModelUserData user) {
        this.user = user;
    }

    public int getMedia_count() {
        return media_count;
    }

    public void setMedia_count(int media_count) {
        this.media_count = media_count;
    }

    public ArrayList<ModelInstaItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ModelInstaItem> items) {
        this.items = items;
    }
}


