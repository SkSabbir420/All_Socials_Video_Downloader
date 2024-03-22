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

public class User implements Serializable {
    @SerializedName("edge_followed_by")
    private EdgeFollowedBy edge_followed_by;
    @SerializedName("edge_follow")
    private EdgeFollowedBy edge_follow;
    @SerializedName("edge_mutual_followed_by")
    private EdgeMutualFollowedBy edge_mutual_followed_by;

    public EdgeFollowedBy getEdge_follow() {
        return edge_follow;
    }

    public void setEdge_follow(EdgeFollowedBy edge_follow) {
        this.edge_follow = edge_follow;
    }

    public EdgeFollowedBy getEdge_followed_by() {
        return edge_followed_by;
    }

    public void setEdge_followed_by(EdgeFollowedBy value) {
        this.edge_followed_by = value;
    }

    public EdgeMutualFollowedBy getEdge_mutual_followed_by() {
        return edge_mutual_followed_by;
    }

    public void setEdge_mutual_followed_by(EdgeMutualFollowedBy value) {
        this.edge_mutual_followed_by = value;
    }
}
