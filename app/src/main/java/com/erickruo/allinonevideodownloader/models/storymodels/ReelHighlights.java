/*
 * *
 *  * Created by Syed Usama Ahmad on 1/31/23, 11:56 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 1/31/23, 11:53 PM
 *
 */

package com.erickruo.allinonevideodownloader.models.storymodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReelHighlights {
    @SerializedName("id")
    private String id;
    @SerializedName("items")
    private ArrayList<ModelInstaItem> items;
    @SerializedName("prefetch_count")
    private long prefetchCount;
    @SerializedName("media_count")
    private long mediaCount;
    @SerializedName("media_ids")
    private double[] mediaIDS;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<ModelInstaItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ModelInstaItem> items) {
        this.items = items;
    }

    public long getPrefetchCount() {
        return prefetchCount;
    }

    public void setPrefetchCount(long prefetchCount) {
        this.prefetchCount = prefetchCount;
    }

    public long getMediaCount() {
        return mediaCount;
    }

    public void setMediaCount(long mediaCount) {
        this.mediaCount = mediaCount;
    }

    public double[] getMediaIDS() {
        return mediaIDS;
    }

    public void setMediaIDS(double[] mediaIDS) {
        this.mediaIDS = mediaIDS;
    }
}
