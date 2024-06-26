package com.erickruo.allinonevideodownloader.models.bulkdownloader;


import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep
public class EdgeFollowedBy implements Serializable {
    int count;

    @SerializedName("count")
    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
