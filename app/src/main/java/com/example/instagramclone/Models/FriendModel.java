package com.example.instagramclone.Models;

public class FriendModel {
    private String followedBy;
    private long followedAt;

    public FriendModel() {
    }

    public FriendModel(String followedBy, long followedAt) {
        this.followedBy = followedBy;
        this.followedAt = followedAt;
    }

    public String getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(String followedBy) {
        this.followedBy = followedBy;
    }

    public long getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(long followedAt) {
        this.followedAt = followedAt;
    }
}
