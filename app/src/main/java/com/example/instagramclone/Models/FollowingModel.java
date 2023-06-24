package com.example.instagramclone.Models;

public class FollowingModel {
    String follwingAt;

    String followingTo;

    public FollowingModel(String follwingAt, String followingTo) {
        this.follwingAt = follwingAt;
        this.followingTo = followingTo;
    }

    public FollowingModel() {
    }

    public String getFollwingAt() {
        return follwingAt;
    }

    public void setFollwingAt(String follwingAt) {
        this.follwingAt = follwingAt;
    }

    public String getFollowingTo() {
        return followingTo;
    }

    public void setFollowingTo(String followingTo) {
        this.followingTo = followingTo;
    }
}
