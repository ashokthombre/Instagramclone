package com.example.instagramclone.Models;

public class NotificationModel {
   private String notificationBy;
   private String notificationAt;

   private String postId;

   private String postedBy;

   private String type;
   private String checkOpen;

   private String notificationId;

    public NotificationModel() {
    }

    public NotificationModel(String notificationBy, String notificationAt, String postId, String postedBy, String type, String checkOpen) {
        this.notificationBy = notificationBy;
        this.notificationAt = notificationAt;
        this.postId = postId;
        this.postedBy = postedBy;
        this.type = type;
        this.checkOpen = checkOpen;
    }

    public NotificationModel(String notificationBy, String notificationAt, String postId, String postedBy, String type, String checkOpen, String notificationId) {
        this.notificationBy = notificationBy;
        this.notificationAt = notificationAt;
        this.postId = postId;
        this.postedBy = postedBy;
        this.type = type;
        this.checkOpen = checkOpen;
        this.notificationId = notificationId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationBy() {
        return notificationBy;
    }

    public void setNotificationBy(String notificationBy) {
        this.notificationBy = notificationBy;
    }

    public String getNotificationAt() {
        return notificationAt;
    }

    public void setNotificationAt(String notificationAt) {
        this.notificationAt = notificationAt;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCheckOpen() {
        return checkOpen;
    }

    public void setCheckOpen(String checkOpen) {
        this.checkOpen = checkOpen;
    }
}
