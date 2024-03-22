/*
 * *
 *  * Created by Syed Usama Ahmad on 1/13/23, 11:25 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 1/4/23, 1:29 PM
 *
 */

package com.erickruo.allinonevideodownloader.models.instafollowers;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User value) {
        this.user = value;
    }
}
