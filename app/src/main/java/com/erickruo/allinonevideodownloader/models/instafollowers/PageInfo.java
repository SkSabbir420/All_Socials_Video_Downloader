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

public class PageInfo implements Serializable {
    @SerializedName("has_next_page")
    private boolean has_next_page;
    @SerializedName("end_cursor")
    private String end_cursor;

    public boolean getHas_next_page() {
        return has_next_page;
    }

    public void setHas_next_page(boolean value) {
        this.has_next_page = value;
    }

    public String getEnd_cursor() {
        return end_cursor;
    }

    public void setEnd_cursor(String value) {
        this.end_cursor = value;
    }
}
