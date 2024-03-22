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

public class InstagramFollowersModel implements Serializable {
    @SerializedName("data")
    private Data data;
    @SerializedName("status")
    private String status;

    public Data getData() {
        return data;
    }

    public void setData(Data value) {
        this.data = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }
}

