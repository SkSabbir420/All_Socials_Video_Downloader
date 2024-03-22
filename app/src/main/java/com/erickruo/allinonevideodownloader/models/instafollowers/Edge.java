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

public class Edge implements Serializable {
    @SerializedName("node")
    private Node node;

    public Node getNode() {
        return node;
    }

    public void setNode(Node value) {
        this.node = value;
    }
}
