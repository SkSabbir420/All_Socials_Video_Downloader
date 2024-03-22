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
import java.util.List;

public class EdgeMutualFollowedBy implements Serializable {
    @SerializedName("count")
    private long count;
    @SerializedName("edges")
    private List<Object> edges;

    public long getCount() {
        return count;
    }

    public void setCount(long value) {
        this.count = value;
    }

    public List<Object> getEdges() {
        return edges;
    }

    public void setEdges(List<Object> value) {
        this.edges = value;
    }
}
