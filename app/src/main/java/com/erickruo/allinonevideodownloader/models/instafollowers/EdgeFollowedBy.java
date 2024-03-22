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

public class EdgeFollowedBy implements Serializable {
    @SerializedName("count")
    private long count;
    @SerializedName("page_info")
    private PageInfo page_info;
    @SerializedName("edges")
    private List<Edge> edges;

    public long getCount() {
        return count;
    }

    public void setCount(long value) {
        this.count = value;
    }

    public PageInfo getPage_info() {
        return page_info;
    }

    public void setPage_info(PageInfo value) {
        this.page_info = value;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> value) {
        this.edges = value;
    }
}
